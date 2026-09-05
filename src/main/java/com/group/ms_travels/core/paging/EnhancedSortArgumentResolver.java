package com.group.ms_travels.core.paging;

import com.group.ms_travels.core.paging.annos.AllowedSortProperty;
import org.springframework.core.MethodParameter;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.SortHandlerMethodArgumentResolver;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class EnhancedSortArgumentResolver extends SortHandlerMethodArgumentResolver {

    @Override
    public @NonNull Sort resolveArgument(MethodParameter parameter,
                                @Nullable ModelAndViewContainer mavContainer,
                                NativeWebRequest webRequest,
                                @Nullable WebDataBinderFactory binderFactory) {
        Sort sort = super.resolveArgument(parameter, mavContainer, webRequest, binderFactory);
        validateSortProperties(parameter, sort);
        return sort;
    }

    private void validateSortProperties(MethodParameter parameter, Sort sort) {
        AllowedSortProperty allowedSortProperty = parameter.getParameterAnnotation(AllowedSortProperty.class);
        if (Objects.isNull(allowedSortProperty) || sort.isUnsorted()) {
            return;
        }

        List<String> allowed = Arrays.asList(allowedSortProperty.props());
        sort.forEach(order -> {
            if (!allowed.contains(order.getProperty())) {
                throw new IllegalArgumentException("Sort property not allowed: " + order.getProperty() + ", allowed=" + allowed);
            }
        });
    }


}
