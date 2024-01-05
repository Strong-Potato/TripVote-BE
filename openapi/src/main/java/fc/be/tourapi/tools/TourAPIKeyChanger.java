package fc.be.tourapi.tools;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.IntStream;

@Slf4j
@Component
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TourAPIKeyChanger {
    private static int keyIndex = 0;

    private static final List<String> keyList = IntStream.rangeClosed(1, 5)
            .mapToObj(i -> System.getenv("TOUR_API_KEY_" + i))
            .toList();

    public static String getEncodedKey() {
        return keyList.get(keyIndex);
    }

    public static void changeNextKey() {
        log.info("API KEY를 변경합니다.");
        keyIndex = ++keyIndex % keyList.size();
    }
}
