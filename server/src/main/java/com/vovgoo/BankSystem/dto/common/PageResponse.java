package com.vovgoo.BankSystem.dto.common;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class PageResponse<T> {

    private final List<T> content;
    private final int pageNumber;
    private final int pageSize;
    private final long totalElements;
    private final int totalPages;

    public static <T> PageResponse<T> of(List<T> content, int pageNumber, int pageSize, long totalElements) {
        List<T> safeContent = content != null ? content : List.of();
        int safePageSize = pageSize > 0 ? pageSize : 1;
        int totalPages = (int) Math.ceil((double) totalElements / safePageSize);

        return PageResponse.<T>builder()
                .content(safeContent)
                .pageNumber(pageNumber)
                .pageSize(safePageSize)
                .totalElements(totalElements)
                .totalPages(totalPages)
                .build();
    }
}