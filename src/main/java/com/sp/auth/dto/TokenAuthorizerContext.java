package com.sp.auth.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Object representation of input to an implementation of an API Gateway custom authorizer
 * of type TOKEN
 *
 * @version 0.0.1
 * @since 0.0.1
 */
@Getter
@Setter
@NoArgsConstructor
public class TokenAuthorizerContext {
    private String type;
    private String authorizationToken;
    private String methodArn;

    /**
     * JSON input is deserialized into this object representation
     * @param type Static value - TOKEN
     * @param authorizationToken - Incoming bearer token sent by a client
     * @param methodArn - The API Gateway method ARN that a client requested
     */
    public TokenAuthorizerContext(String type, String authorizationToken, String methodArn) {
        this.type = type;
        this.authorizationToken = authorizationToken;
        this.methodArn = methodArn;
    }
}
