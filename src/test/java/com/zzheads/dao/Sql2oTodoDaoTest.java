package com.zzheads.dao;//

import com.zzheads.exc.DaoException;
import com.zzheads.model.Todo;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class Sql2oTodoDaoTest {

    private Sql2oTodoDao dao;
    private Connection conn;

    @Before public void setUp() throws Exception {
        String connectionString = "jdbc:h2:mem:testing;INIT=RUNSCRIPT from 'classpath:db/init.sql'";
        Sql2o sql2o = new Sql2o(connectionString, "", "");
        dao = new Sql2oTodoDao(sql2o);
        // Keep connection opened through entire test so that it isn't wiped out
        conn = sql2o.open();
    }

    @After
    public void tearDown() throws Exception {
        conn.close();
    }

    @Test
    public void addingTodoSetsId() throws Exception {
        Todo todo = newTestTodo();
        int originalTodoId = todo.getId();
        dao.add(todo);
        assertNotEquals(originalTodoId, todo.getId());
    }

    @Test
    public void addedTodosAreReturnedFromFindAll () throws Exception {
        Todo todo = newTestTodo();
        dao.add(todo);
        assertEquals(1, dao.findAll().size());
    }

    @Test public void noTodosReturnsEmptyList() throws Exception {
        assertEquals(0, dao.findAll().size());
    }

    @Test public void existingTodosCanBeFoundById() throws Exception {
        Todo todo = newTestTodo();
        dao.add(todo);
        Todo foundTodo = dao.findById(todo.getId());
        assertEquals(todo, foundTodo);
    }

    @Test public void updatedTodoDoesntSaveNewOne() throws Exception {
        Todo todo = newTestTodo();
        dao.add(todo);
        assertEquals(1, dao.findAll().size());
        todo.setName("New name");
        dao.update(todo);
        assertEquals(1, dao.findAll().size());
    }

    @Test public void deleteTodoDecreaseSize() throws Exception {
        Todo todo1 = newTestTodo();
        dao.add(todo1);
        Todo todo2 = newTestTodo();
        dao.add(todo2);
        assertEquals(2, dao.findAll().size());
        dao.delete(todo1);
        assertEquals(1, dao.findAll().size());
        dao.delete(todo2.getId());
        assertEquals(0, dao.findAll().size());
    }

    @Test public void cantFindDaoSituationToThrowDaoException() throws Exception {
    }

    private Todo newTestTodo() {
        return new Todo("Some test task");
    }
}

