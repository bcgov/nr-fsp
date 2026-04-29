package ca.bc.gov.nrs.fsp.api.security;

import org.junit.jupiter.api.Test;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.Jwt;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

class CognitoAccessTokenValidatorTest {

  private final CognitoAccessTokenValidator validator = new CognitoAccessTokenValidator();

  @Test
  void accessToken_passes() {
    Jwt jwt = baseJwtBuilder().claim("token_use", "access").build();

    OAuth2TokenValidatorResult result = validator.validate(jwt);

    assertThat(result.hasErrors()).isFalse();
  }

  @Test
  void idToken_fails() {
    Jwt jwt = baseJwtBuilder().claim("token_use", "id").build();

    OAuth2TokenValidatorResult result = validator.validate(jwt);

    assertThat(result.hasErrors()).isTrue();
    assertThat(result.getErrors()).anySatisfy(err ->
        assertThat(err.getDescription()).contains("token_use must be 'access'")
    );
  }

  @Test
  void missingTokenUse_fails() {
    Jwt jwt = baseJwtBuilder().build();

    OAuth2TokenValidatorResult result = validator.validate(jwt);

    assertThat(result.hasErrors()).isTrue();
  }

  private static Jwt.Builder baseJwtBuilder() {
    return Jwt.withTokenValue("token")
        .header("alg", "RS256")
        .issuedAt(Instant.now())
        .expiresAt(Instant.now().plusSeconds(300));
  }
}
