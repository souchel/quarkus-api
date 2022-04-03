package com.orness.web.validation;

import com.orness.data.HeroRepository;
import lombok.RequiredArgsConstructor;

import javax.enterprise.context.ApplicationScoped;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@ApplicationScoped
@RequiredArgsConstructor
public class UniqueMailValidator implements ConstraintValidator<UniqueEmail, String> {

    private final HeroRepository heroRepository;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value == null || value.isBlank() || !heroRepository.existsByMail(value);
    }
}
