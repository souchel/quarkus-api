package com.orness.web;

import com.orness.service.views.HeroView;
import com.orness.service.views.PageView;
import com.orness.web.requests.HeroCreationRequest;
import com.orness.web.responses.HeroResponse;
import com.orness.web.responses.PageResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "cdi")
public interface HeroMapper {

    HeroResponse viewToResponse(HeroView hero);
    PageResponse<HeroResponse> viewsToResponses(PageView<HeroView> heroes);
    HeroView requestToView(HeroCreationRequest hero);
}
