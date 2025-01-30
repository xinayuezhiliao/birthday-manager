package com.birthdaymanager.controller;

import com.birthdaymanager.model.Birthday;
import com.birthdaymanager.service.BirthdayService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/birthdays")
@CrossOrigin(origins = "http://localhost:5173")
public class BirthdayController {
    
    private static final Logger logger = LoggerFactory.getLogger(BirthdayController.class);
    
    @Autowired
    private BirthdayService birthdayService;
    
    @GetMapping
    public ResponseEntity<List<Birthday>> getAllBirthdays() {
        try {
            logger.info("Fetching all birthdays");
            List<Birthday> birthdays = birthdayService.getAllBirthdays();
            return ResponseEntity.ok(birthdays);
        } catch (Exception e) {
            logger.error("Error fetching all birthdays", e);
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Birthday> getBirthdayById(@PathVariable Long id) {
        try {
            logger.info("Fetching birthday with id: {}", id);
            return birthdayService.getBirthdayById(id)
                    .map(ResponseEntity::ok)
                    .orElseGet(() -> {
                        logger.warn("Birthday not found with id: {}", id);
                        return ResponseEntity.notFound().build();
                    });
        } catch (Exception e) {
            logger.error("Error fetching birthday with id: {}", id, e);
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @PostMapping
    public ResponseEntity<Birthday> createBirthday(@RequestBody Birthday birthday) {
        try {
            logger.info("Creating new birthday for: {}", birthday.getName());
            Birthday savedBirthday = birthdayService.saveBirthday(birthday);
            logger.info("Successfully created birthday with id: {}", savedBirthday.getId());
            return ResponseEntity.ok(savedBirthday);
        } catch (Exception e) {
            logger.error("Error creating birthday", e);
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Birthday> updateBirthday(@PathVariable Long id, @RequestBody Birthday birthdayDetails) {
        try {
            logger.info("Updating birthday with id: {}", id);
            Birthday updatedBirthday = birthdayService.updateBirthday(id, birthdayDetails);
            if (updatedBirthday == null) {
                logger.warn("Birthday not found with id: {}", id);
                return ResponseEntity.notFound().build();
            }
            logger.info("Successfully updated birthday with id: {}", id);
            return ResponseEntity.ok(updatedBirthday);
        } catch (Exception e) {
            logger.error("Error updating birthday with id: {}", id, e);
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBirthday(@PathVariable Long id) {
        try {
            logger.info("Deleting birthday with id: {}", id);
            birthdayService.deleteBirthday(id);
            logger.info("Successfully deleted birthday with id: {}", id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            logger.error("Error deleting birthday with id: {}", id, e);
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @GetMapping("/search")
    public ResponseEntity<List<Birthday>> searchBirthdays(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String relationship) {
        try {
            List<Birthday> results;
            if (name != null && !name.isEmpty()) {
                logger.info("Searching birthdays by name: {}", name);
                results = birthdayService.searchByName(name);
            } else if (relationship != null && !relationship.isEmpty()) {
                logger.info("Searching birthdays by relationship: {}", relationship);
                results = birthdayService.searchByRelationship(relationship);
            } else {
                logger.info("No search criteria provided, returning all birthdays");
                results = birthdayService.getAllBirthdays();
            }
            return ResponseEntity.ok(results);
        } catch (Exception e) {
            logger.error("Error searching birthdays", e);
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @GetMapping("/upcoming")
    public ResponseEntity<List<Birthday>> getUpcomingBirthdays(@RequestParam(defaultValue = "30") int days) {
        try {
            logger.info("Fetching upcoming birthdays for next {} days", days);
            List<Birthday> upcomingBirthdays = birthdayService.getUpcomingBirthdays(days);
            return ResponseEntity.ok(upcomingBirthdays);
        } catch (Exception e) {
            logger.error("Error fetching upcoming birthdays", e);
            return ResponseEntity.internalServerError().build();
        }
    }
}
