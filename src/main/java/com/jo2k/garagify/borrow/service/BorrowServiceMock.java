package com.jo2k.garagify.borrow.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Profile("dev")
public class BorrowServiceMock implements IBorrowService {

}
