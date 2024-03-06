package com.yanmastra.microservices.crud;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.ListPagingAndSortingRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface PagingJpaRepository<Entity, Id> extends JpaRepository<Entity, Id>, ListPagingAndSortingRepository<Entity, Id> {
}
