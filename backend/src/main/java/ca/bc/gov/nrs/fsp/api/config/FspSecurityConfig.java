package ca.bc.gov.nrs.fsp.api.config;

import ca.bc.gov.nrs.fsp.api.security.CognitoAccessTokenValidator;
import ca.bc.gov.nrs.fsp.api.security.CognitoGroupsAuthoritiesConverter;
import ca.bc.gov.nrs.fsp.api.security.FsptsRoleValidator;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtValidators;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;

/**
 * Security beans for the FSP API.
 *
 * <p>Authentication is performed against Cognito-issued JWTs. On top of the
 * standard signature/issuer/expiry checks we layer two custom validators:</p>
 * <ul>
 *   <li>{@link CognitoAccessTokenValidator} — only access tokens are accepted
 *   (rejects ID tokens that happen to be presented as bearer tokens).</li>
 *   <li>{@link FsptsRoleValidator} — the token must carry at least one FSPTS
 *   role in the {@code cognito:groups} claim.</li>
 * </ul>
 *
 * <p>Authorities are derived from {@code cognito:groups} via
 * {@link CognitoGroupsAuthoritiesConverter}, which exposes both the raw,
 * organization-suffixed group and the canonical role so future endpoint-level
 * authorization can match either form.</p>
 */
@Configuration
@EnableMethodSecurity
public class FspSecurityConfig {

  @Bean
  public JwtDecoder jwtDecoder(OAuth2ResourceServerProperties properties) {
    String jwkSetUri = properties.getJwt().getJwkSetUri();
    String issuerUri = properties.getJwt().getIssuerUri();

    NimbusJwtDecoder decoder = NimbusJwtDecoder.withJwkSetUri(jwkSetUri).build();

    OAuth2TokenValidator<Jwt> validator = new DelegatingOAuth2TokenValidator<>(
        JwtValidators.createDefaultWithIssuer(issuerUri),
        new CognitoAccessTokenValidator(),
        new FsptsRoleValidator()
    );
    decoder.setJwtValidator(validator);
    return decoder;
  }

  @Bean
  public JwtAuthenticationConverter jwtAuthenticationConverter() {
    JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
    converter.setJwtGrantedAuthoritiesConverter(new CognitoGroupsAuthoritiesConverter());
    return converter;
  }
}
