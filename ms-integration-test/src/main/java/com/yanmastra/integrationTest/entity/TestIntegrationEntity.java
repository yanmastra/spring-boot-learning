package com.yanmastra.integrationTest.entity;

import com.yanmastra.msSecurityBase.crud.CrudableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@Entity(name = "tb_test_integration")
@Data
@EqualsAndHashCode(callSuper = true)
public class TestIntegrationEntity extends CrudableEntity {
    @Id
    private String id;
    @Column(name = "name", length = 36)
    private String name;

    @Column(name = "price", precision = 14, scale = 2)
    private BigDecimal price;

    public TestIntegrationEntity() {
    }

    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return getClass().getName() + "{" +
                "id="+id+", "+
                "name="+name+", " +
                "price="+price+"}";
    }
}
