package com.yanmastra.msSecurityBase.crud;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@NoArgsConstructor
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
}
