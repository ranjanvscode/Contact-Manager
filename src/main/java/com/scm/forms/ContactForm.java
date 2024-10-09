package com.scm.forms;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ContactForm {

    @NotBlank(message = "Name is required")
    @Size(min=3,message = "Name should be min 3 character")
    private String name;

    @Email(message = "Email should be valid") // @Pattern(regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$", message = "Invalid email")
    private String email;

    private MultipartFile profilePic;

    @NotBlank(message = "Contact No. is required")
    @Pattern(regexp = "^[0-9]{10}$", message = "Invalid format")
    private String phoneNo;

    private boolean favorite;

}
