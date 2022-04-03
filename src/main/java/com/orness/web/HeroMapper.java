package com.orness.web;

import com.orness.service.views.HeroView;
import com.orness.web.requests.HeroCreationRequest;
import com.orness.web.responses.HeroResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "cdi")
public interface HeroMapper {

    HeroResponse viewToResponse(HeroView hero);
    HeroView requestToView(HeroCreationRequest hero);
}
