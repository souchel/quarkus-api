package com.orness.data;

import lombok.Data;
import org.hibernate.annotations.Type;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.UUID;

@Data
@Entity
public class HeroEntity {
    @Id @GeneratedValue
    @Type(type = "uuid-char")
    private UUID id;
    private String firstname;
    private String lastname;
    private int age;
    private String mail;
}
