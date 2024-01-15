package fc.be.openapi.tourapi.dto.response.form;

import java.util.List;

public interface TourAPIResponse {

    boolean isItemEmpty();

    <T> List<T> getItems();
}
