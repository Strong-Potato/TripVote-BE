package fc.be.tourapi.dto.mapper;

import fc.be.domain.place.Place;

/**
 * TourAPI의 응답을 도메인 객체로 변환하는 인터페이스
 *
 * @param <T> 장소 상위 도메인 객체. Place
 * @param <I> TourAPI의 응답 객체
 */
public interface TourAPIMapper<T extends Place, I> {

    /**
     * TourAPI의 응답 객체를 도메인 객체로 변환한다.
     *
     * @param item TourAPI의 응답 객체
     * @return 도메인 객체
     */
    T generate(I item);
}