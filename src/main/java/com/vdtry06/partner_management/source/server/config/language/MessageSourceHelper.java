package com.vdtry06.partner_management.source.server.config.language;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

// Hỗ trợ Lấy Thông báo
@Configuration
public class MessageSourceHelper {
    @Bean
    MessageSource messageSource() {
        // Chỉ định file messages.properties trong classpath để lấy đa ngôn ngữ
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:messages");
        // Đặt encoding UTF-8 để đọc tiếng Việt, tiếng nước != ngoài anh
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }

    // Lấy message theo code, tham số args và Locale hiện tại trong LanguageContext
    public String getMessage(String code, Object... args) { // = Object[] args
        return messageSource().getMessage(code, args, LanguageContext.getLocale());
    }
}
