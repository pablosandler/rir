package com.mycompany.rir.daos;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;

public abstract class AbstractDao<T> implements EditableBaseDao<T> {

    @PersistenceContext(unitName = "entityManager")
    private EntityManager entityManager;

    private Class<T> type;

    public AbstractDao(Class<T> type){
        this.type=type;
    }

    public EntityManager getEntityManager(){
        return entityManager;
    }


    public T save(T entity){
        entityManager.persist(entity);
        return entity;
    }

    public T update(T entity){
        return entityManager.merge(entity);
    }


    public List<T> getAll(){
        CriteriaQuery cq = this.entityManager.getCriteriaBuilder().createQuery();
        cq.select(cq.from(type));
        return this.entityManager.createQuery(cq).getResultList();
    }

    public T getById(long id){
        return (T)entityManager.find(type,id);
    }

}
