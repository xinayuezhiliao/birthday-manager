package com.birthdaymanager.controller;

import com.birthdaymanager.model.Contact;
import com.birthdaymanager.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/contacts")
@CrossOrigin(origins = "*")
public class ContactController {
    
    @Autowired
    private ContactService contactService;

    @GetMapping("/upcoming")
    public ResponseEntity<List<Contact>> getUpcomingBirthdays() {
        return ResponseEntity.ok(contactService.getUpcomingBirthdays());
    }

    @GetMapping("/week")
    public ResponseEntity<List<Contact>> getBirthdaysInNextWeek() {
        return ResponseEntity.ok(contactService.getBirthdaysInNextWeek());
    }

    @PostMapping
    public ResponseEntity<Contact> saveContact(@RequestBody Contact contact) {
        return ResponseEntity.ok(contactService.saveContact(contact));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Contact> updateContact(@PathVariable Long id, @RequestBody Contact contact) {
        contact.setId(id);
        return ResponseEntity.ok(contactService.saveContact(contact));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteContact(@PathVariable Long id) {
        contactService.deleteContact(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Contact> getContact(@PathVariable Long id) {
        return contactService.getContact(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<Contact>> getAllContacts() {
        return ResponseEntity.ok(contactService.getAllContacts());
    }
}
