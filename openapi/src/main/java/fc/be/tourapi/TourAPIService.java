package fc.be.tourapi;

import fc.be.domain.place.Place;
import fc.be.tourapi.exception.NoItemsFromAPIException;
import fc.be.tourapi.tools.TourAPICommunicator;
import fc.be.tourapi.tools.TourAPIDomainConverter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TourAPIService {
    private final TourAPIDomainConverter tourAPIDomainConverter;

    public <T> List<T> bringAreaBasedSyncDomains(
            final int pageNo,
            final int numOfRows,
            final int areaCode,
            final int sigunguCode,
            final int contentTypeId,
            final Class<T> areaBasedSyncListClass
    ) {
        var result = TourAPICommunicator.callAreaBasedSync(
                pageNo, numOfRows,
                areaCode, sigunguCode,
                contentTypeId
        );

        var body = result.response().body();

        if (body.numOfRows() == 0) {
            throw new NoItemsFromAPIException();
        }

        var item = body.items().item();


        return tourAPIDomainConverter.buildAreaBasedSyncListFromItem(item, areaBasedSyncListClass);
    }

    public <T> List<T> bringSearchKeywordDomains(
            final int pageNo,
            final int numOfRows,
            final int areaCode,
            final int sigunguCode,
            final int contentTypeId,
            final String keyword,
            final Class<T> searchKeywordClass
    ) {
        var result = TourAPICommunicator.callSearchKeyword(
                pageNo, numOfRows,
                areaCode, sigunguCode,
                keyword,
                contentTypeId
        );

        var body = result.response().body();

        if (body.numOfRows() == 0) {
            throw new NoItemsFromAPIException();
        }

        var item = body.items().item();

        return tourAPIDomainConverter.buildSearchKeywordListFromItem(item, searchKeywordClass);
    }

    public <T> T bringDetailCommonDomain(
            final int contentId,
            final int contentTypeId,
            final Class<T> detailCommonClass
    ) {
        var result = TourAPICommunicator.callDetailCommon(contentId, contentTypeId);

        var body = result.response().body();

        if (body.numOfRows() == 0) {
            throw new NoItemsFromAPIException();
        }

        var item = body.items().item().getFirst();

        return tourAPIDomainConverter.buildDetailCommonFromItem(item, detailCommonClass);
    }

    public <T> T bringDetailIntroDomain(
            final int contentId,
            final int contentTypeId,
            final Class<T> detailIntroClass
    ) {
        var result = TourAPICommunicator.callDetailIntro(contentId, contentTypeId);

        var body = result.response().body();

        if (body.numOfRows() == 0) {
            throw new NoItemsFromAPIException();
        }

        var item = body.items().item().getFirst();

        return tourAPIDomainConverter.buildDetailIntroFromItem(item, detailIntroClass);
    }

    public <T extends Place> T bringDetailImageDomains(
            final int contentId,
            final Class<T> detailImageListClass
    ) {
        var result = TourAPICommunicator.callDetailImage(contentId);

        var body = result.response().body();

        if (body.numOfRows() == 0) {
            throw new NoItemsFromAPIException();
        }

        var item = body.items().item();

        return tourAPIDomainConverter.buildDetailImageListFromItem(item, detailImageListClass);
    }


}