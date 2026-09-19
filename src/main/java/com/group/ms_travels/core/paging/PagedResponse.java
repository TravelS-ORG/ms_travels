package com.group.ms_travels.core.paging;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@AllArgsConstructor
public class PagedResponse<T> {

    private List<T> items;
    private int page;
    private int pageSize;
    private long totalItems;
    private int totalPages;

    public static <T> PagedResponse<T> from(Page<T> page) {
        return new PagedResponse<>(
            page.getContent(),
            page.getNumber() +1,
            page.getSize(),
            page.getTotalElements(),
            page.getTotalPages()
        );
    }

}
