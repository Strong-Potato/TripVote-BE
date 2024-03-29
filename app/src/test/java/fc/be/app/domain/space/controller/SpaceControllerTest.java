package fc.be.app.domain.space.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import fc.be.app.domain.space.dto.request.DateUpdateRequest;
import fc.be.app.domain.space.dto.request.TitleUpdateRequest;
import fc.be.app.domain.space.dto.response.SpaceResponse;
import fc.be.app.domain.space.service.SpaceService;
import fc.be.app.global.config.WebConfig;
import fc.be.app.global.config.security.SecurityAppConfig;
import fc.be.app.global.config.security.SecurityConfig;
import fc.be.app.global.interceptor.SpaceInterceptor;
import fc.be.app.domain.space.service.SpaceTokenService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.security.oauth2.client.servlet.OAuth2ClientAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = SpaceController.class,
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE,
                        classes = {SecurityConfig.class, SecurityAppConfig.class, WebConfig.class})
        },
        excludeAutoConfiguration = {
                SecurityAutoConfiguration.class,
                OAuth2ClientAutoConfiguration.class
        }
)
class SpaceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private SpaceService spaceService;

    @MockBean
    private CommandLineRunner commandLineRunner;

    @MockBean
    private SpaceInterceptor spaceInterceptor;

    @MockBean
    private SpaceTokenService spaceVisitService;

    @DisplayName("여행 스페이스를 생성한다.")
    @Test
    void createSpace() throws Exception {
        // when then
        mockMvc.perform(
                        post("/spaces")
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("여행스페이스 식별자값으로 여행스페이스 값을 조회한다.")
    @Test
    void getSpaceById() throws Exception {
        // given
        SpaceResponse spaceResponse = SpaceResponse.builder()
                .id(1L)
                .title("부산 여행")
                .members(List.of(SpaceResponse.MemberInfo.builder()
                        .id(1L)
                        .build()))
                .build();
        when(spaceService.getSpaceById(anyLong(), any())).thenReturn(spaceResponse);

        // when then
        Long spaceId = 1L;
        mockMvc.perform(
                        get("/spaces/{spaceId}", spaceId)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("200"))
                .andExpect(jsonPath("$.message").value("SUCCESS"))
                .andExpect(jsonPath("$.data.title").value("부산 여행"));
    }

    @DisplayName("여행스페이스의 타이틀을 수정한다.")
    @Test
    void updateSpaceByTitle() throws Exception {
        // given
        TitleUpdateRequest updateRequest = TitleUpdateRequest.builder()
                .cities(List.of("가평"))
                .build();

        // when then
        mockMvc.perform(
                        put("/spaces/1/title")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(updateRequest))
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("여행스페이스의 타이틀의 값이 넘어오지 않은 경우 예외를 발생시킨다.")
    @Test
    void updateSpaceByTitleWithoutTitle() throws Exception {
        // given
        TitleUpdateRequest updateRequest = TitleUpdateRequest.builder()
                .build();

        // when then
        mockMvc.perform(
                        put("/spaces/1/title")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(updateRequest))
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Bad Request"))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.detail").value("Invalid request content."))
                .andExpect(jsonPath("$.instance").value("/spaces/1/title"));
    }

    @DisplayName("여행스페이스의 날짜값을 수정한다.")
    @Test
    void updateSpaceByDates() throws Exception {
        // given
        DateUpdateRequest updateRequest = DateUpdateRequest.builder()
                .startDate(LocalDate.of(2024, 1, 3))
                .endDate(LocalDate.of(2024, 1, 5))
                .build();

        // when then
        mockMvc.perform(
                        put("/spaces/1/dates")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(updateRequest))
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("여행스페이스의 날짜값이 넘어오지 않은 경우 예외를 발생시킨다.")
    @Test
    void updateSpaceByDatesWithoutDates() throws Exception {
        // given
        DateUpdateRequest updateRequest = DateUpdateRequest.builder()
                .build();

        // when then
        mockMvc.perform(
                        put("/spaces/1/dates")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(updateRequest))
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Bad Request"))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.detail").value("Invalid request content."))
                .andExpect(jsonPath("$.instance").value("/spaces/1/dates"));
    }
}