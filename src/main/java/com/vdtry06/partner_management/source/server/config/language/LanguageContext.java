package com.vdtry06.partner_management.source.server.config.language;

import java.util.Locale;

// quản lý Locale (ngôn ngữ/vùng miền) hiện tại cho mỗi request của người dùng
public class LanguageContext {
    /*
        ThreadLocal giúp lưu ngôn ngữ riêng cho từng thread (mỗi request có thể có locale khác nhau)
        Điều này đảm bảo rằng nếu 10 người dùng truy cập cùng lúc, mỗi người có thể có một ngôn ngữ khác nhau mà không bị lẫn lộn
     */
    private static final ThreadLocal<Locale> context = new ThreadLocal<>();

    // Gán Locale cho thread hiện tại
    public static void setLocale(Locale locale) {
        context.set(locale);
    }

    // Lấy Locale của thread hiện tại, mặc định ENGLISH nếu chưa set
    public static Locale getLocale() {
        return context.get() != null ? context.get() : Locale.ENGLISH;
    }

    // Xóa Locale sau khi xử lý xong để tránh rò rỉ bộ nhớ
    public static void clear() {
        context.remove();
    }
}
