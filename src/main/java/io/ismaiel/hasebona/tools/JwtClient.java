/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.ismaiel.hasebona.tools;

import io.jsonwebtoken.*;

import javax.crypto.SecretKey;
import java.security.Key;
import java.security.PublicKey;
import java.util.Date;
import java.util.Map;

/**
 * Used to encode user info into JWT token, and to decode it again
 *
 * @author Ismaiel Nosairat <ismaiel.nosairat@gmail.com>
 */
public class JwtClient {

    private java.security.Key privateKey;    // private key , Asymmetric cryprtography only
    private java.security.PublicKey publicKey; // public  key , Asymmetric cryprtography only
    private SignatureAlgorithm signatureAlgorithm; // signatureAlgorithm
    private javax.crypto.SecretKey secretKey; // secret key, Symmetric cryprtography only
    private boolean isAsymmetric; // flag
    private long ttlMillis;  // token expiry timeout

    public Key getPrivateKey() {
        return privateKey;
    }

    void setPrivateKey(Key privateKey) {
        this.privateKey = privateKey;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    void setPublicKey(PublicKey publicKey) {
        this.publicKey = publicKey;
    }

    public SignatureAlgorithm getSignatureAlgorithm() {
        return signatureAlgorithm;
    }

    void setSignatureAlgorithm(SignatureAlgorithm signatureAlgorithm) {
        this.signatureAlgorithm = signatureAlgorithm;
    }

    public SecretKey getSecretKey() {
        return secretKey;
    }

    void setSecretKey(SecretKey secretKey) {
        this.secretKey = secretKey;
    }

    boolean isIsAsymmetric() {
        return isAsymmetric;
    }

    void setIsAsymmetric(boolean isAsymmetric) {
        this.isAsymmetric = isAsymmetric;
    }

    public long getTtlMillis() {
        return ttlMillis;
    }

    void setTtlMillis(long ttlMillis) {
        this.ttlMillis = ttlMillis;
    }

    public String createJWT(Map<String, Object> inputData) {

        //The JWT signature algorithm we will be using to sign the token
        //We will sign our JWT with our ApiKey secret
        //Let's set the JWT Claims
        JwtBuilder builder = Jwts.builder()
                .setClaims(inputData);
        if (this.isAsymmetric) {
            builder.signWith(this.signatureAlgorithm, this.privateKey);
        } else {
            builder.signWith(this.signatureAlgorithm, this.secretKey);
        }

        //if it has been specified, let's add the expiration
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        if (this.ttlMillis >= 0) {
            long expMillis = nowMillis + ttlMillis;
            Date exp = new Date(expMillis);
            builder.setExpiration(exp);
        }

        //Builds the JWT and serializes it to a compact, URL-safe string
        return builder.compact();

    }

    public Map<String, Object> parseJWT(String jwtToken) {

        //This line will throw an exception if it is not a signed JWS (as expected)
        //if (this.isAsymmetric)
        JwtParser parser = Jwts.parser();
        if (this.isAsymmetric) {
            parser.setSigningKey(this.publicKey);
        } else {
            parser.setSigningKey(this.secretKey);
        }

        Claims body = parser
                .parseClaimsJws(jwtToken)
                .getBody();
        Map<String, Object> map = (Map<String, Object>) body;
        return map;
    }
}
