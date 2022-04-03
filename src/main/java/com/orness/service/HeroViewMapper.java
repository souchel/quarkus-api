package com.orness.service;

import com.orness.data.HeroEntity;
import com.orness.service.views.HeroView;
import org.mapstruct.Mapper;

@Mapper(componentModel = "cdi")
public interface HeroViewMapper {

    HeroView entityToView(HeroEntity hero);
    HeroEntity viewToEntity(HeroView hero);
}
