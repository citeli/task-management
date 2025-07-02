package com.taskmanagement.domain.models;

import com.taskmanagement.domain.enums.Status;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.io.Serializable;

@Entity
public class TaskModel implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;
    public String title;
    public String description;
    public Status status;

    // Construtor padrão necessário para JSON-B ou Jackson
    public TaskModel() {}

    // Construtor completo opcional
    public TaskModel(String title, String description, Status status) {
        this.title = title;
        this.description = description;
        this.status = status;
    }
}
