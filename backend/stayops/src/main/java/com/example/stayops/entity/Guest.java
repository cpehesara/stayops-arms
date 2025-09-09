package com.example.stayops.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

@Entity
@Table(name = "guests")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Guest {

    @Id
    @Column(name = "guest_id", updatable = false,nullable = false,unique = true)
    private String guestId;

    @NotBlank
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @NotBlank
    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Email
    @NotBlank
    @Column(unique = true, nullable = false)
    private String email;

    @Pattern(regexp = "^[0-9+\\-]{7,15}$", message = "Invalid phone number")
    @Column(nullable = false)
    private String phone;

    @Column
    private String nationality;

    @Column(name = "identity_type")
    private String identityType;

    @Column(name = "identity_number")
    private String identityNumber;

    @Lob
    private byte[] qrCodeImage;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Instant updatedAt;
}