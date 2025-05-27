package com.jo2k.garagify.borrow.service;

import com.jo2k.dto.BorrowGET;
import com.jo2k.dto.BorrowPOST;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.OffsetDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Profile("dev")
public class BorrowServiceMock implements IBorrowService {
    @PostConstruct
    public void init() {
        System.out.println("BorrowServiceDev loaded!");
    }
    @Override
    public List<BorrowGET> createBorrowsIfNotExistsAndAvailable(List<BorrowPOST> borrowPOSTs) {
        return List.of(
                new BorrowGET()
                        .id("mocked-id")
                        .spotId("mocked-spot-id")
                        .startDate(OffsetDateTime.parse("2023-01-01T10:00:00Z"))
                        .endDate(OffsetDateTime.parse("2023-01-01T12:00:00Z"))
                        .borrowerId("mocked-user-id"),
                new BorrowGET()
                        .id("mocked-id")
                        .spotId("mocked-spot-id")
                        .startDate(OffsetDateTime.parse("2023-01-01T10:00:00Z"))
                        .endDate(OffsetDateTime.parse("2023-01-01T12:00:00Z"))
                        .borrowerId("mocked-user-id"),
                new BorrowGET()
                        .id("mocked-id")
                        .spotId("mocked-spot-id")
                        .startDate(OffsetDateTime.parse("2023-01-01T10:00:00Z"))
                        .endDate(OffsetDateTime.parse("2023-01-01T12:00:00Z"))
                        .borrowerId("mocked-user-id"),
                new BorrowGET()
                        .id("mocked-id")
                        .spotId("mocked-spot-id")
                        .startDate(OffsetDateTime.parse("2023-01-01T10:00:00Z"))
                        .endDate(OffsetDateTime.parse("2023-01-01T12:00:00Z"))
                        .borrowerId("mocked-user-id"),
                new BorrowGET()
                        .id("mocked-id")
                        .spotId("mocked-spot-id")
                        .startDate(OffsetDateTime.parse("2023-01-01T10:00:00Z"))
                        .endDate(OffsetDateTime.parse("2023-01-01T12:00:00Z"))
                        .borrowerId("mocked-user-id"),
                new BorrowGET()
                        .id("mocked-id")
                        .spotId("mocked-spot-id")
                        .startDate(OffsetDateTime.parse("2023-01-01T10:00:00Z"))
                        .endDate(OffsetDateTime.parse("2023-01-01T12:00:00Z"))
                        .borrowerId("mocked-user-id"),
                new BorrowGET()
                        .id("mocked-id")
                        .spotId("mocked-spot-id")
                        .startDate(OffsetDateTime.parse("2023-01-01T10:00:00Z"))
                        .endDate(OffsetDateTime.parse("2023-01-01T12:00:00Z"))
                        .borrowerId("mocked-user-id"),
                new BorrowGET()
                        .id("mocked-id")
                        .spotId("mocked-spot-id")
                        .startDate(OffsetDateTime.parse("2023-01-01T10:00:00Z"))
                        .endDate(OffsetDateTime.parse("2023-01-01T12:00:00Z"))
                        .borrowerId("mocked-user-id")
        );
    }
}
