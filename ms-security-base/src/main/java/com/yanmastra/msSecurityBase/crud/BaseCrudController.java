package com.yanmastra.msSecurityBase.crud;

import com.yanmastra.msSecurityBase.Log;
import com.yanmastra.msSecurityBase.configuration.HttpException;
import com.yanmastra.msSecurityBase.crud.utils.CrudQueryFilterUtils;
import com.yanmastra.msSecurityBase.entity.BaseEntity;
import com.yanmastra.msSecurityBase.security.UserPrincipal;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Indexed;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;

import java.security.Principal;
import java.util.*;

@Indexed
public abstract class BaseCrudController<Entity extends BaseEntity, Dao> {
    protected abstract PagingJpaRepository<Entity, String> getRepository();
    protected abstract Dao fromEntity(Entity entity);
    protected abstract Entity toEntity(Dao dao);
    protected abstract Entity update(Entity entity, Dao dao);
    protected abstract Class<Entity> getEntityClass();
    private static final Logger logger = LoggerFactory.getLogger(BaseCrudController.class);

    @Autowired
    public EntityManager entityManager;

    protected Sort getSort() {
        return Sort.by("createdAt", "createdAt").descending();
    }

    @GetMapping
    @Transactional
    public Paginate<Dao> getList(
            @RequestParam(value = "page", defaultValue = "1", required = false) Integer page,
            @RequestParam(value = "size", defaultValue = "5", required = false) Integer size,
            @RequestParam(required = false) Map<String, String> allParams
    ) {
        logger.info("page:"+page+", size:"+size);

        if (page <= 0) page = 1;
        if (size <= 0) size = 1;

        Map<String, Object> queryParams = new HashMap<>();
        String countQuery = CrudQueryFilterUtils.createFilterQueryCount(allParams, queryParams, getSearchAbleColumn(), getEntityClass());
        Log.log.info("query:"+countQuery);
        Long count = CrudQueryFilterUtils.executeCount(countQuery, queryParams, entityManager);
        Log.log.info("result:"+count);
        Pageable pageable = Pageable.ofSize(size).withPage(page-1);

        String hql = CrudQueryFilterUtils.createFilterQuery(allParams, queryParams, getSearchAbleColumn(), getSort());
        Log.log.info("query:"+hql);
        Log.log.info("params:"+queryParams);
        TypedQuery<Entity> query = entityManager.createQuery(hql, getEntityClass());
        for (String key: queryParams.keySet()) {
            query.setParameter(key, queryParams.get(key));
        }
        int first = (int) pageable.getOffset();
        if (count <= 0) first = 0;
        query.setFirstResult(first);
        query.setMaxResults(pageable.getPageSize());
        List<Entity> results = query.getResultList();
        return Paginate.from(results.stream().map(this::fromEntity).toList(), pageable, count);
    }

    protected abstract Set<String> getSearchAbleColumn();

    @GetMapping("/{id}")
    @Transactional
    public Dao getOne(
            @PathVariable("id") String id,
            Principal context
    ) {
        logger.info("GET id:"+id);
        Optional<Entity> entityOptional = getRepository().findById(id);
        if (entityOptional.isPresent()) {
            return this.fromEntity(entityOptional.get());
        } else {
            throw new EntityNotFoundException("Entity not found!");
        }
    }

    @PostMapping
    @Transactional
    public ResponseEntity<Map<String, Object>> create(
            @RequestBody Dao dao,
            UserPrincipal context
    ) {
        Entity newEntity = toEntity(dao);
        Optional<Entity> existed = getRepository().findById(newEntity.getId());
        if (existed.isPresent()) {
            throw new HttpException(
                    "Entity " + newEntity.getClass().getSimpleName() + " with id: "+newEntity.getId()+" already exists!",
                    HttpStatus.CONFLICT
            );
        }

        try {
            newEntity.setCreatedBy(context.getUsername());
            Entity savedEntity = getRepository().save(newEntity);
            Map<String, Object> response = Map.of(
                    "success", true,
                    "message", "Saved successfully",
                    "data", this.fromEntity(savedEntity)
            );
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new RestClientException("Failed to save "+(dao == null ? "Unknown" : dao.getClass().getName())+" object due to error:" + e.getMessage(), e);
        }
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<Map<String, Object>> update(
            @PathVariable("id") String id,
            @RequestBody Dao dao,
            UserPrincipal principal
    ) {
        logger.info("Request received!");
        try {
            Entity entity = toEntity(dao);
            entity.setId(id);
            entity.setUpdatedBy(principal.getUsername());
            Optional<Entity> existed = getRepository().findById(id);
            if (existed.isPresent()) {
                 entity = update(existed.get(), dao);
            } else {
                entity = update(entity, dao);
            }
            entity = getRepository().save(entity);

            Map<String, Object> result = Map.of(
                    "success", true,
                    "data", this.fromEntity(entity)
            );

            return ResponseEntity.ok(result);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new RestClientException("Failed to save "+(dao == null ? "Unknown" : dao.getClass().getName())+" object due to error:" + e.getMessage(), e);
        }
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Map<String, Object>> delete(
            @PathVariable("id") String id,
            UserPrincipal principal
    ) {
        Optional<Entity> existed = getRepository().findById(id);
        if (existed.isPresent()) {
            Entity obj = existed.get();
            getRepository().deleteById(id);
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", obj.getClass().getName() + " has been deleted successfully!"
            ));
        } else {
            return ResponseEntity.status(404).body(Map.of(
                    "success", false,
                    "message", "Entity not found!"
            ));
        }
    }

}
