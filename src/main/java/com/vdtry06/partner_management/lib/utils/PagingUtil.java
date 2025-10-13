package com.vdtry06.partner_management.lib.utils;

public class PagingUtil {
    public static final String DEFAULT_PAGE = "1";
    public static final String DEFAULT_SIZE = "10";
    public static final int DEFAULT_SIZE_INT = Integer.parseInt(DEFAULT_SIZE);

    // page: trang so bn?,  perSize : so ban ghi tren moi trang
    // tra ve vi tri bat dau lay du lieu trong db
    public static Integer getOffSet(Integer page, Integer perSize) {
        return (page - 1) * (perSize == null ? DEFAULT_SIZE_INT : perSize);
    }

    // total record: tong cac ban ghi, perSize : so ban ghi tren moi trang
    // tra ve tong so trang
    public static Integer getTotalPage(Long totalRecord, Integer perSize) {
        return (int) Math.ceil((double) totalRecord / (perSize == null ? DEFAULT_SIZE_INT : perSize));
    }
}
