package com.javarush.nikolenko.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Cache;


import java.io.Serializable;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "user_info")
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
public class UserInfo implements Serializable {

    @Id
    @Column(name = "user_id")
    private Long userId;
    @Column(name = "address")
    private String address;
    @Column(name = "phone")
    private String phone;

    @OneToOne
    @JoinColumn(name = "user_id")
    @ToString.Exclude
    private User user;
}
