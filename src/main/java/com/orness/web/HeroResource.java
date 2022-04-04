package com.orness.web;

import com.orness.service.IHeroService;
import com.orness.web.requests.HeroCreationRequest;
import com.orness.web.responses.HeroResponse;
import lombok.RequiredArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

@Path("/heroes")
@RequiredArgsConstructor
public class HeroResource {

    private final HeroMapper heroMapper;
    private final IHeroService heroService;


    @GET
    @Path("/{mail}")
    public HeroResponse getHero(@PathParam("mail") @NotBlank String mail) {
        return heroMapper.viewToResponse(heroService.getHeroView(mail));
    }

    @POST
    public void createHero(@Valid HeroCreationRequest hero) {
        heroService.registerHero(heroMapper.requestToView(hero));
    }
}