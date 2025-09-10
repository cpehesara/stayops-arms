package com.example.stayops.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "guest_accounts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GuestAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "guest_id", nullable = false, unique = true)
    private Guest guest;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    private boolean activated;
}
