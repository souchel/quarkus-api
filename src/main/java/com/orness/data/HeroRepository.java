package com.orness.data;

import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;
import java.util.Optional;

@ApplicationScoped
public class HeroRepository implements PanacheRepository<HeroEntity> {

    public Optional<HeroEntity> findByMail(String mail) {
        return find("mail", mail).firstResultOptional();
    }

    public boolean existsByMail(String mail) {
        return count("mail", mail) != 0L;
    }
}
