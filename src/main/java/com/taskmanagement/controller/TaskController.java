package com.taskmanagement.controller;

import com.taskmanagement.domain.enums.Status;
import com.taskmanagement.domain.models.TaskModel;
import com.taskmanagement.dto.TaskRequestDTO;
import com.taskmanagement.service.TaskService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import java.util.Optional;

@Path("/tasks")
public class TaskController {

    @Inject
    TaskService taskService;

    @POST
    public Response createTask(@Valid TaskRequestDTO dto) {
        TaskModel created = taskService.create(new TaskModel(dto.title, dto.description, dto.status));
        return Response.status(Response.Status.CREATED).entity(created).build();
    }

    @GET
    @RolesAllowed("admin")
    @Produces(MediaType.APPLICATION_JSON)
    public Response listAllTasks() {
        List<TaskModel> tasks = taskService.listAll();
        return Response.ok(tasks).build();
    }

    @GET
    @Path("/completed")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("admin")
    public Response listCompletedTasks() {
        List<TaskModel> completedTasks = taskService.findByStatus(Status.Completed);
        return Response.ok(completedTasks).build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTaskById(@PathParam("id") Long id) {
        Optional<TaskModel> task = taskService.findById(id);

        return task.isPresent()
                ? Response.ok(task.get()).build()
                : Response.status(Response.Status.NOT_FOUND).build();
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateTask(TaskModel updatedTask) {
        Optional<TaskModel> result = taskService.update(updatedTask);

        return result.isPresent()
                ? Response.ok(result.get()).build()
                : Response.status(Response.Status.NOT_FOUND).build();
    }

}
