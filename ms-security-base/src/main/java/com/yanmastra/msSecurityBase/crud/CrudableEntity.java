package com.yanmastra.msSecurityBase.crud;

import com.yanmastra.msSecurityBase.entity.BaseEntity;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
public abstract class CrudableEntity extends BaseEntity implements Serializable {

    public abstract String getId();
    public abstract void setId(String id);
}
