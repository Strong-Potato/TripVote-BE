package fc.be.openapi.google.dto.search;

import fc.be.openapi.google.dto.search.form.Place;

import java.util.Collections;
import java.util.List;

public record GoogleSearchResponse(List<Place> places) {
    public GoogleSearchResponse {
        if(places == null){
            places = Collections.emptyList();
        }
    }
}