package com.vdtry06.partner_management.source.server.config.language;

import com.vdtry06.partner_management.lib.enumerated.Language;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/*
    @Target({ElementType.METHOD, ElementType.TYPE}): Cho phép sử dụng annotation này trên phương thức (method) hoặc lớp (class)
    @Retention(RetentionPolicy.RUNTIME): Đảm bảo annotation được giữ lại và có thể được đọc/xử lý tại thời điểm chạy (runtime) bởi Spring.
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface DetectLanguage { // custom annotation
    // Chỉ định ngôn ngữ mặc định cho 1 API
    Language value() default Language.EN;
}
