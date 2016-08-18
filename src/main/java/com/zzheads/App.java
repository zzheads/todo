package com.zzheads;

import com.google.gson.Gson;
import com.zzheads.dao.Sql2oTodoDao;
import com.zzheads.dao.TodoDao;
import com.zzheads.exc.ApiError;
import com.zzheads.model.Todo;
import org.sql2o.Sql2o;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static spark.Spark.*;
import static spark.Spark.after;

public class App {

//    DONE: When the app first starts it will attempt to fetch all Todos in the system. Handle the request and return all the Todos.
//    DONE: Look at the browser tool to see what is being requested and how and create the appropriate route
//    DONE: When a Todo is created and the save link is clicked, it will make a request to the server. Handle the request by creating a Todo and setting the proper status code.
//    DONE: Look at the browser tool to see what is being requested and how and create the appropriate route
//    DONE: When a previously saved Todo is updated and the save link is clicked, it will make a request to the server. Handle the request by updating the existing Todo.
//    DONE: Look at the browser tool to see what is being requested and how and create the appropriate route. HINT: You might not’ve used this one before, but you can figure it out if you PUT your mind to it!
//    DONE: When a previously saved Todo is deleted and the save link is clicked, it will make a request to the server. Handle the deletion and return a blank response and the proper status code.
//
//    DONE: Add unit tests to test your model and dao implementation
//    DONE: Add functional testing to prove the API is working as expected

    public static void main(String[] args) {

        String datasource = "jdbc:h2:E:/Projects/ToDo/data/todos.db";

        if (args.length > 0) {
            if (args.length != 2) {
                System.out.printf("java Api <port> <datasource>%n");
                System.exit(0);
            }
            port(Integer.parseInt(args[0]));
            datasource = args[1];
        }

        Sql2o sql2o = new Sql2o (String.format("%s;INIT=RUNSCRIPT from 'classpath:db/init.sql'", datasource), "", "");
        TodoDao todoDao = new Sql2oTodoDao(sql2o);
        Gson gson = new Gson();

        staticFileLocation("/public");

        get("/todos", "application/json", (req, res) -> todoDao.findAll(), gson::toJson);

        post("/todos", "application/json", (req, res) -> {
            Todo todo = gson.fromJson(req.body(), Todo.class);
            todoDao.add(todo);
            res.status(201);
            return todo;
        }, gson::toJson);

        put("/todos/:id", "application/json", (req, res) -> {
            int id = Integer.parseInt(req.params("id"));
            Todo todo = gson.fromJson(req.body(), Todo.class);
            if (todo == null || todoDao.findById(id) == null) throw new ApiError(404, "Could not find course with id " + id);
            todoDao.update(todo);
            res.status(201);
            return todo;
        }, gson::toJson);

        delete("/todos/:id", "application/json", (req, res) -> {
            int id = Integer.parseInt(req.params("id"));
            if (todoDao.findById(id) == null) throw new ApiError(404, "Could not find course with id " + id);
            todoDao.delete(id);
            res.status(201);
            return null;
        }, gson::toJson);

        exception(ApiError.class, (exc, req, res) -> {
            ApiError err = (ApiError) exc;
            Map<String, Object> jsonMap = new HashMap<>();
            jsonMap.put("status", err.getStatus());
            jsonMap.put("errorMessage", err.getMessage());
            res.type("application/json");
            res.status(err.getStatus());
            res.body(gson.toJson(jsonMap));
        });

        after((req, res) -> {
            res.type("application/json");
        });

    }

}
