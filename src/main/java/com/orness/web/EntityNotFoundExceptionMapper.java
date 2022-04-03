package com.orness.web;

import com.orness.web.responses.ErrorResponse;
import org.jboss.logging.Logger;

import javax.inject.Inject;
import javax.persistence.EntityNotFoundException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.UUID;

@Provider
public class EntityNotFoundExceptionMapper implements ExceptionMapper<EntityNotFoundException> {

    @Inject
    Logger log;

    public Response toResponse(EntityNotFoundException x) {
        UUID uuid = UUID.randomUUID();
        String message = x.getMessage() + " StacktraceId: " + uuid;
        ErrorResponse response = new ErrorResponse(message, uuid, "Not Found");
        log.error(message, x);
        return Response.status(404).entity(response).build();
    }
}
