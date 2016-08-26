package com.zzheads.dao;

import com.zzheads.exc.DaoException;
import com.zzheads.model.Todo;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import org.sql2o.Sql2oException;

import java.util.List;

//
// com.zzheads.dao created by zzheads on 18.08.2016.
//
public class Sql2oTodoDao implements TodoDao {

    private final Sql2o sql2o;

    public Sql2oTodoDao(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    @Override public void update(Todo todo) throws DaoException {
        String sql = String.format("UPDATE todos SET name='%s', completed=%s, edited=%s WHERE id=%d",todo.getName(), todo.isCompleted(), todo.isEdited(), todo.getId());
        try (Connection con = sql2o.open()) {
            con.createQuery(sql)
                .executeUpdate()
                .getKey();
        } catch (Sql2oException ex) {
            throw new DaoException(ex, "Problem updating todo.");
        }
    }

    @Override public void add(Todo todo) throws DaoException {
        String sql = String.format("INSERT INTO todos(name, completed, edited) VALUES ('%s', %s, %s)", todo.getName(), todo.isCompleted(), todo.isEdited());
        try (Connection con = sql2o.open()) {
            int id = (int) con.createQuery(sql)
                .executeUpdate()
                .getKey();
            todo.setId(id);
        } catch (Sql2oException ex) {
            throw new DaoException(ex, "Problem adding todo.");
        }
    }

    @Override public void delete(Todo todo) throws DaoException {
        delete(todo.getId());
    }

    @Override public void delete(int id) throws DaoException {
        String sql = String.format("DELETE FROM todos WHERE id=%d", id);
        try (Connection con = sql2o.open()) {
            con.createQuery(sql)
                .executeUpdate();
        } catch (Sql2oException ex) {
            throw new DaoException(ex, "Problem deleting todo.");
        }
    }

    @Override public List<Todo> findAll() throws DaoException {
        try (Connection con = sql2o.open()) {
            return con.createQuery("SELECT * FROM todos")
                .executeAndFetch(Todo.class);
        } catch (Sql2oException ex) {
            throw new DaoException(ex, "Problem findAll todo.");
        }
    }

    @Override public Todo findById(int id) throws DaoException {
        String sql = String.format("SELECT * FROM todos WHERE id=%d", id);
        try (Connection con = sql2o.open()) {
            return con.createQuery(sql)
                .executeAndFetchFirst(Todo.class);
        } catch (Sql2oException ex) {
            throw new DaoException(ex, "Problem findById todo.");
        }
    }
}
