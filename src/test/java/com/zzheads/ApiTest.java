package com.zzheads;

import com.google.gson.Gson;
import com.zzheads.dao.Sql2oTodoDao;
import com.zzheads.exc.ApiError;
import com.zzheads.model.Todo;
import com.zzheads.testing.ApiClient;
import com.zzheads.testing.ApiResponse;
import org.junit.*;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import spark.Spark;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

//
// course-reviews
// com.zzheads.courses created by zzheads on 16.08.2016.
//
public class ApiTest {

    private static final String PORT = "4568";
    private static final String TEST_DATASOURCE = "jdbc:h2:mem:testing";
    private Connection conn;
    private ApiClient client;
    private Gson gson;
    private Sql2oTodoDao todoDao;

    @BeforeClass
    public static void startServer() {
        String[] args = {PORT, TEST_DATASOURCE};
        App.main(args);
    }

    @AfterClass
    public static void stopServer() {
        Spark.stop();
    }

    @Before public void setUp() throws Exception {
        Sql2o sql2o = new Sql2o(TEST_DATASOURCE + ";INIT=RUNSCRIPT from 'classpath:db/init.sql'", "", "");
        todoDao = new Sql2oTodoDao(sql2o);
        conn = sql2o.open();
        client = new ApiClient("http://localhost:" + PORT);
        gson = new Gson();
    }

    @After public void tearDown() throws Exception {
        conn.close();
    }

    @Test
    public void addingTodosReturnsCreatedStatus() throws Exception {
        Map<String, Object> values = new HashMap<>();
        values.put("name", "Test task");
        values.put("completed", false);
        values.put("edited", false);

        ApiResponse res = client.request("POST", "/todos", gson.toJson(values));

        assertEquals(201, res.getStatus());
    }

    @Test
    public void todosCanBeAccessed() throws Exception {
        Todo todo = newTestTodo();
        todoDao.add(todo);
        todoDao.add(todo);
        todoDao.add(todo);

        ApiResponse res = client.request("GET", "/todos");
        Todo[] retrieved = gson.fromJson(res.getBody(), Todo[].class);

        assertEquals(3, retrieved.length);
    }

    @Test
    public void updateMissingTodoReturnNotFoundStatus() throws Exception {
        Map<String, Object> values = new HashMap<>();
        values.put("name", "Test task");
        values.put("completed", false);
        values.put("edited", false);

        ApiResponse res = client.request("PUT", "/todos/42", gson.toJson(values));
        assertEquals(404, res.getStatus());
    }

    @Test
    public void updateTodo() throws Exception {
        Map<String, Object> values = new HashMap<>();
        values.put("name", "Test task");
        values.put("completed", false);
        values.put("edited", false);

        ApiResponse res = client.request("POST", "/todos", gson.toJson(values));
        assertEquals(201, res.getStatus());

        res = client.request("GET", "/todos");
        Todo[] retrieved = gson.fromJson(res.getBody(), Todo[].class);
        int id = retrieved[0].getId();
        assertEquals("Test task", retrieved[0].getName());
        assertEquals(false, retrieved[0].isCompleted());
        assertEquals(false, retrieved[0].isEdited());

        Map<String, Object> valuesEdited = new HashMap<>();
        valuesEdited.put("id", String.valueOf(id));
        valuesEdited.put("name", "Edited test task");
        valuesEdited.put("completed", true);
        valuesEdited.put("edited", true);

        res = client.request("PUT", "/todos/"+id, gson.toJson(valuesEdited));
        assertEquals(201, res.getStatus());

        res = client.request("GET", "/todos");
        retrieved = gson.fromJson(res.getBody(), Todo[].class);
        assertEquals("Edited test task", retrieved[0].getName());
        assertEquals(true, retrieved[0].isCompleted());
        assertEquals(true, retrieved[0].isEdited());

        assertEquals(1, retrieved.length);
    }

    @Test
    public void deleteTodo() throws Exception {
        Map<String, Object> values = new HashMap<>();
        values.put("name", "Test task");
        values.put("completed", false);
        values.put("edited", false);

        Map<String, Object> valuesEdited = new HashMap<>();
        valuesEdited.put("name", "Edited test task");
        valuesEdited.put("completed", true);
        valuesEdited.put("edited", true);

        ApiResponse res = client.request("POST", "/todos", gson.toJson(values));
        assertEquals(201, res.getStatus());

        res = client.request("POST", "/todos", gson.toJson(valuesEdited));
        assertEquals(201, res.getStatus());

        res = client.request("GET", "/todos");
        Todo[] retrieved = gson.fromJson(res.getBody(), Todo[].class);
        assertEquals(2, retrieved.length);

        int id1 = retrieved[0].getId();
        assertEquals("Test task", retrieved[0].getName());
        assertEquals(false, retrieved[0].isCompleted());
        assertEquals(false, retrieved[0].isEdited());

        int id2 = retrieved[1].getId();
        assertEquals("Edited test task", retrieved[1].getName());
        assertEquals(true, retrieved[1].isCompleted());
        assertEquals(true, retrieved[1].isEdited());

        res = client.request("DELETE", "/todos/"+id1, gson.toJson(values));
        assertEquals(201, res.getStatus());
        res = client.request("GET", "/todos");
        retrieved = gson.fromJson(res.getBody(), Todo[].class);
        assertEquals(1, retrieved.length);
    }

    @Test
    public void deleteMissingTodo() throws Exception {
        Map<String, Object> values = new HashMap<>();
        values.put("name", "Test task");
        values.put("completed", false);
        values.put("edited", false);

        ApiResponse res = client.request("DELETE", "/todos/34", gson.toJson(values));
        assertEquals(404, res.getStatus());
    }

    private Todo newTestTodo() {
        return new Todo("Some test task");
    }

}
