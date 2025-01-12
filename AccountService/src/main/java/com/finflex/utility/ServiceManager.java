package com.finflex.utility;

import com.finflex.entitiy.BaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class ServiceManager<T extends BaseEntity,ID> implements IService<T,ID> {
    private final JpaRepository<T,ID> repository;
    public ServiceManager(JpaRepository<T,ID> repository){
        this.repository = repository;
    }

    @Override
    public T save(T entity) {
        entity.setCreatedDate(LocalDateTime.now());
        entity.setUpdatedDate(LocalDateTime.now());
        return repository.save(entity);
    }

    @Override
    public Iterable<T> saveAll(Iterable<T> entityList) {
        entityList.forEach(x->{
            x.setCreatedDate(LocalDateTime.now());
            x.setUpdatedDate(LocalDateTime.now());

        });
        return repository.saveAll(entityList);
    }
    @Override
    public T update(T entity) {
        entity.setUpdatedDate(LocalDateTime.now());
        return repository.save(entity);
    }
    @Override
    public void delete(T entity) {
        entity.setState(false);
        update(entity);
    }
    @Override
    public void deleteById(ID id) {
        repository.deleteById(id);
    }
    @Override
    public Optional<T> findById(ID id) {
        return repository.findById(id);
    }
    @Override
    public List<T> findAll() {
        return repository.findAll();
    }
    @Override
    public List<T> findAllByIds(List<ID> ids) {
        return repository.findAllById(ids);
    }
}

