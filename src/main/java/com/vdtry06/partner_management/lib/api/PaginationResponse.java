package com.vdtry06.partner_management.lib.api;

import lombok.*;

import java.io.Serializable;
import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PaginationResponse<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    private int page;
    private int perPage;
    private List<T> data;
    private long totalRecord;
    private long totalPage;

}
