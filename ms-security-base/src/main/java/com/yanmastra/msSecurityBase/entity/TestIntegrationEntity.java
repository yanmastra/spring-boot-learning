package com.yanmastra.msSecurityBase.entity;

import com.yanmastra.msSecurityBase.crud.CrudableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.math.BigDecimal;

@Entity(name = "tb_test_integration")
public class TestIntegrationEntity extends CrudableEntity {
    @Id
    private String id;
    @Column(name = "name", length = 36)
    private String name;

    @Column(name = "price", precision = 14, scale = 2)
    private BigDecimal price;

    public TestIntegrationEntity() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
