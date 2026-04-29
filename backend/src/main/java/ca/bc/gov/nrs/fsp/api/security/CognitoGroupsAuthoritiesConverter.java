package ca.bc.gov.nrs.fsp.api.security;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Maps {@code cognito:groups} entries to Spring Security authorities.
 *
 * <p>For each group:</p>
 * <ul>
 *   <li>The raw group is exposed as {@code ROLE_<GROUP>} so callers can check
 *   organization-specific roles like {@code ROLE_FSPTS_ADMINISTRATOR_DPG}.</li>
 *   <li>If the group matches a canonical FSPTS role (with optional org suffix),
 *   the canonical role is also exposed as {@code ROLE_<CANONICAL>} so
 *   {@code hasRole("FSPTS_ADMINISTRATOR")} matches every org-suffixed variant.</li>
 * </ul>
 */
public class CognitoGroupsAuthoritiesConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

  private static final String GROUPS_CLAIM = "cognito:groups";
  private static final String AUTHORITY_PREFIX = "ROLE_";

  @Override
  public Collection<GrantedAuthority> convert(Jwt jwt) {
    List<String> groups = jwt.getClaimAsStringList(GROUPS_CLAIM);
    if (groups == null || groups.isEmpty()) {
      return List.of();
    }
    Set<GrantedAuthority> authorities = new LinkedHashSet<>();
    for (String group : groups) {
      authorities.add(new SimpleGrantedAuthority(AUTHORITY_PREFIX + group));
      String canonical = FsptsRoles.canonicalRoleFor(group);
      if (canonical != null) {
        authorities.add(new SimpleGrantedAuthority(AUTHORITY_PREFIX + canonical));
      }
    }
    return authorities;
  }
}
