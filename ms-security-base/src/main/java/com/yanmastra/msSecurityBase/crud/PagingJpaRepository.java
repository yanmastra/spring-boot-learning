package com.yanmastra.msSecurityBase.crud;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;

@NoRepositoryBean
public interface PagingJpaRepository<Entity, Id> extends JpaRepository<Entity, Id>, PagingAndSortingRepository<Entity, Id> {
}
