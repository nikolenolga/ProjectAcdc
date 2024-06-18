package com.javarush.lessonsForDelete.lesson15;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Inheritance(strategy = InheritanceType.JOINED)
public class Seller extends BaseParent {
    @Column(name = "profit")
    double profit;

}
