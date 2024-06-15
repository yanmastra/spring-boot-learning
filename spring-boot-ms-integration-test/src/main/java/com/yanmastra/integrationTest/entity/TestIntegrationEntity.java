package com.yanmastra.integrationTest.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.yanmastra.msSecurityBase.entity.BaseEntity;
import jakarta.persistence.*;

import java.math.BigDecimal;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity(name = "tb_test_integration")
public class TestIntegrationEntity extends BaseEntity {
    @JsonProperty("id")
    @Id
    private String id;
    @JsonProperty("name")
    @Column(name = "name", length = 36)
    private String name;

    @JsonProperty("price")
    @Column(name = "price", precision = 14, scale = 2)
    private BigDecimal price;

    @JsonProperty("category")
    @Enumerated(EnumType.STRING)
    @Column(name = "category")
    private CategoryEntity categoryEntity;

    public TestIntegrationEntity() {
    }

    @JsonIgnore
    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @JsonIgnore
    @Override
    public String toString() {
        return getClass().getName() + "{" +
                "id="+id+", "+
                "name="+name+", " +
                "price="+price+"}";
    }

    @JsonIgnore
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @JsonIgnore
    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @JsonIgnore
    public CategoryEntity getCategoryEntity() {
        return categoryEntity;
    }

    public void setCategoryEntity(CategoryEntity categoryEntity) {
        this.categoryEntity = categoryEntity;
    }
}
