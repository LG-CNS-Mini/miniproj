package com.lgcns.beinstagramclone.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.List;

@Data @AllArgsConstructor
public class SliceResponseDTO<T> {
    private List<T> items;
    private int page;
    private int size;
    private boolean hasNext;
}
