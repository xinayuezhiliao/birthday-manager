package com.birthdaymanager.service;

import com.birthdaymanager.exception.ResourceNotFoundException;
import com.birthdaymanager.model.Birthday;
import com.birthdaymanager.repository.BirthdayRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class BirthdayService {
    
    private static final Logger logger = LoggerFactory.getLogger(BirthdayService.class);
    
    @Autowired
    private BirthdayRepository birthdayRepository;
    
    @Transactional(readOnly = true)
    public List<Birthday> getAllBirthdays() {
        try {
            logger.debug("Fetching all birthdays");
            return birthdayRepository.findAll();
        } catch (Exception e) {
            logger.error("Error fetching all birthdays", e);
            throw new RuntimeException("Failed to fetch birthdays", e);
        }
    }
    
    @Transactional(readOnly = true)
    public Optional<Birthday> getBirthdayById(Long id) {
        try {
            logger.debug("Fetching birthday with id: {}", id);
            Optional<Birthday> birthday = birthdayRepository.findById(id);
            if (birthday.isEmpty()) {
                throw new ResourceNotFoundException("Birthday not found with id: " + id);
            }
            return birthday;
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Error fetching birthday with id: {}", id, e);
            throw new RuntimeException("Failed to fetch birthday", e);
        }
    }
    
    @Transactional
    public Birthday saveBirthday(Birthday birthday) {
        try {
            validateBirthday(birthday);
            logger.debug("Saving birthday for: {}", birthday.getName());
            return birthdayRepository.save(birthday);
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Error saving birthday", e);
            throw new RuntimeException("Failed to save birthday", e);
        }
    }
    
    private void validateBirthday(Birthday birthday) {
        if (birthday.getName() == null || birthday.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }
        if (birthday.getBirthDate() == null) {
            throw new IllegalArgumentException("Birth date cannot be null");
        }
        if (birthday.getBirthDate().isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Birth date cannot be in the future");
        }
        if (birthday.getRelationship() == null || birthday.getRelationship().trim().isEmpty()) {
            throw new IllegalArgumentException("Relationship cannot be empty");
        }
    }
    
    @Transactional
    public void deleteBirthday(Long id) {
        try {
            logger.debug("Deleting birthday with id: {}", id);
            if (!birthdayRepository.existsById(id)) {
                throw new ResourceNotFoundException("Birthday not found with id: " + id);
            }
            birthdayRepository.deleteById(id);
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Error deleting birthday with id: {}", id, e);
            throw new RuntimeException("Failed to delete birthday", e);
        }
    }
    
    @Transactional(readOnly = true)
    public List<Birthday> searchByName(String name) {
        try {
            if (name == null || name.trim().isEmpty()) {
                throw new IllegalArgumentException("Search name cannot be empty");
            }
            logger.debug("Searching birthdays by name: {}", name);
            return birthdayRepository.findByNameContainingIgnoreCase(name);
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Error searching birthdays by name: {}", name, e);
            throw new RuntimeException("Failed to search birthdays by name", e);
        }
    }
    
    @Transactional(readOnly = true)
    public List<Birthday> getUpcomingBirthdays(int days) {
        try {
            if (days <= 0) {
                throw new IllegalArgumentException("Days must be positive");
            }
            logger.debug("Fetching upcoming birthdays for next {} days", days);
            return birthdayRepository.findUpcomingBirthdays(days);
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Error fetching upcoming birthdays", e);
            throw new RuntimeException("Failed to fetch upcoming birthdays", e);
        }
    }
    
    @Transactional(readOnly = true)
    public List<Birthday> searchByRelationship(String relationship) {
        try {
            if (relationship == null || relationship.trim().isEmpty()) {
                throw new IllegalArgumentException("Search relationship cannot be empty");
            }
            logger.debug("Searching birthdays by relationship: {}", relationship);
            return birthdayRepository.findByRelationshipContainingIgnoreCase(relationship);
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Error searching birthdays by relationship: {}", relationship, e);
            throw new RuntimeException("Failed to search birthdays by relationship", e);
        }
    }
    
    @Transactional
    public Birthday updateBirthday(Long id, Birthday birthdayDetails) {
        try {
            logger.debug("Updating birthday with id: {}", id);
            Optional<Birthday> birthday = birthdayRepository.findById(id);
            if (birthday.isEmpty()) {
                throw new ResourceNotFoundException("Birthday not found with id: " + id);
            }
            validateBirthday(birthdayDetails);
            Birthday existingBirthday = birthday.get();
            existingBirthday.setName(birthdayDetails.getName());
            existingBirthday.setBirthDate(birthdayDetails.getBirthDate());
            existingBirthday.setRelationship(birthdayDetails.getRelationship());
            existingBirthday.setNotes(birthdayDetails.getNotes());
            return birthdayRepository.save(existingBirthday);
        } catch (ResourceNotFoundException | IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Error updating birthday with id: {}", id, e);
            throw new RuntimeException("Failed to update birthday", e);
        }
    }
}
