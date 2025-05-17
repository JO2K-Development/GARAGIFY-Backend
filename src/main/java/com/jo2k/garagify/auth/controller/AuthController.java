package com.jo2k.garagify.auth.controller;

import com.jo2k.api.AuthControllerApi;
import com.jo2k.dto.TokenDto;
import com.jo2k.dto.TokenVerificationForm;
import com.jo2k.garagify.auth.service.AuthService;
import com.jo2k.garagify.auth.service.GoogleTokenVerifierService;
import com.jo2k.garagify.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RequiredArgsConstructor
@RestController
class AuthController implements AuthControllerApi {

    private final GoogleTokenVerifierService tokenVerifierService;
    private final AuthService authService;
    private final UserService userService;


    @Override
    public ResponseEntity<TokenDto> authenticateWithGoogle(@RequestBody TokenVerificationForm tokenVerificationForm) {
        // TODO replace with real verification for deployment with real google client id

        /*   var payload = tokenVerifierService.verifyToken(idToken);
         *   String email = payload.getEmail();
         */

        /* For now any token with "email" claim will go through, for example:
         * this token: eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyLCJlbWFpbCI6Im15QGVtYWlsIn0.qqei_kFbDs8ALnBaOwqXIDg7n-F7sfp4FT_yXmDYLy0
         */
        String email = tokenVerifierService.verifyTokenMock(tokenVerificationForm.getToken());
        TokenDto tokenDto = authService.generateToken(userService.findOrCreateByEmail(email).toString());
        return ResponseEntity.ok(tokenDto);
    }

    @Override
    public ResponseEntity<TokenDto> refreshToken(@RequestBody TokenVerificationForm tokenVerificationForm) {
        TokenDto tokenDto = authService.getRefreshToken(tokenVerificationForm.getToken());
        return ResponseEntity.ok(tokenDto);
    }
}