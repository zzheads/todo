package com.zzheads.model;//

// todo
// com.teamtreehouse.techdegrees.model created by zzheads on 18.08.2016.
//
public class Todo {
    private int id;
    private String name;
    private boolean completed;
    private boolean edited;

    public Todo() {
    }

    public Todo(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public boolean isEdited() {
        return edited;
    }

    public void setEdited(boolean edited) {
        this.edited = edited;
    }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Todo)) return false;
        Todo todo = (Todo) o;
        return getId() == todo.getId() && isCompleted() == todo.isCompleted() && isEdited() == todo.isEdited() && getName().equals(todo.getName());
    }

    @Override public int hashCode() {
        int result = getId();
        result = 31 * result + getName().hashCode();
        result = 31 * result + (isCompleted() ? 1 : 0);
        result = 31 * result + (isEdited() ? 1 : 0);
        return result;
    }
}
