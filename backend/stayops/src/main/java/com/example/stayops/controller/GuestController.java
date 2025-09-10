package com.example.stayops.controller;

import com.example.stayops.dto.GuestCreateDTO;
import com.example.stayops.dto.GuestRegistrationDTO;
import com.example.stayops.dto.GuestResponseDTO;
import com.example.stayops.entity.GuestAccount;
import com.example.stayops.service.GuestService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/guests")
@RequiredArgsConstructor
public class GuestController {

    private final GuestService guestService;

    @PostMapping("/create")
    public ResponseEntity<GuestResponseDTO> createGuest(@Valid @RequestBody GuestCreateDTO dto) {
        return new ResponseEntity<>(guestService.createGuest(dto), HttpStatus.CREATED);
    }

    @PostMapping("/register")
    public ResponseEntity<GuestAccount> registerGuest(@Valid @RequestBody GuestRegistrationDTO dto) {
        return new ResponseEntity<>(guestService.registerGuestFromMobile(dto), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GuestResponseDTO> getGuest(@PathVariable("id") String guestId) {
        return ResponseEntity.ok(guestService.getGuestById(guestId));
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<GuestResponseDTO>> getAllGuests() {
        return ResponseEntity.ok(guestService.getAllGuests());
    }
}
