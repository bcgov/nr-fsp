package ca.bc.gov.nrs.fsp.api.security;

import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.Jwt;

/**
 * Rejects Cognito ID tokens (or anything that isn't an access token). Cognito
 * stamps the token type in the {@code token_use} claim — only {@code "access"}
 * is acceptable for protecting API endpoints.
 */
public class CognitoAccessTokenValidator implements OAuth2TokenValidator<Jwt> {

  private static final String TOKEN_USE_CLAIM = "token_use";
  private static final String EXPECTED_TOKEN_USE = "access";

  @Override
  public OAuth2TokenValidatorResult validate(Jwt jwt) {
    String tokenUse = jwt.getClaimAsString(TOKEN_USE_CLAIM);
    if (EXPECTED_TOKEN_USE.equals(tokenUse)) {
      return OAuth2TokenValidatorResult.success();
    }
    OAuth2Error error = new OAuth2Error(
        "invalid_token",
        "Token is not an access token (token_use must be 'access', was '" + tokenUse + "')",
        null
    );
    return OAuth2TokenValidatorResult.failure(error);
  }
}
