package com.orness.web.requests;

//import com.orness.spring_boot_app.web.validation.UniqueEmail;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
public class HeroCreationRequest {
    @NotBlank(message = "firstname should not be left blank")
    @Size(min = 1, max = 200, message = "firstname should not be longer than 200 characters")
    @Pattern(regexp = "^[a-zA-Z\\-À-ÿ' ]*$", message = "There should be no special characters in the firstname")//\p{L}
    private String firstname;
    @NotBlank(message = "lastname should not be left blank")
    @Size(min = 1, max = 200, message = "lastname should not be longer than 200 characters")
    @Pattern(regexp = "^[a-zA-Z\\-À-ÿ' ]*$", message = "There should be no special characters in the lastname")
    private String lastname;
    @NotBlank
    @Email(message = "Email should be valid")
    //@UniqueEmail
    private String mail;
    @Max(value = 150, message = "age should not be more than 150")
    @Min(value = 0, message = "age should not be less than 0")
    private Integer age;
}
