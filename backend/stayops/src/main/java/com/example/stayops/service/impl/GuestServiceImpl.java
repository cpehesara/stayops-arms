package com.example.stayops.service.impl;

import com.example.stayops.dto.GuestCreateDTO;
import com.example.stayops.dto.GuestResponseDTO;
import com.example.stayops.entity.Guest;
import com.example.stayops.repository.GuestRepository;
import com.example.stayops.service.GuestService;
import com.example.stayops.util.QRCodeUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GuestServiceImpl implements GuestService {

    private final GuestRepository guestRepository;

    @Override
    public GuestResponseDTO createGuest(GuestCreateDTO dto){
        String guestId = QRCodeUtil.generateGuestId();
        byte[] qrBytes = QRCodeUtil.generateQRCodeImage(guestId,250,250);

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

        guestRepository.save(guest);

        return toResponseDTO(guest);
    }

    @Override
    public GuestResponseDTO getGuestById(String guestId){
        Guest guest = guestRepository.findById(guestId)
                .orElseThrow(() -> new RuntimeException("Guest not found"));
        return toResponseDTO(guest);
    }

    @Override
    public List<GuestResponseDTO> getAllGuests(){
        return guestRepository.findAll()
                .stream().map(this::toResponseDTO).collect(Collectors.toList());
    }

    private GuestResponseDTO toResponseDTO(Guest guest){
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
