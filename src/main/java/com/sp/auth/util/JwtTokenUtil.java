package com.sp.auth.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sp.auth.dto.SpAuthenticatedUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.DefaultClaims;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Date;
import java.util.UUID;

/**
 * @author sarvesh
 * @version 0.0.1
 * @since 0.0.1
 */
public class JwtTokenUtil {
    private static final String SECRET_KEY = "6Pw9eSE@8XTNX]1=_kEYU~KvGz9mB*A|M=%AgsfuUH}#ZItuJ#@U#8hq}@E" +
            "%dy$ljEMVvEF5G$mEcOV^+vS]F]BUvy(1_2@VtwSvpkuc73idA[{XiUY}PjZdUUl4$)" +
            "W2T2uAgS$#0RJi^zw{J6feK[kP*NrW|CdBqYwrcD#F%Em~hZmQF*720Y59_tOB[daSeN-23[zO_Bcb]{GY1nN%JE)fh3ZF4N|2" +
            "(J]LuSVvo=Dovu}qvrs5{8GPl]C[DVjV]Vkr4mshGqdGtIxVjtHN2^#OazRF)Q2PyBKg)" +
            "nWRwFWrMni[~ue}p42MzGTL0zeNjqdqZjPSJLKSV{U$h@czZ|urEgY3=3~b2iy+RGBe3i%Jhlj+3NLQXN+aL{xy" +
            "(@pP02n9j1pOGxiSV=hHBPV@Xw#}(]zc}#3=U[Fd7GTuU[k3AA4+Tii-" +
            "(uIYSQfAcwG70wM[cFDwZe{I7I[245GkXJMnC6v4rdjC}16SB7^y[I$zrLWJbLr8NnNevv%$vUk)kkLNrr{H4)" +
            "Noxo|yG@Qra[KtfO7qmA+v0LG)pd64XtkC8WlNDPQ]IQY7*x2wn|%k)}XbfYN}11IJFL" +
            "(I0lTc9p}iHc}-%Zmk7c@%yC{gIy%R_CnSx@bj(tDwwFVi%|WsyO{^$uGzq]NFk[a]b|S#t[ua" +
            "(zx99iq[b}rb9n*kv12d^1LTGeX8K1NcE+0tpo}GEYp5*pIaB*" +
            "(}n|wI]*#*ovKRiR6]#ggaqlYPNo+p=4MLv^%^rFXF*]4JNF*negapf_pb_D3LD9Eo}*sY~)lRQWkjEP()" +
            "0b3cHR44jlmzDgqlz8rgyDKbdbKF#hbOGoa0WHEg0Hr1UZ7EJnsl$P1h(qhXvhbU8wS03i*|2m]CW|y}1U9s5)" +
            "i[R1BXYe$QLq|z*$70lzLS{L#c$vx58[s[JcaWg7W]2_iiluaTVgFvAI0}yd0ExWLQCiKt1%4Bm3_g0zBAh}Ri+]ERxT#jKqn]ZYzpk" +
            "}USWE2d0gFGQywI";
    private static final SignatureAlgorithm SIGNATURE_ALGORITHM = SignatureAlgorithm.HS512;
    private static final Key SIGNING_KEY = new SecretKeySpec(DatatypeConverter.parseBase64Binary(SECRET_KEY),
            SIGNATURE_ALGORITHM.getJcaName());
    private static final String JWT_ISSUER = "https://dev-27255988.okta.com/oauth2/default";

    public static String generateXAuthToken(SpAuthenticatedUser user) {
        Claims claims = new DefaultClaims();
        claims.setSubject(user.getUsername());
        claims.setIssuer(JWT_ISSUER);
        claims.setIssuedAt(new Date());
        claims.setExpiration(new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000));// 24 hours
        claims.setId(UUID.randomUUID().toString());
        claims.put("userDetails", user);
        return "Bearer " + Jwts.builder()
                .setClaims(claims)
                .signWith(SIGNING_KEY, SIGNATURE_ALGORITHM)
                .compact();
    }

    public static SpAuthenticatedUser getUserDetails(String token) {
        final Object userDetails = Jwts.parserBuilder()
                .setSigningKey(SIGNING_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("userDetails");
        SpAuthenticatedUser user = null;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            final String json = objectMapper.writeValueAsString(userDetails);
            user = objectMapper.readValue(json, new TypeReference<SpAuthenticatedUser>() {
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }

    public static String getUsername(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(SIGNING_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject().split(",")[0];
    }

    public static Date getExpirationDate(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(SIGNING_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.getExpiration();
    }

    public static boolean validate(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(SIGNING_KEY)
                    .build().parseClaimsJws(token);
        } catch (Exception ex) {
            throw ex;
        }
        return true;
    }
}
