package com.jo2k.garagify.common.exception;

public class ParkingNotFoundException extends RuntimeException {
    public ParkingNotFoundException(String message) {
        super(message);
    }
}
