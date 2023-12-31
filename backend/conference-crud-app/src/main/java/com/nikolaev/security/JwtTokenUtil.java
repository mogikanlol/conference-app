package com.nikolaev.security;

import com.nikolaev.user.AuthorityName;
import com.nikolaev.user.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
//import org.springframework.mobile.device.Device;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtTokenUtil implements Serializable {


    static final String CLAIM_KEY_USERNAME = "sub";
    static final String CLAIM_KEY_AUDIENCE = "audience";
    static final String CLAIM_KEY_CREATED = "created";
    static final String CLAIM_KEY_CONFIRMED = "confirmed";
    static final String CLAIM_KEY_ID = "userId";
    static final String CLAIM_ADMIN = "isAdmin";

    private static final String AUDIENCE_UNKNOWN = "unknown";
    private static final String AUDIENCE_WEB = "web";
    private static final String AUDIENCE_MOBILE = "mobile";
    private static final String AUDIENCE_TABLET = "tablet";

    //    @Value("${jwt.secret}")
    private String secret = """
            Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod
            tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam,
            quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo
            consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse
            cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non
            proident, sunt in culpa qui officia deserunt mollit anim id est laborum.""";

    //    @Value("${jwt.expiration}")
    private Long expiration = 604800L;

    public String getUsernameFromToken(String token) {
        String username;
        try {
            final Claims claims = getClaimsFromToken(token);
            username = claims.getSubject();
        } catch (Exception e) {
            username = null;
        }
        return username;
    }

    public Date getCreatedDateFromToken(String token) {
        Date created;
        try {
            final Claims claims = getClaimsFromToken(token);
            created = new Date((Long) claims.get(CLAIM_KEY_CREATED));
        } catch (Exception e) {
            created = null;
        }
        return created;
    }

    public Date getExpirationDateFromToken(String token) {
        Date expiration;
        try {
            final Claims claims = getClaimsFromToken(token);
            expiration = claims.getExpiration();
        } catch (Exception e) {
            expiration = null;
        }
        return expiration;
    }

    public String getAudienceFromToken(String token) {
        String audience;
        try {
            final Claims claims = getClaimsFromToken(token);
            audience = (String) claims.get(CLAIM_KEY_AUDIENCE);
        } catch (Exception e) {
            audience = null;
        }
        return audience;
    }

    private Claims getClaimsFromToken(String token) {
        Claims claims;
        try {
            claims = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            claims = null;
        }
        return claims;
    }

    private Date generateExpirationDate() {
        return new Date(System.currentTimeMillis() + expiration * 1000);
    }

    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    private Boolean isCreatedBeforeLastPasswordReset(Date created, Date lastPasswordReset) {
        return (lastPasswordReset != null && created.before(lastPasswordReset));
    }
//
//    private String generateAudience(Device device) {
//        String audience = AUDIENCE_UNKNOWN;
//        if (device.isNormal()) {
//            audience = AUDIENCE_WEB;
//        } else if (device.isTablet()) {
//            audience = AUDIENCE_TABLET;
//        } else if (device.isMobile()) {
//            audience = AUDIENCE_MOBILE;
//        }
//        return audience;
//    }

    private Boolean ignoreTokenExpiration(String token) {
        String audience = getAudienceFromToken(token);
        return (AUDIENCE_TABLET.equals(audience) || AUDIENCE_MOBILE.equals(audience));
    }

//    public String generateToken(UserDetails userDetails, Device device) {
//        Map<String, Object> claims = new HashMap<>();
//        claims.put(CLAIM_KEY_USERNAME, userDetails.getUsername());
//        claims.put(CLAIM_KEY_AUDIENCE, generateAudience(device));
//        claims.put(CLAIM_KEY_CREATED, new Date());
//        if (userDetails instanceof JwtUser) {
//            claims.put(CLAIM_KEY_ID, ((JwtUser) userDetails).getId());
//        }
//        claims.put(CLAIM_ADMIN, false);
//        for (GrantedAuthority grantedAuthority : userDetails.getAuthorities()) {
//            if (grantedAuthority.getAuthority().equals(AuthorityName.ROLE_ADMIN.name())){
//                claims.put(CLAIM_ADMIN, true);
//            }
//        }
//        return generateToken(claims);
//    }

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(CLAIM_KEY_USERNAME, userDetails.getUsername());
//        claims.put(CLAIM_KEY_AUDIENCE, generateAudience(device));
        claims.put(CLAIM_KEY_CREATED, new Date());
        if (userDetails instanceof JwtUser) {
            claims.put(CLAIM_KEY_ID, ((JwtUser) userDetails).getId());
        }
        claims.put(CLAIM_ADMIN, false);
        for (GrantedAuthority grantedAuthority : userDetails.getAuthorities()) {
            if (grantedAuthority.getAuthority().equals(AuthorityName.ROLE_ADMIN.name())){
                claims.put(CLAIM_ADMIN, true);
            }
        }
        return generateToken(claims);
    }

//    public String generateToken(User user) {
//        Map<String, Object> claims = new HashMap<>();
//        claims.put(CLAIM_KEY_USERNAME, user.getUsername());
//        claims.put(CLAIM_KEY_CREATED, new Date());
//        claims.put(CLAIM_KEY_CONFIRMED, user.isConfirmed());
//        return generateToken(claims);
//    }
//
//    public String generateToken(User user, Device device) {
//        Map<String, Object> claims = new HashMap<>();
//        claims.put(CLAIM_KEY_USERNAME, user.getUsername());
//        claims.put(CLAIM_KEY_AUDIENCE, generateAudience(device));
//        claims.put(CLAIM_KEY_CREATED, new Date());
//        claims.put(CLAIM_KEY_CONFIRMED, user.isConfirmed());
//        return generateToken(claims);
//    }

    public String generateToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(CLAIM_KEY_USERNAME, user.getEmail());
        claims.put(CLAIM_KEY_CREATED, new Date());
        claims.put(CLAIM_KEY_CONFIRMED, user.isConfirmed());
        return generateToken(claims);
    }

//    public String generateToken(User user, Device device) {
//        Map<String, Object> claims = new HashMap<>();
//        claims.put(CLAIM_KEY_USERNAME, user.getEmail());
//        claims.put(CLAIM_KEY_AUDIENCE, generateAudience(device));
//        claims.put(CLAIM_KEY_CREATED, new Date());
//        claims.put(CLAIM_KEY_CONFIRMED, user.isConfirmed());
//        return generateToken(claims);
//    }

    String generateToken(Map<String, Object> claims) {
        JwtBuilder jwtBuilder = Jwts.builder()
                .setClaims(claims)
                .setExpiration(generateExpirationDate());

        byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        Key key = Keys.hmacShaKeyFor(keyBytes);
        jwtBuilder.signWith(key); //or signWith(Key, SignatureAlgorithm)
        return jwtBuilder.compact();
//                .signWith(SignatureAlgorithm.HS512, secret)
//                .compact();
    }

    public Boolean canTokenBeRefreshed(String token, Date lastPasswordReset) {
        final Date created = getCreatedDateFromToken(token);
        return !isCreatedBeforeLastPasswordReset(created, lastPasswordReset)
                && (!isTokenExpired(token) || ignoreTokenExpiration(token));
    }

    public String refreshToken(String token) {
        String refreshedToken;
        try {
            final Claims claims = getClaimsFromToken(token);
            claims.put(CLAIM_KEY_CREATED, new Date());
            refreshedToken = generateToken(claims);
        } catch (Exception e) {
            refreshedToken = null;
        }
        return refreshedToken;
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        JwtUser user = (JwtUser) userDetails;
        final String username = getUsernameFromToken(token);
        final Date created = getCreatedDateFromToken(token);
        //final Date expiration = getExpirationDateFromToken(token);
        return (
                username.equals(user.getEmail())
                        && !isTokenExpired(token)
                        && !isCreatedBeforeLastPasswordReset(created, user.getLastPasswordResetDate()));
    }
}