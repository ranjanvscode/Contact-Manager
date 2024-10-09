package com.scm.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.scm.entities.Contacts;
import com.scm.entities.User;
import com.scm.forms.ContactForm;
import com.scm.helper.AppConstant;
import com.scm.helper.GetEmail;
import com.scm.helper.Message;
import com.scm.helper.MessageType;
import com.scm.service.ContactService;
import com.scm.service.ImageService;
import com.scm.service.UserService;

import jakarta.validation.Valid;
import jakarta.servlet.http.HttpSession;


@Controller
@RequestMapping("/user/contacts")
public class ContactController {

    @Autowired
    ContactService contactService;

    @Autowired
    UserService userService;

    @Autowired
    ImageService imageService;


    // @GetMapping("/add")
    @GetMapping("/add")
    public String addContact(Model model){

        ContactForm contactForm = new ContactForm();
        model.addAttribute("contactForm", contactForm);

        return "user/add";
    }


    @PostMapping("/regContact")
    public String registerContact(@Valid @ModelAttribute ContactForm contactForm,BindingResult result ,Authentication authentication,HttpSession session) {
        
        if (result.hasErrors()) {
            Message msg = Message.builder().content("Please correct the following errors").type(MessageType.red).build();
            session.setAttribute("message", msg);
            return "user/add";
        }

        String userEmail = GetEmail.getEmailOfLoggedInUser(authentication);
        User user = userService.getUserByEmail(userEmail);

        Contacts contact = new Contacts();

        contact.setEmail(contactForm.getEmail());
        contact.setFavorite(contactForm.isFavorite());
        contact.setName(contactForm.getName());
        contact.setPhoneNo(contactForm.getPhoneNo());
        contact.setUser(user);


        if (contactForm.getProfilePic()!=null && !contactForm.getProfilePic().isEmpty()) {

            String fileName = UUID.randomUUID().toString();// Cloudinary image Public Id
            String fileUrl = imageService.uploadimage(contactForm.getProfilePic(),fileName);
            contact.setProfilePic(fileUrl);
            contact.setCloudinaryImagePublicId(fileName);
        }

        contactService.saveContact(contact);

        Message msg = Message.builder().content("Contact added Successful").type(MessageType.green).build();
        session.setAttribute("message", msg);
        
        return "redirect:add";
    }

    //Contacts View
    
    @GetMapping
    public String viewContacts(
                                @RequestParam(value="page", defaultValue="0") int page,
                                @RequestParam(value="size" ,defaultValue=AppConstant.PAGE_SIZE+"") int size,
                                @RequestParam(value="sortBy", defaultValue="name") String sortBy,
                                @RequestParam(value="direction", defaultValue="asc") String direction,
                                Model model,Authentication authentication){

        String userEmail = GetEmail.getEmailOfLoggedInUser(authentication);
        Page<Contacts> contactList = contactService.getByUser(userService.getUserByEmail(userEmail),page,size,sortBy,direction);
        model.addAttribute("contacts", contactList);
        model.addAttribute("pageSize", AppConstant.PAGE_SIZE);
        return "user/contacts";
    }

    @GetMapping("/delete/{contactId}/{publicId}")
    public String deleteContact(@PathVariable("contactId") String contactId,
                                @PathVariable("publicId") String publicId, HttpSession session){

        contactService.deleteContact(contactId);
        if(publicId!=null) imageService.deleteImage(publicId);

        Message msg = Message.builder().content("Contact deleted successfully").type(MessageType.green).build();
        session.setAttribute("message", msg);

        return "redirect:/user/contacts";
    }

    @GetMapping("/view/{id}")
    public String view(@PathVariable String id,Model model){

        Contacts contact = contactService.getById(id);
        model.addAttribute("contactData", contact);
        model.addAttribute("contactId", id);
        return "user/view";
    }

    @PostMapping("/update/{contactId}")
    public String updateContact(@ModelAttribute ContactForm contactForm,@PathVariable String contactId, Model model,HttpSession session){

        Contacts contact = contactService.getById(contactId);

        contact.setName(contactForm.getName());
        contact.setEmail(contactForm.getEmail());
        contact.setFavorite(contactForm.isFavorite());
        contact.setPhoneNo(contactForm.getPhoneNo());
        
        if (contactForm.getProfilePic()!=null && !contactForm.getProfilePic().isEmpty()) {

            String publicId = contact.getCloudinaryImagePublicId();
            if(publicId!=null) imageService.deleteImage(publicId);
            
            String fileName = UUID.randomUUID().toString();// Cloudinary image Public Id
            String fileUrl = imageService.uploadimage(contactForm.getProfilePic(),fileName);
            contact.setProfilePic(fileUrl);
            contact.setCloudinaryImagePublicId(fileName);
        }

        Contacts updatedContact = contactService.updateContact(contact);
        model.addAttribute("contactData", updatedContact);

        Message msg = Message.builder().content("Contact updated").type(MessageType.green).build();
        session.setAttribute("message", msg);

        return "user/view";
    }
}
