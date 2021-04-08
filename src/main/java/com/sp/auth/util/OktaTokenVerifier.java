package com.sp.auth.util;

import com.okta.jwt.AccessTokenVerifier;
import com.okta.jwt.Jwt;
import com.okta.jwt.JwtVerificationException;
import com.okta.jwt.JwtVerifiers;

import java.time.Duration;

/**
 * @author sarvesh
 * @version 0.0.1
 * @since 0.0.1
 */
public class OktaTokenVerifier {
    private static final String jwtIssuer = "https://dev-27255988.okta.com/oauth2/default";

    public static Jwt validate(String token) throws JwtVerificationException {
        AccessTokenVerifier jwtVerifier = JwtVerifiers.accessTokenVerifierBuilder()
                .setIssuer(jwtIssuer)
                .setAudience("api://default")                   // defaults to 'api://default'
                .setConnectionTimeout(Duration.ofSeconds(1))    // defaults to 1s
                .setRetryMaxAttempts(2)                     // defaults to 2
                .setRetryMaxElapsed(Duration.ofSeconds(10)) // defaults to 10s
                .build();
        return jwtVerifier.decode(token);
    }

}
