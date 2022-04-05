package com.orness.data;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.quarkus.panache.common.Sort;

import javax.enterprise.context.ApplicationScoped;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class HeroRepository implements PanacheRepositoryBase<HeroEntity, UUID> {

    public Optional<HeroEntity> findByMail(String mail) {
        return find("mail", mail).firstResultOptional();
    }

    public boolean existsByMail(String mail) {
        return count("mail", mail) != 0L;
    }

    public Page<HeroEntity> findAll(int pageIndex, int pageSize) {
        return new Page<>(count(), pageSize, pageIndex,
                findAll(Sort.by("firstname", Sort.Direction.Ascending).and("lastname", Sort.Direction.Ascending)).page(pageIndex, pageSize).list());
    }
}
