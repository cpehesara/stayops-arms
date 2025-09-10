package com.example.stayops.service;

import com.example.stayops.dto.GuestCreateDTO;
import com.example.stayops.dto.GuestRegistrationDTO;
import com.example.stayops.dto.GuestResponseDTO;
import com.example.stayops.entity.GuestAccount;

import java.util.List;

public interface GuestService {
    GuestResponseDTO createGuest(GuestCreateDTO dto);
    GuestResponseDTO getGuestById(String guestId);
    List<GuestResponseDTO> getAllGuests();

    // New method for mobile registration
    GuestAccount registerGuestFromMobile(GuestRegistrationDTO dto);
}
