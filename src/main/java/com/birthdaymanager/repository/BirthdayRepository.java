package com.birthdaymanager.repository;

import com.birthdaymanager.model.Birthday;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.time.LocalDate;
import java.util.List;

public interface BirthdayRepository extends JpaRepository<Birthday, Long> {
    
    // Find birthdays by name (case-insensitive)
    List<Birthday> findByNameContainingIgnoreCase(String name);
    
    // Find upcoming birthdays in the next N days
    @Query("SELECT b FROM Birthday b WHERE FUNCTION('DATEDIFF', 'DAY', CURRENT_DATE, " +
           "CASE " +
           "  WHEN FUNCTION('DATEDIFF', 'DAY', CURRENT_DATE, " +
           "       FUNCTION('DATEADD', 'YEAR', " +
           "         FUNCTION('DATEDIFF', 'YEAR', b.birthDate, CURRENT_DATE), " +
           "         b.birthDate)) < 0 " +
           "  THEN FUNCTION('DATEADD', 'YEAR', " +
           "         FUNCTION('DATEDIFF', 'YEAR', b.birthDate, CURRENT_DATE) + 1, " +
           "         b.birthDate) " +
           "  ELSE FUNCTION('DATEADD', 'YEAR', " +
           "         FUNCTION('DATEDIFF', 'YEAR', b.birthDate, CURRENT_DATE), " +
           "         b.birthDate) " +
           "END) <= ?1 " +
           "ORDER BY FUNCTION('DATEDIFF', 'DAY', CURRENT_DATE, " +
           "CASE " +
           "  WHEN FUNCTION('DATEDIFF', 'DAY', CURRENT_DATE, " +
           "       FUNCTION('DATEADD', 'YEAR', " +
           "         FUNCTION('DATEDIFF', 'YEAR', b.birthDate, CURRENT_DATE), " +
           "         b.birthDate)) < 0 " +
           "  THEN FUNCTION('DATEADD', 'YEAR', " +
           "         FUNCTION('DATEDIFF', 'YEAR', b.birthDate, CURRENT_DATE) + 1, " +
           "         b.birthDate) " +
           "  ELSE FUNCTION('DATEADD', 'YEAR', " +
           "         FUNCTION('DATEDIFF', 'YEAR', b.birthDate, CURRENT_DATE), " +
           "         b.birthDate) " +
           "END)")
    List<Birthday> findUpcomingBirthdays(int days);
    
    // Find birthdays by relationship
    List<Birthday> findByRelationshipContainingIgnoreCase(String relationship);
}
