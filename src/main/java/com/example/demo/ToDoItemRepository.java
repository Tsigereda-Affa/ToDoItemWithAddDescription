package com.example.demo;

import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;

public interface ToDoItemRepository extends CrudRepository<ToDoItem, Long> {
    //this will save the list of items
    ArrayList<ToDoItem>findByUsername(String username);

}
