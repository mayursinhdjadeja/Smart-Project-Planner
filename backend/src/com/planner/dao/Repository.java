package com.planner.dao;

import com.planner.exception.PlannerException;
import java.util.List;

// <T> means that we can use interface for any class
public interface Repository<T> 
{

    // To save new data(INSERT)
    void add(T entity) throws PlannerException;

    // To retrive all data(SELECT *)
    List<T> getAll() throws PlannerException;

    // To retrive specific one ID(SELECT WHERE id = ?)
    T getById(int id) throws PlannerException;

    // To update data(UPDATE)
    void update(T entity) throws PlannerException;

    // To delete data(DELETE)
    void delete(int id) throws PlannerException;
}