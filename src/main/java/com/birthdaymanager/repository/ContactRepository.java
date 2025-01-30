package com.birthdaymanager.repository;

import com.birthdaymanager.model.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface ContactRepository extends JpaRepository<Contact, Long> {
    @Query("SELECT c FROM Contact c ORDER BY c.daysUntilBirthday ASC")
    List<Contact> findUpcomingBirthdays();
    
    @Query("SELECT c FROM Contact c WHERE c.daysUntilBirthday <= 7 ORDER BY c.daysUntilBirthday ASC")
    List<Contact> findBirthdaysInNextWeek();
}
