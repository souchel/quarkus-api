package com.orness.web;

import com.orness.web.responses.ErrorResponse;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.UUID;

@Provider
public class ServerExceptionMapper implements ExceptionMapper<WebApplicationException> {
    @Override
    public Response toResponse(WebApplicationException exception) {
        ErrorResponse response = new ErrorResponse(exception.getMessage(), UUID.randomUUID(), "Internal Error");
        return Response.status(500).entity(response).build();
    }
}
