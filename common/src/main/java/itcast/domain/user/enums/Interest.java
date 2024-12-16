package itcast.domain.user.enums;

import java.util.Arrays;

public enum Interest {
    FRONTEND,
    BACKEND,
    NEWS;

    public static Interest from(final String category) {
        return Arrays.stream((values()))
                .filter(value -> value.name().equalsIgnoreCase(category.trim()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("카테고리가 유효하지 않습니다."));
        // TODO 커스텀 Exception 만들어야 함
    }
}
