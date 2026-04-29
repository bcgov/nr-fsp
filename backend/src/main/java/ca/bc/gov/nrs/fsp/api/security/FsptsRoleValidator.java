package ca.bc.gov.nrs.fsp.api.security;

import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.List;

/**
 * Rejects tokens that do not carry at least one FSPTS role in the
 * {@code cognito:groups} claim. Group names may include an organization
 * suffix (e.g. {@code FSPTS_ADMINISTRATOR_DPG}); see {@link FsptsRoles}
 * for matching rules.
 */
public class FsptsRoleValidator implements OAuth2TokenValidator<Jwt> {

  private static final String GROUPS_CLAIM = "cognito:groups";

  private static final OAuth2Error MISSING_ROLE = new OAuth2Error(
      "insufficient_scope",
      "Token does not carry a required FSPTS role in cognito:groups",
      null
  );

  @Override
  public OAuth2TokenValidatorResult validate(Jwt jwt) {
    List<String> groups = jwt.getClaimAsStringList(GROUPS_CLAIM);
    if (groups == null || groups.isEmpty()) {
      return OAuth2TokenValidatorResult.failure(MISSING_ROLE);
    }
    for (String group : groups) {
      if (FsptsRoles.canonicalRoleFor(group) != null) {
        return OAuth2TokenValidatorResult.success();
      }
    }
    return OAuth2TokenValidatorResult.failure(MISSING_ROLE);
  }
}
