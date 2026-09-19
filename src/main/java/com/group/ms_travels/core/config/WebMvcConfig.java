package com.group.ms_travels.core.config;

import com.group.ms_travels.core.paging.EnhancedPageableArgumentResolver;
import com.group.ms_travels.core.paging.EnhancedSortArgumentResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        EnhancedSortArgumentResolver sortArgumentResolver = new EnhancedSortArgumentResolver();
        resolvers.add(new EnhancedPageableArgumentResolver(sortArgumentResolver));
        resolvers.add(sortArgumentResolver);
    }
}
