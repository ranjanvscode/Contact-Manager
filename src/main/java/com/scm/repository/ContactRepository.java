package com.scm.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.scm.entities.Contacts;
import com.scm.entities.User;

import java.util.List;


public interface ContactRepository extends JpaRepository<Contacts,String> {

    // public List<Contacts> findByUser(User user);
    public Page<Contacts> findByUser(User user,Pageable pageable);

    @Query("SELECT c FROM Contacts c WHERE c.user.id = :userId")
    public List<Contacts> findByUserId(@Param("userId") String userId);
}
