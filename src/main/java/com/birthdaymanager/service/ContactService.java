package com.birthdaymanager.service;

import com.birthdaymanager.model.Contact;
import com.birthdaymanager.repository.ContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ContactService {
    @Autowired
    private ContactRepository contactRepository;

    public List<Contact> getUpcomingBirthdays() {
        return contactRepository.findUpcomingBirthdays();
    }

    public List<Contact> getBirthdaysInNextWeek() {
        return contactRepository.findBirthdaysInNextWeek();
    }

    public Contact saveContact(Contact contact) {
        contact.updateDaysUntilBirthday();
        return contactRepository.save(contact);
    }

    public void deleteContact(Long id) {
        contactRepository.deleteById(id);
    }

    public Optional<Contact> getContact(Long id) {
        return contactRepository.findById(id);
    }

    public List<Contact> getAllContacts() {
        return contactRepository.findAll();
    }

    @Scheduled(cron = "0 0 0 * * *") // Run at midnight every day
    public void updateDaysUntilBirthday() {
        List<Contact> contacts = contactRepository.findAll();
        for (Contact contact : contacts) {
            contact.updateDaysUntilBirthday();
            contactRepository.save(contact);
        }
    }
}
