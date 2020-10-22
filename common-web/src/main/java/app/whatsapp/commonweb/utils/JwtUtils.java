package app.whatsapp.commonweb.utils;

import app.whatsapp.common.constants.CommonConstants;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.Verification;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;

import javax.servlet.http.HttpServletRequest;
import java.time.Duration;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

public class JwtUtils {

    private static final String RANDOM_CYPHER = "randomKey";

    private static DecodedJWT decodeJwt(String jwt) throws JWTDecodeException {
        return JWT.decode(jwt);
    }

    public static boolean isExpired(String jwt) {
        if (StringUtils.isEmpty(jwt)) {
            return true;
        }
        Optional<Date> expiresAt = getExpiresAt(jwt);
        if (expiresAt.isPresent()) {
            return System.currentTimeMillis() > expiresAt.get().getTime();
        }
        return false;
    }

    private static String getKey(String key) {
        return RANDOM_CYPHER + key;
    }

    public static Optional<String> getIssuer(String jwt) {
        try {
            return Optional.of(decodeJwt(jwt).getIssuer());
        } catch (Exception e) {
        }
        return Optional.empty();
    }

    public static <T> Optional<T> getClaim(String jwt, String key, Class<T> tClass) {
        try {
            Claim claim = decodeJwt(jwt).getClaim(key);
            if (claim != null && !claim.isNull()) {
                return Optional.of(claim.as(tClass));
            }
        } catch (Exception e) {
        }
        return Optional.empty();
    }

    public static Optional<String> getClaim(String jwt, String key) {
        try {
            return getClaim(jwt, key, String.class);
        } catch (Exception e) {
        }
        return Optional.empty();
    }

    public static Optional<String> getJwtFromHeader(HttpServletRequest httpServletRequest, String headerKey) {
        if (httpServletRequest != null && StringUtils.isNotBlank(headerKey)) {
            return HttpUtils.getJwtFromAuthorizationHeader(httpServletRequest.getHeader(headerKey));
        }
        return Optional.empty();
    }

    public static Optional<String> getJwtFromHeader(HttpServletRequest httpServletRequest) {
        return getJwtFromHeader(httpServletRequest, HttpHeaders.AUTHORIZATION);
    }

    public static Optional<Date> getIssuedAt(String jwt) {
        try {
            return Optional.of(decodeJwt(jwt).getIssuedAt());
        } catch (Exception e) {
        }
        return Optional.empty();
    }

    public static Optional<Date> getExpiresAt(String jwt) {
        try {
            return Optional.of(decodeJwt(jwt).getExpiresAt());
        } catch (Exception e) {
        }
        return Optional.empty();
    }

    public static Optional<String> getSubject(String jwt) {
        if (StringUtils.isNotBlank(jwt)) {
            try {
                return Optional.of(decodeJwt(jwt).getSubject());
            } catch (Exception e) {
            }
        }
        return Optional.empty();
    }

    public static class Builder {

        private JWTCreator.Builder jwtBuilder;
        private String key;

        private Builder() {
            jwtBuilder = JWT.create();
        }

        public static Builder getInstance() {
            return new Builder();
        }

        public Builder withIssuer(String issuer) {
            jwtBuilder.withIssuer(issuer);
            return this;
        }

        public Builder withSubject(String subject) {
            jwtBuilder.withSubject(subject);
            return this;
        }

        public Builder withValidity(Duration duration) {
            if (duration == null || duration.isNegative()) {
                duration = Duration.ZERO;
            }
            jwtBuilder.withExpiresAt(new Date(System.currentTimeMillis() + duration.toMillis()));
            return this;
        }

        public Builder signWith(String key) {
            this.key = getKey(key);
            return this;
        }

        public Builder withClaim(String key, Object value) {
            if (StringUtils.isNotBlank(key) && value != null) {
                if (value instanceof String) {
                    jwtBuilder.withClaim(key, (String) value);
                } else if (value instanceof Long) {
                    jwtBuilder.withClaim(key, (Long) value);
                } else if (value instanceof Date) {
                    jwtBuilder.withClaim(key, (Date) value);
                } else if (value instanceof Double) {
                    jwtBuilder.withClaim(key, (Double) value);
                } else if (value instanceof Integer) {
                    jwtBuilder.withClaim(key, (Integer) value);
                } else if (value instanceof Boolean) {
                    jwtBuilder.withClaim(key, (Boolean) value);
                }
            }
            return this;
        }

        public Builder withClaims(Map<String, Object> claims) {
            addClaimsToBuilder(jwtBuilder, claims);
            return this;
        }

        public String build() {
            return jwtBuilder.withIssuedAt(new Date()).sign(Algorithm.HMAC256(key));
        }

        private void addClaimsToBuilder(JWTCreator.Builder builder, Map<String, Object> claims) {
            for (Map.Entry<String, Object> entry : claims.entrySet()) {
                withClaim(entry.getKey(), entry.getValue());
            }
        }
    }


    public static class Verifier {

        private Verification verification;
        private String jwt;

        private Verifier(String jwt, String key) {
            this.jwt = jwt;
            verification = JWT.require(Algorithm.HMAC256(key));
            withIssuer();
            withSubject();
            withIssuedAt();
        }

        private void withIssuedAt() {
            Optional<Date> issuedAt = getIssuedAt(jwt);
            if (issuedAt.isPresent()) {
                verification.acceptIssuedAt(issuedAt.get().getTime());
            }
        }

        public static Verifier getInstance(String jwt, String key) {
            return new Verifier(jwt, getKey(key));
        }

        private void withIssuer() {
            Optional<String> issuer = getIssuer(jwt);
            if (issuer.isPresent() && StringUtils.isNotBlank(issuer.get())) {
                verification.withIssuer(issuer.get());
            }
        }

        private void withSubject() {
            Optional<String> subject = getSubject(jwt);
            if (subject.isPresent() && StringUtils.isNotBlank(subject.get())) {
                verification.withSubject(subject.get());
            }
        }

        public Verifier withClaim(String key, Object value) {
            if (StringUtils.isNotBlank(key) && value != null) {
                if (value instanceof String) {
                    verification.withClaim(key, (String) value);
                } else if (value instanceof Long) {
                    verification.withClaim(key, (Long) value);
                } else if (value instanceof Date) {
                    verification.withClaim(key, (Date) value);
                } else if (value instanceof Double) {
                    verification.withClaim(key, (Double) value);
                } else if (value instanceof Integer) {
                    verification.withClaim(key, (Integer) value);
                } else if (value instanceof Boolean) {
                    verification.withClaim(key, (Boolean) value);
                }
            }
            return this;
        }

        public Verifier withClaims(Map<String, Object> claims) {
            addClaimsToBuilder(verification, claims);
            return this;
        }

        private void addClaimsToBuilder(Verification builder, Map<String, Object> claims) {
            for (Map.Entry<String, Object> entry : claims.entrySet()) {
                withClaim(entry.getKey(), entry.getValue());
            }
        }

        public boolean verify() {
            if (isExpired(jwt)) {
                return false;
            }
            try {
                verification.build().verify(jwt);
                return true;
            } catch (Exception e) {
                return false;
            }
        }

    }
}
