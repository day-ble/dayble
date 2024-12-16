package itcast.dto.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PageResponse<T> {
    private final List<T> contents;
    private final int page;
    private final int size;
    private final int totalPages;
}
