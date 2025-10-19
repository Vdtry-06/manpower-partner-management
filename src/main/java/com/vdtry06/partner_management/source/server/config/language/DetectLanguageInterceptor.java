package com.vdtry06.partner_management.source.server.config.language;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Locale;
/*
    Dùng để "can thiệp" vào quá trình xử lý request trước và sau khi controller method được gọi
 */
@Component
@RequiredArgsConstructor
public class DetectLanguageInterceptor implements HandlerInterceptor {
    private final String LANGUAGE_HEADER = "Accept-Language";

    // Trước khi Controller được gọi
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if (!(handler instanceof HandlerMethod handlerMethod)) {
            return true;
        }

        // Kiểm tra Annotation
        DetectLanguage detectLanguage = handlerMethod.getMethodAnnotation(DetectLanguage.class);

        // Xác định Ngôn ngữ Mặc định
        Locale defaultLocale = detectLanguage != null ? Locale.forLanguageTag(detectLanguage.value().name()) : Locale.ENGLISH;

        // Phát hiện Ngôn ngữ từ Header
        String languageTag = request.getHeader(LANGUAGE_HEADER);

        // Xác định Locale Cuối cùng
        Locale locale = (languageTag != null && !languageTag.isEmpty()) ? Locale.forLanguageTag(languageTag) : defaultLocale;
        LanguageContext.setLocale(locale);
        return true;
    }

    // Sau khi Request hoàn thành
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        LanguageContext.clear();
    }
}
