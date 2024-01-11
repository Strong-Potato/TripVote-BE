package fc.be.openapi.tourapi;

import fc.be.openapi.tourapi.dto.bone.PlaceDTO;
import fc.be.openapi.tourapi.exception.TourAPIError;
import fc.be.openapi.tourapi.tools.TourAPICommunicator;
import fc.be.openapi.tourapi.tools.TourAPIDomainConverter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

import static fc.be.openapi.tourapi.exception.TourAPIErrorCode.NO_ITEMS_FROM_API;

@Slf4j
@Service
@RequiredArgsConstructor
public class TourAPIService {
    private final TourAPIDomainConverter tourAPIDomainConverter;
    private final TourAPICommunicator tourAPICommunicator;

    public List<PlaceDTO> bringAreaBasedSyncDomains(
            final int pageNo,
            final int numOfRows,
            final int areaCode,
            final int sigunguCode,
            final int contentTypeId,
            final char sortedBy,
            final String categoryCode
    ) {
        var result = tourAPICommunicator.callAreaBasedSync(
                pageNo, numOfRows,
                areaCode, sigunguCode,
                contentTypeId,
                sortedBy,
                categoryCode
        );

        if (result == null) {
            return null;
        }

        var body = result.response().body();

        if (body.numOfRows() == 0) {
            new TourAPIError(NO_ITEMS_FROM_API);
            return Collections.emptyList();
        }

        var item = body.items().item();

        return tourAPIDomainConverter.buildAreaBasedSyncListFromItem(item, contentTypeId);
    }

    public List<PlaceDTO> bringSearchKeywordDomains(
            final int pageNo,
            final int numOfRows,
            final int areaCode,
            final int sigunguCode,
            final int contentTypeId,
            final String keyword,
            final char sortedBy,
            final String categoryCode
    ) {
        var result = tourAPICommunicator.callSearchKeyword(
                pageNo, numOfRows,
                areaCode, sigunguCode,
                keyword,
                contentTypeId,
                sortedBy,
                categoryCode
        );

        if (result == null) {
            return null;
        }

        var body = result.response().body();

        if (body.numOfRows() == 0) {
            new TourAPIError(NO_ITEMS_FROM_API);
            return Collections.emptyList();
        }

        var item = body.items().item();

        return tourAPIDomainConverter.buildSearchKeywordListFromItem(item, contentTypeId);
    }

    public PlaceDTO bringDetailCommonDomain(
            final int contentId,
            final int contentTypeId
    ) {
        var result = tourAPICommunicator.callDetailCommon(contentId, contentTypeId);

        if (result == null) {
            return null;
        }

        var body = result.response().body();

        if (body.numOfRows() == 0) {
            new TourAPIError(NO_ITEMS_FROM_API);
            return null;
        }

        var item = body.items().item().getFirst();

        return tourAPIDomainConverter.buildDetailCommonFromItem(item, contentTypeId);
    }

    public PlaceDTO bringDetailIntroDomain(
            final int contentId,
            final int contentTypeId
    ) {
        var result = tourAPICommunicator.callDetailIntro(contentId, contentTypeId);

        if (result == null) {
            return null;
        }

        var body = result.response().body();

        if (body.numOfRows() == 0) {
            new TourAPIError(NO_ITEMS_FROM_API);
            return null;
        }

        var item = body.items().item().getFirst();

        return tourAPIDomainConverter.buildDetailIntroFromItem(item, contentTypeId);
    }

    public PlaceDTO bringDetailImageDomains(
            final int contentId,
            final int contentTypeId
    ) {
        var result = tourAPICommunicator.callDetailImage(contentId);

        if (result == null) {
            return null;
        }

        var body = result.response().body();

        if (body.numOfRows() == 0) {
            new TourAPIError(NO_ITEMS_FROM_API);
            return null;
        }

        var item = body.items().item();

        return tourAPIDomainConverter.buildDetailImageListFromItem(item, contentTypeId);
    }


}