package com.scm.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.scm.entities.Contacts;
import com.scm.entities.User;

public interface ContactService {

    Contacts saveContact(Contacts contact);  

    Contacts updateContact(Contacts contact);

    void deleteContact(String id);

    List<Contacts> getAllContact();

    Contacts getById(String id);

    List<Contacts> searchContacts(String name,String email,String phoneNo);

    List<Contacts> getByUserId(String userId);

    Page<Contacts> getByUser(User user,int page, int size, String sortBy,String direction);

    // Contacts getByPhoneNo(String number);
} 
