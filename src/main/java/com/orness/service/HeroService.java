package com.orness.service;

import com.orness.data.HeroRepository;
import com.orness.service.views.HeroView;
import lombok.AllArgsConstructor;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;

@ApplicationScoped
@AllArgsConstructor
public class HeroService implements IHeroService{

    private final HeroRepository heroRepository;
    private final HeroViewMapper heroViewMapper;

    @Override
    public HeroView getHeroView(String mail) {
        return heroViewMapper.entityToView(heroRepository.findByMail(mail).orElseThrow(
                () -> new EntityNotFoundException("No hero found with mail " + mail)
        ));
    }

    @Override
    @Transactional
    public void registerHero(HeroView heroView) {
        heroRepository.persistAndFlush(heroViewMapper.viewToEntity(heroView));
    }
}
