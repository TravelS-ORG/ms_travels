package com.group.ms_travels.core.paging;

import com.group.ms_travels.core.paging.annos.PageableParam;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.springframework.core.MethodParameter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableHandlerMethodArgumentResolverSupport;
import org.springframework.data.web.PageableArgumentResolver;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;

@RequiredArgsConstructor
public class EnhancedPageableArgumentResolver extends PageableHandlerMethodArgumentResolverSupport implements PageableArgumentResolver {

    private static final String SIZE_PARAM = "page_size";
    private static final int DEFAULT_PAGE_SIZE = 20;

    private final EnhancedSortArgumentResolver sortArgumentResolver;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(PageableParam.class);
    }

    @Override
    public @NonNull Pageable resolveArgument(MethodParameter parameter, @Nullable ModelAndViewContainer mavContainer, NativeWebRequest webRequest, @Nullable WebDataBinderFactory binderFactory) {
        String page = webRequest.getParameter(getParameterNameToUse(getPageParameterName(), parameter));
        String size = webRequest.getParameter(getParameterNameToUse(getSizeParameterName(), parameter));

        int maxPageSize = getMaxPageSize(parameter);
        int pageNumber = (page != null && !page.isBlank()) ? Integer.parseInt(page) - 1 : 0;
        int pageSize = (size != null && !size.isBlank()) ? Integer.parseInt(size) : DEFAULT_PAGE_SIZE;

        if (pageNumber < 0) {
            throw new IllegalArgumentException("Page number must be existed!");
        }

        if (pageSize <=0 || pageSize > maxPageSize) {
            throw new IllegalArgumentException("Page size must be in [1, " + maxPageSize + "]");
        }

        Sort sort = sortArgumentResolver.resolveArgument(parameter, mavContainer, webRequest, binderFactory);

        return sort.isSorted()
                ? PageRequest.of(pageNumber, pageSize, sort)
                : PageRequest.of(pageNumber, pageSize);
    }

    private int getMaxPageSize(MethodParameter parameter) {
        PageableParam pageableParam = parameter.getParameterAnnotation(PageableParam.class);
        return pageableParam != null ? pageableParam.maxPageSize() : 100;
    }
}
