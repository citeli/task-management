package com.taskmanagement.exceptions;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class JsonExceptionMapper implements ExceptionMapper<Exception> {

    @Override
    public Response toResponse(Exception exception) {
        Throwable root = findRootCause(exception);

        if (root instanceof InvalidFormatException formatException) {
            String field = formatException.getPath().getFirst().getFieldName();
            String message = "Invalid value for field '" + field + "'. ";

            if ("status".equals(field)) {
                message += "Accepted values are: ToDo, InProgress, Completed.";
            } else {
                message += "Check the request format.";
            }

            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(message)
                    .type(MediaType.TEXT_PLAIN)
                    .build();
        }

        return Response.status(Response.Status.BAD_REQUEST)
                .entity("Invalid request. " + exception.getMessage())
                .type(MediaType.TEXT_PLAIN)
                .build();
    }

    private Throwable findRootCause(Throwable throwable) {
        while (throwable.getCause() != null && throwable != throwable.getCause()) {
            throwable = throwable.getCause();
        }
        return throwable;
    }
}
