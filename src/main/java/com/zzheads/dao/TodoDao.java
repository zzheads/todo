package com.zzheads.dao;//

import com.zzheads.exc.DaoException;
import com.zzheads.model.Todo;

import java.util.List;

//
// com.teamtreehouse.techdegrees.dao created by zzheads on 18.08.2016.
//
public interface TodoDao {
    void update(Todo todo) throws DaoException;
    void delete(Todo todo) throws DaoException;
    void delete(int id) throws DaoException;
    void add(Todo todo) throws DaoException;
    List<Todo> findAll() throws DaoException;
    Todo findById(int id) throws DaoException;
}
