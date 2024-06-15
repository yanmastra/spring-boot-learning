package com.yanmastra.msSecurityBase.crud;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Paginate<E> {
    private List<E> data;
    @JsonProperty("current_page")
    private int currentPage;
    private int size;
    @JsonProperty("isFirst")
    private boolean isFirst;
    @JsonProperty("is_last")
    private boolean isLast;
    @JsonProperty("total_data")
    private long totalData;
    @JsonProperty("total_page")
    private int totalPage;

    public Paginate() {
    }

    public static <E> Paginate<E> of(Page<E> page) {
        Paginate<E> paginate = new Paginate<>();
        paginate.setData(page.getContent());
        paginate.setSize(page.getPageable().getPageSize());
        paginate.setFirst(page.isFirst());
        paginate.setLast(page.isLast());
        paginate.setTotalData(page.getTotalElements());
        paginate.setTotalPage(page.getTotalPages());
        return paginate;
    }

    public static <E> Paginate<E> from(List<E> data, Pageable pageable, long total) {
        Paginate<E> paginate = new Paginate<>();
        paginate.setData(data);
        paginate.setCurrentPage(pageable.getPageNumber());
        paginate.setSize(pageable.getPageSize());
        paginate.setFirst(!pageable.hasPrevious());
        paginate.setTotalData(total);
        int totalPage = (int) (total / pageable.getPageSize());
        if (total % pageable.getPageSize() > 0) {
            totalPage += 1;
        }
        paginate.setTotalPage(totalPage);
        paginate.setLast(paginate.getCurrentPage() == totalPage);
        return paginate;
    }

    public List<E> getData() {
        return data;
    }

    public void setData(List<E> data) {
        this.data = data;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public boolean isFirst() {
        return isFirst;
    }

    public void setFirst(boolean first) {
        isFirst = first;
    }

    public boolean isLast() {
        return isLast;
    }

    public void setLast(boolean last) {
        isLast = last;
    }

    public long getTotalData() {
        return totalData;
    }

    public void setTotalData(long totalData) {
        this.totalData = totalData;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }
}
