package com.example.stayops.service.impl;

import com.example.stayops.dto.GuestCreateDTO;
import com.example.stayops.dto.GuestRegistrationDTO;
import com.example.stayops.dto.GuestResponseDTO;
import com.example.stayops.entity.Guest;
import com.example.stayops.entity.GuestAccount;
import com.example.stayops.repository.GuestAccountRepository;
import com.example.stayops.repository.GuestRepository;
import com.example.stayops.service.GuestService;
import com.example.stayops.util.QRCodeUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GuestServiceImpl implements GuestService {

    private final GuestRepository guestRepository;
    private final GuestAccountRepository guestAccountRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    /**
     * Receptionist creates a guest profile (no password, account inactive).
     */
    @Override
    public GuestResponseDTO createGuest(GuestCreateDTO dto) {
        String guestId = QRCodeUtil.generateGuestId();
        byte[] qrBytes = QRCodeUtil.generateQRCodeImage(guestId, 250, 250);

        Guest guest = Guest.builder()
                .guestId(guestId)
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .email(dto.getEmail())
                .phone(dto.getPhone())
                .nationality(dto.getNationality())
                .identityType(dto.getIdentityType())
                .identityNumber(dto.getIdentityNumber())
                .qrCodeImage(qrBytes)
                .build();

        guest = guestRepository.save(guest);

        // Create placeholder GuestAccount
        GuestAccount account = GuestAccount.builder()
                .guest(guest)
                .email(guest.getEmail())
                .password("") // not yet set
                .activated(false)
                .build();
        guestAccountRepository.save(account);

        return toResponseDTO(guest);
    }

    /**
     * Guest registers themselves from mobile app
     */
    @Override
    public GuestAccount registerGuestFromMobile(GuestRegistrationDTO dto) {
        Guest guest = guestRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new RuntimeException("Guest not found. Contact hotel."));

        GuestAccount account = guestAccountRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new RuntimeException("Guest account not found."));

        if (account.isActivated()) {
            throw new RuntimeException("Account already registered.");
        }

        if (!dto.getPassword().equals(dto.getConfirmPassword())) {
            throw new RuntimeException("Passwords do not match.");
        }

        account.setPassword(passwordEncoder.encode(dto.getPassword()));
        account.setActivated(true);

        return guestAccountRepository.save(account);
    }

    /**
     * Find guest by ID
     */
    @Override
    public GuestResponseDTO getGuestById(String guestId) {
        Guest guest = guestRepository.findById(guestId)
                .orElseThrow(() -> new RuntimeException("Guest not found"));
        return toResponseDTO(guest);
    }

    /**
     * List all guests
     */
    @Override
    public List<GuestResponseDTO> getAllGuests() {
        return guestRepository.findAll()
                .stream().map(this::toResponseDTO).collect(Collectors.toList());
    }

    /**
     * Convert Guest → GuestResponseDTO
     */
    private GuestResponseDTO toResponseDTO(Guest guest) {
        return GuestResponseDTO.builder()
                .guestId(guest.getGuestId())
                .fullName(guest.getFirstName() + " " + guest.getLastName())
                .email(guest.getEmail())
                .phone(guest.getPhone())
                .nationality(guest.getNationality())
                .identityType(guest.getIdentityType())
                .identityNumber(guest.getIdentityNumber())
                .qrCodeBase64(Base64.getEncoder().encodeToString(guest.getQrCodeImage()))
                .build();
    }
}
