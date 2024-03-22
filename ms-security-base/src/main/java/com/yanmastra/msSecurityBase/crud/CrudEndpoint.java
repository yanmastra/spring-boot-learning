package com.yanmastra.msSecurityBase.crud;

import com.yanmastra.msSecurityBase.configuration.HttpException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Indexed;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;

import java.security.Principal;
import java.util.Map;
import java.util.Optional;

@Indexed
public abstract class CrudEndpoint<Entity extends CrudableEntity, Dao> {
    protected abstract PagingJpaRepository<Entity, String> getRepository();
    protected abstract Dao fromEntity(Entity entity);
    protected abstract Entity toEntity(Dao dao);
    protected abstract Entity update(Entity entity, Dao dao);
    private static final Logger logger = LoggerFactory.getLogger(CrudEndpoint.class);

    @GetMapping
    @Transactional
    public Page<Dao> getList(
            @RequestParam(value = "page", defaultValue = "1", required = false) Integer page,
            @RequestParam(value = "size", defaultValue = "5", required = false) Integer size,
            Principal principal
    ) {
        Pageable pageable = Pageable.ofSize(size).withPage(page);
        return getRepository().findAll(pageable).map(this::fromEntity);
    }

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
            Principal context
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

    @PutMapping("{id}")
    @Transactional
    public ResponseEntity<Map<String, Object>> update(
            @PathVariable("id") String id,
            Dao dao,
            Principal principal
    ) {
        try {
            Entity entity = toEntity(dao);
            entity.setId(id);
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

    @DeleteMapping("{id}")
    @Transactional
    public ResponseEntity<Map<String, Object>> delete(
            @PathVariable("id") String id,
            Principal principal
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
