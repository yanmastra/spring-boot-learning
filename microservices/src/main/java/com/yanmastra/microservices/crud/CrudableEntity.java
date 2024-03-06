package com.yanmastra.microservices.crud;

import com.yanmastra.microservices.entity.BaseEntity;

import java.io.Serializable;

public abstract class CrudableEntity extends BaseEntity implements Serializable {

    public abstract String getId();
    public abstract void setId(String id);
}
