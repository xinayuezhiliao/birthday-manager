package com.birthdaymanager.model;

import lombok.Data;
import jakarta.persistence.*;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "contacts")
public class Contact {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private LocalDate birthday;
    private String relationship;
    private String avatar;
    private String notes;
    
    @Column(name = "days_until_birthday")
    private Integer daysUntilBirthday;
    
    public void updateDaysUntilBirthday() {
        LocalDate now = LocalDate.now();
        LocalDate nextBirthday = birthday.withYear(now.getYear());
        
        if (nextBirthday.isBefore(now) || nextBirthday.isEqual(now)) {
            nextBirthday = nextBirthday.plusYears(1);
        }
        
        this.daysUntilBirthday = (int) now.until(nextBirthday).getDays();
    }
}
