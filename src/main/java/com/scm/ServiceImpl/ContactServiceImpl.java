package com.scm.ServiceImpl;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.scm.entities.Contacts;
import com.scm.entities.User;
import com.scm.helper.ResourceNotFound;
import com.scm.repository.ContactRepository;
import com.scm.service.ContactService;

@Service
public class ContactServiceImpl implements ContactService{

    @Autowired
    ContactRepository contactRepo;
    
    @Override
    public Contacts saveContact(Contacts contact) {

        String id = UUID.randomUUID().toString();
        contact.setId(id);
        return contactRepo.save(contact);
    }

    @Override
    public Contacts updateContact(Contacts contact) {
        
        return contactRepo.save(contact);
    }

    @Override
    public void deleteContact(String id) {
        
        Contacts contact = contactRepo.findById(id).orElseThrow(()-> new ResourceNotFound("Contact not found with given ID"));
        contactRepo.delete(contact);
    }

    @Override
    public List<Contacts> getAllContact() {
        
        return contactRepo.findAll();
    }

    @Override
    public Contacts getById(String id) {
        
        return contactRepo.findById(id).orElseThrow(()-> new ResourceNotFound("Contact not found with given ID"));
    }

    @Override
    public List<Contacts> searchContacts(String name, String email, String phoneNo) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'searchContacts'");
    }

    @Override
    public List<Contacts> getByUserId(String userId) {
        
        return contactRepo.findByUserId(userId);
    }

    @Override
    public Page<Contacts> getByUser(User user,int page, int size, String sortBy,String direction) {

        Sort sort = direction.equals("asc")?Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        var pageable = PageRequest.of(page, size, sort);
    
        return contactRepo.findByUser(user,pageable);
    }

}
