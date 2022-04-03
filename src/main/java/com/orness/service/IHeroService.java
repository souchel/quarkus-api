package com.orness.service;

import com.orness.service.views.HeroView;

public interface IHeroService {
    HeroView getHeroView(String mail);
    void registerHero(HeroView heroView);
}
