package com.jo2k.garagify.lendoffer.controller;

import com.jo2k.api.LendOfferControllerApi;
import com.jo2k.dto.LendOfferDTO;
import com.jo2k.dto.LendOfferListDTO;
import com.jo2k.dto.LendOfferPostForm;
import com.jo2k.dto.LendOfferPutForm;
import com.jo2k.garagify.lendoffer.mapper.LendOfferMapper;
import com.jo2k.garagify.lendoffer.persistence.model.LendOffer;
import com.jo2k.garagify.lendoffer.api.LendOfferService;
import com.jo2k.garagify.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.OffsetDateTime;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class LendController implements LendOfferControllerApi {

    private final LendOfferService lendOfferService;
    private final UserService userService;
    private final LendOfferMapper lendOfferMapper;

    @Override
    public ResponseEntity<LendOfferDTO> createLendOffer(@RequestBody LendOfferPostForm lendOfferPOST) {
        LendOffer lendOffer = lendOfferService.createLendOffer(
                UUID.fromString(lendOfferPOST.getSpotId()),
                userService.getCurrentUser().getId(),
                lendOfferPOST.getStartDate(),
                lendOfferPOST.getEndDate()
        );

        LendOfferDTO createdOffer = lendOfferMapper.toDto(lendOffer);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{lend_offer_id}")
                .buildAndExpand(createdOffer.getId())
                .toUri();

        return ResponseEntity.created(location).body(createdOffer);
    }

    @Override
    public ResponseEntity<LendOfferListDTO> getAllLendOffers(
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size,
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime start_date,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime end_date,
            @RequestParam(required = false) String owner_id) {

        PageRequest pageable = PageRequest.of(page, size);
        Page<LendOffer> offersPage = lendOfferService.getAllLendOffers(
                start_date,
                end_date,
                owner_id != null ? UUID.fromString(owner_id) : null,
                pageable
        );

        Page<LendOfferDTO> offers = offersPage.map(lendOfferMapper::toDto);

        LendOfferListDTO response = new LendOfferListDTO(
                offers.getContent(),
                offers.getTotalElements(),
                offers.getTotalPages(),
                offers.getNumber(),
                offers.getSize()
        );

        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<LendOfferDTO> getLendOfferById(@PathVariable("lend_offer_id") String lendOfferId) {
        LendOffer lendOffer = lendOfferService.getLendOfferById(UUID.fromString(lendOfferId));
        LendOfferDTO dto = lendOfferMapper.toDto(lendOffer);
        return ResponseEntity.ok(dto);
    }

    @Override
    public ResponseEntity<LendOfferDTO> updateLendOfferById(
            @PathVariable("lend_offer_id") String lendOfferId,
            @RequestBody LendOfferPutForm lendOfferPUT) {

        LendOffer updatedLendOffer = lendOfferService.updateLendOffer(
                UUID.fromString(lendOfferId),
                lendOfferPUT.getStartDate(),
                lendOfferPUT.getEndDate()
        );
        LendOfferDTO dto = lendOfferMapper.toDto(updatedLendOffer);
        return ResponseEntity.ok(dto);
    }


    @Override
    public ResponseEntity<Void> deleteLendOfferById(@PathVariable("lend_offer_id") String lendOfferId) {
        lendOfferService.deleteLendOffer(UUID.fromString(lendOfferId));
        return ResponseEntity.ok().build();
    }
}