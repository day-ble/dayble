package itcast.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import java.util.List;

@Getter
@AllArgsConstructor
public class PageResponse<T> {
    private final List<T> contents;
    private final int page;
    private final int size;
    private final int totalPages;
}
