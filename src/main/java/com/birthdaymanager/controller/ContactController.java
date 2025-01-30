package com.birthdaymanager.controller;

import com.birthdaymanager.model.Contact;
import com.birthdaymanager.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:5173") // Vue.js default dev server port
public class ContactController {
    @Autowired
    private ContactService contactService;

    @GetMapping("/contacts/upcoming")
    public ResponseEntity<List<Contact>> getUpcomingBirthdays() {
        return ResponseEntity.ok(contactService.getUpcomingBirthdays());
    }

    @GetMapping("/contacts/week")
    public ResponseEntity<List<Contact>> getBirthdaysInNextWeek() {
        return ResponseEntity.ok(contactService.getBirthdaysInNextWeek());
    }

    @PostMapping("/contacts")
    public ResponseEntity<Contact> saveContact(@RequestBody Contact contact) {
        return ResponseEntity.ok(contactService.saveContact(contact));
    }
}
