package com.orness.service;

import com.orness.data.HeroEntity;
import com.orness.data.Page;
import com.orness.service.views.HeroView;
import com.orness.service.views.PageView;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;

@Mapper(componentModel = "cdi")
public interface HeroViewMapper {

    HeroView entityToView(HeroEntity hero);
    PageView<HeroView> entitiesToViews(Page<HeroEntity> hero);
    @InheritInverseConfiguration(name = "entityToView")
    HeroEntity viewToEntity(HeroView hero);
}
