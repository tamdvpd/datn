package com.fashionstore.fashionstore.service;

import java.util.Collections;

import org.springframework.stereotype.Service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;

@Service
public class GoogleOAuthService {

    private final GoogleIdTokenVerifier verifier;

    public GoogleOAuthService() {
        verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), JacksonFactory.getDefaultInstance())
                .setAudience(Collections.singletonList("632537849203-qbc1m7utqg8t3t4c8td32ujlk7lbb9bl.apps.googleusercontent.com"))
                .build();
    }

    public GoogleIdToken verifyToken(String idTokenString) {
        try {
            return verifier.verify(idTokenString);
        } catch (Exception e) {
            return null;
        }
    }
}