package com.nasa.collab.hub.security;

import io.jsonwebtoken.*;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Date;

@Service
public class JwtTokenService {

    @Value("${github.app.id}")
    private String githubAppId;

    @Value("${private.key.file}")
    private String privateKeyFilePath;

    private PrivateKey signingKey;
    private String jwtToken;

    private static final SignatureAlgorithm SIGNATURE_ALGORITHM = SignatureAlgorithm.RS256;
    private static final long TOKEN_TTL = 600000;


    @PostConstruct
    public void setSigningKey() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        File privateKeyFile = ResourceUtils.getFile(privateKeyFilePath);
        byte[] privateKey = Files.readAllBytes(Path.of(privateKeyFile.toURI()));

        KeySpec keySpec = new PKCS8EncodedKeySpec(privateKey);

        KeyFactory kf = KeyFactory.getInstance("RSA");
        this.signingKey = kf.generatePrivate(keySpec);
    }
    public String generateJwtToken() throws Exception {
        if(!isValidToken(jwtToken)) {
            jwtToken = Jwts.builder()
                    .setId("github-auth")
                    .setSubject("authenticating via private key")
                    .setIssuer(String.valueOf(githubAppId))
                    .signWith(signingKey, SIGNATURE_ALGORITHM)
                    .setExpiration(new Date(System.currentTimeMillis() + TOKEN_TTL))
                    .setIssuedAt(new Date())
                    .compact();
        }
        return jwtToken;
    }

    private boolean isValidToken(String jwtToken) throws Exception {
        if(jwtToken != null && validateToken(jwtToken)) {
            return true;
        }
        return false;
    }

    public boolean validateToken(String authToken) throws Exception {
        Jws<Claims> claims = null;
        try {
            claims = Jwts.parser().setSigningKey(signingKey).parseClaimsJws(authToken);
            return true;
        }
        catch (MalformedJwtException | UnsupportedJwtException | IllegalArgumentException ex) {
            throw new Exception(ex);
        }
        catch (ExpiredJwtException ex) {
            throw new ExpiredJwtException(claims.getHeader(), claims.getBody(), "Token has Expired", ex);
        }
    }
}
