package com.jo2k.garagify.parking.api;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ParkingInfoService<Action> {
    Page<Action> getForCurrentUser(Pageable pageable);
}