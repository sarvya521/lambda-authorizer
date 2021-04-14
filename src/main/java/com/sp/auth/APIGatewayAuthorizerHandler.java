package com.sp.auth;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.sp.auth.dao.UserDao;
import com.sp.auth.dao.impl.UserDaoImpl;
import com.sp.auth.dto.AuthPolicy;
import com.sp.auth.dto.SpAuthenticatedUser;
import com.sp.auth.dto.TokenAuthorizerContext;
import com.sp.auth.util.JwtTokenUtil;
import com.sp.auth.util.OktaTokenVerifier;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.okta.jwt.Jwt;
import com.okta.jwt.JwtVerificationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.Objects;

import static com.sp.auth.dto.AuthPolicy.HttpMethod;

/**
 * @author sarvesh
 * @version 0.0.1
 * @since 0.0.1
 */
@Slf4j
public class APIGatewayAuthorizerHandler implements RequestHandler<TokenAuthorizerContext, AuthPolicy> {
    private static final Logger log = LoggerFactory.getLogger(APIGatewayAuthorizerHandler.class);
    private static final UserDao DAO = new UserDaoImpl();

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    @Override
    public AuthPolicy handleRequest(TokenAuthorizerContext input, Context context) {
        // log execution details
        log.debug("ENVIRONMENT VARIABLES: {}", GSON.toJson(System.getenv()));
        log.debug("CONTEXT: {}", GSON.toJson(context));

        String token = input.getAuthorizationToken();
        log.debug("TOKEN: {}", token);
        token = token.replace("Bearer ", "");

        // validate the incoming token
        // and produce the principal user identifier associated with the token

        // this could be accomplished in a number of ways:
        // 1. Call out to OAuth provider
        // 2. Decode a JWT token in-line
        // 3. Lookup in a self-managed DB
        Jwt jwt = null;
        try {
            jwt = OktaTokenVerifier.validate(token);
        } catch (JwtVerificationException ex) {
            log.error("TOKEN is not valid.", ex);
            // if the client token is not recognized or invalid
            // you can send a 401 Unauthorized response to the client by failing like so:
            // throw new RuntimeException("Unauthorized");
            throw new RuntimeException("Unauthorized");
        }
        log.info("decoded okta jwt: {}", GSON.toJson(jwt));
        String principalId = (String)jwt.getClaims().get("sub");
        log.info("SUB: {}", principalId);
        // if the token is valid, a policy should be generated which will allow or deny access to the client

        String methodArn = input.getMethodArn();
        String[] arnPartials = methodArn.split(":");
        String region = arnPartials[3];
        String awsAccountId = arnPartials[4];
        String[] apiGatewayArnPartials = arnPartials[5].split("/");
        String restApiId = apiGatewayArnPartials[0];
        String stage = apiGatewayArnPartials[1];
        String httpMethod = apiGatewayArnPartials[2];
        String resource = ""; // root resource
        if (apiGatewayArnPartials.length == 4) {
            resource = apiGatewayArnPartials[3];
        }
        log.debug("region: {} awsAccountId: {} restApiId: {} stage: {} httpMethod: {} resource: {}",
                region, awsAccountId, restApiId, stage, httpMethod, resource);

        final SpAuthenticatedUser authenticatedUser = DAO.findByEmail(principalId);
        if(Objects.isNull(authenticatedUser)) {
            log.info("user not found");
            // if access is denied, the client will receive a 403 Access Denied response
            return new AuthPolicy(principalId, AuthPolicy.PolicyDocument.getDenyAllPolicy(region, awsAccountId, restApiId, stage));
        }
        log.debug("SpAuthenticatedUser: {}", GSON.toJson(authenticatedUser));
        // if access is allowed, API Gateway will proceed with the back-end integration configured on the method that was called

        // this function must generate a policy that is associated with the recognized principal user identifier.
        // depending on your use case, you might store policies in a DB, or generate them on the fly

        // keep in mind, the policy is cached for 5 minutes by default (TTL is configurable in the authorizer)
        // and will apply to subsequent calls to any method/resource in the RestApi
        // made with the same token

        // the example policy below denies access to all resources in the RestApi
        return new AuthPolicy(
                principalId,
                AuthPolicy.PolicyDocument.getAllowOnePolicy(region, awsAccountId,
                        restApiId, stage, HttpMethod.valueOf(httpMethod.toUpperCase()), resource),
                Collections.singletonMap("X-Authorization", JwtTokenUtil.generateXAuthToken(authenticatedUser)));
    }
}
