package plus.suja.teach.teachshop.entity;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;

public class PageResponse<T> {
    private Integer totalPage;
    private Integer pageNum;
    private Integer pageSize;
    private List<T> data;

    public PageResponse() {
    }

    public PageResponse(Integer totalPage, Integer pageNum, Integer pageSize, List<T> data) {
        this.totalPage = totalPage;
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.data = data;
    }

    public PageResponse<T> getAllPageResponse(Integer pageNum, Integer pageSize, Function<Pageable, Page<T>> function) {
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize);
        Page<T> page = function.apply(pageable);
        return new PageResponse<>(page.getTotalPages(), pageNum, pageSize, page.toList());
    }

    public PageResponse<T> checkPageAndFunctionApply(Integer pageNum, Integer pageSize, BiFunction<Integer, Integer, PageResponse<T>> function) {
        if (Objects.isNull(pageNum)) {
            pageNum = 1;
        }
        if (Objects.isNull(pageSize)) {
            pageSize = 10;
        }
        return function.apply(pageNum, pageSize);
    }

    public Integer getTotalPage() {
        return totalPage;
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public List<T> getData() {
        return data;
    }
}
