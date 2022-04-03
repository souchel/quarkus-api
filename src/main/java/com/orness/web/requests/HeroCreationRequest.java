package com.orness.web.requests;

import com.orness.web.validation.UniqueEmail;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
public class HeroCreationRequest {
    @NotBlank(message = "{blank.firstname.message}")
    @Size(min = 1, max = 200, message = "{size.message}")
    @Pattern(regexp = "^\\p{L}*$", message = "{letters.message}")
    private String firstname;
    @NotBlank(message = "{blank.lastname.message}")
    @Size(min = 1, max = 200, message = "{size.message}")
    @Pattern(regexp = "^\\p{L}*$", message = "{letters.message}")
    private String lastname;
    @NotBlank
    @Email(message = "{email.invalid.message}")
    @UniqueEmail(message = "{duplicate.mail.message}")
    private String mail;
    @Max(value = 150, message = "{age.lower.message}")
    @Min(value = 0, message = "{age.greater.message}")
    private Integer age;
}
