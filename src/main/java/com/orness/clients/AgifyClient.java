package com.orness.clients;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.jboss.resteasy.annotations.jaxrs.QueryParam;

import javax.enterprise.inject.Default;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("/")
@Default
@RegisterRestClient(configKey="agify-api")
public interface AgifyClient {

    @GET
    AgifyDTO getByName(@QueryParam String name);
}
