package com.orness.service;

import com.orness.clients.AgifyClient;
import com.orness.clients.AgifyDTO;
import com.orness.data.HeroRepository;
import com.orness.service.views.HeroView;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;

@ApplicationScoped
public class HeroService implements IHeroService{

    private final HeroRepository heroRepository;
    private final HeroViewMapper heroViewMapper;
    private final AgifyClient agifyClient;

    public HeroService(HeroRepository heroRepository, HeroViewMapper heroViewMapper,
                       @RestClient AgifyClient agifyClient) {
        this.heroRepository = heroRepository;
        this.heroViewMapper = heroViewMapper;
        this.agifyClient = agifyClient;
    }

    @Override
    public HeroView getHeroView(String mail) {
        return heroViewMapper.entityToView(heroRepository.findByMail(mail).orElseThrow(
                () -> new EntityNotFoundException("No hero found with mail " + mail)
        ));
    }

    @Override
    @Transactional
    public void registerHero(HeroView heroView) {
        if (heroView.getAge() == null) {
            AgifyDTO dto = agifyClient.getByName(StringUtils.lowerCase(StringUtils.stripAccents(heroView.getFirstname())));
            heroView.setAge(dto.age());
        }
        heroRepository.persistAndFlush(heroViewMapper.viewToEntity(heroView));
    }
}
