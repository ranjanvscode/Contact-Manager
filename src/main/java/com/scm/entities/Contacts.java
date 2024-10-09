package com.scm.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Contacts {

    @Id
    private String id;
    private String name;
    private String email;
    @Column(length = 1000)
    private String profilePic;
    private String phoneNo;
    private boolean favorite;
    private String  cloudinaryImagePublicId;

    @ManyToOne
    private User user;

}
