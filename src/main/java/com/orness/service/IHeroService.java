package com.orness.service;

import com.orness.service.views.HeroView;
import com.orness.service.views.PageView;

public interface IHeroService {
    HeroView getHeroView(String mail);
    PageView<HeroView> getHeroes(int pageIndex, int pageSize);
    void registerHero(HeroView heroView);
}
