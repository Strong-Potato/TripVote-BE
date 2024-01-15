package fc.be.openapi.tourapi;

import fc.be.openapi.tourapi.dto.request.*;
import fc.be.openapi.tourapi.dto.response.bone.PlaceDTO;
import fc.be.openapi.tourapi.tools.DomainConverter;
import fc.be.openapi.tourapi.tools.PlaceDTOMerger;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TourAPIService {
    private final DomainConverter domainConverter;
    private final TourAPIClient tourAPIClient;

    public List<PlaceDTO> bringAreaBasedSyncDomains(AreaBasedSync1Request areaBasedSync1Request) {
        return tourAPIClient.callByAreaBasedSync(areaBasedSync1Request).map(result ->
                domainConverter.fromAreaBasedSyncList(result.getItems(), areaBasedSync1Request.contentTypeId())
        ).orElse(null);
    }

    public List<PlaceDTO> bringSearchKeywordDomains(SearchKeyword1Request searchKeyword1Request) {
        return tourAPIClient.callBySearchKeyword(searchKeyword1Request).map(result ->
                domainConverter.fromSearchKeywordList(result.getItems(), searchKeyword1Request.contentTypeId())
        ).orElse(null);
    }

    public PlaceDTO bringDetailDomain(DetailRequest detailRequest) {
        return PlaceDTOMerger.merge(
                bringDetailCommonDomain(detailRequest.toDetailCommon()),
                bringDetailImageDomains(detailRequest.toDetailImage()),
                bringDetailIntroDomain(detailRequest.toDetailIntro())
        );
    }

    private PlaceDTO bringDetailCommonDomain(DetailCommon1Request detailCommon1Request) {
        return tourAPIClient.callByDetailCommon(detailCommon1Request).map(result ->
                domainConverter.fromDetailCommon(result.getItems().getFirst(), detailCommon1Request.contentTypeId())
        ).orElse(null);
    }

    private PlaceDTO bringDetailIntroDomain(DetailIntro1Request detailIntro1Request) {
        return tourAPIClient.callByDetailIntro(detailIntro1Request).map(result ->
                domainConverter.fromDetailIntro(result.getItems().getFirst(), detailIntro1Request.contentTypeId())
        ).orElse(null);
    }

    private PlaceDTO bringDetailImageDomains(DetailImage1Request detailImage1Request) {
        return tourAPIClient.callByDetailImage(detailImage1Request).map(result ->
                domainConverter.fromDetailImageList(result.getItems(), detailImage1Request.contentTypeId())
        ).orElse(null);
    }

}