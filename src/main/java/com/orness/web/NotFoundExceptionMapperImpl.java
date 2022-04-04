package com.orness.web;

import com.orness.web.responses.ErrorResponse;

import javax.persistence.EntityNotFoundException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.UUID;

@Provider
public class NotFoundExceptionMapperImpl implements ExceptionMapper<EntityNotFoundException> {

    public Response toResponse(EntityNotFoundException x) {
        ErrorResponse response = new ErrorResponse(x.getMessage(), UUID.randomUUID(), "Not Found");
        return Response.status(404).entity(response).build();
    }
}
