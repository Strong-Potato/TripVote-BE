package fc.be.openapi.tourapi;

import fc.be.openapi.config.TourAPIProperties;
import fc.be.openapi.tourapi.dto.request.*;
import fc.be.openapi.tourapi.dto.response.form.diff_property.detail_intro1.DetailIntro1Response;
import fc.be.openapi.tourapi.dto.response.form.same_property.area_based_sync_list1.AreaBasedSyncList1Response;
import fc.be.openapi.tourapi.dto.response.form.same_property.detail_common1.DetailCommon1Response;
import fc.be.openapi.tourapi.dto.response.form.same_property.detail_image1.DetailImage1Response;
import fc.be.openapi.tourapi.dto.response.form.same_property.search_keyword1.SearchKeyword1Response;
import fc.be.openapi.tourapi.tools.Communicator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static fc.be.openapi.tourapi.tools.TourAPIKeyChanger.getEncodedKey;

@Component
@RequiredArgsConstructor
public class TourAPIClient {
    private final Communicator communicator;
    private final TourAPIProperties properties;

    public Optional<SearchKeyword1Response> callBySearchKeyword(SearchKeyword1Request searchKeyword1Request) {
        StringBuilder url = buildEssentialUrl(properties.getSearchKeyword());

        return communicator.fetchDataFromAPI(
                searchKeyword1Request.appendQueryStrings(url),
                SearchKeyword1Response.class
        );
    }

    public Optional<DetailCommon1Response> callByDetailCommon(DetailCommon1Request detailCommon1Request) {
        StringBuilder url = buildEssentialUrl(properties.getDetailCommon());

        return communicator.fetchDataFromAPI(
                detailCommon1Request.appendQueryStrings(url),
                DetailCommon1Response.class
        );
    }

    public Optional<DetailIntro1Response> callByDetailIntro(DetailIntro1Request detailIntro1Request) {
        StringBuilder url = buildEssentialUrl(properties.getDetailIntro());

        return communicator.fetchDataFromAPI(
                detailIntro1Request.appendQueryStrings(url),
                DetailIntro1Response.class
        );
    }

    public Optional<DetailImage1Response> callByDetailImage(DetailImage1Request detailImage1Request) {
        StringBuilder url = buildEssentialUrl(properties.getDetailImage());

        return communicator.fetchDataFromAPI(
                detailImage1Request.appendQueryStrings(url),
                DetailImage1Response.class
        );
    }

    public Optional<AreaBasedSyncList1Response> callByAreaBasedSync(AreaBasedSync1Request areaBasedSync1Request) {
        StringBuilder url = buildEssentialUrl(properties.getAreaBasedSyncList());

        return communicator.fetchDataFromAPI(
                areaBasedSync1Request.appendQueryStrings(url),
                AreaBasedSyncList1Response.class
        );
    }

    private StringBuilder buildEssentialUrl(final String endpoint) {
        return new StringBuilder(properties.getBaseUrl() + endpoint)
                .append("?MobileOS=").append(properties.getMobileOs())
                .append("&MobileApp=").append(properties.getMobileApp())
                .append("&_type=").append(properties.getRenderType())
                .append("&serviceKey=").append(getEncodedKey());
    }

}