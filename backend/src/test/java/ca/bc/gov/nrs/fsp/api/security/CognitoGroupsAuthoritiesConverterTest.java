package ca.bc.gov.nrs.fsp.api.security;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.time.Instant;
import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class CognitoGroupsAuthoritiesConverterTest {

  private final CognitoGroupsAuthoritiesConverter converter = new CognitoGroupsAuthoritiesConverter();

  @Test
  void canonicalRole_emitsSingleAuthority() {
    Jwt jwt = baseJwtBuilder()
        .claim("cognito:groups", List.of("FSPTS_ADMINISTRATOR"))
        .build();

    assertThat(authorityNames(converter.convert(jwt)))
        .containsExactlyInAnyOrder("ROLE_FSPTS_ADMINISTRATOR");
  }

  @Test
  void orgSuffixedRole_emitsBothRawAndCanonical() {
    Jwt jwt = baseJwtBuilder()
        .claim("cognito:groups", List.of("FSPTS_ADMINISTRATOR_DPG"))
        .build();

    assertThat(authorityNames(converter.convert(jwt)))
        .containsExactlyInAnyOrder(
            "ROLE_FSPTS_ADMINISTRATOR_DPG",
            "ROLE_FSPTS_ADMINISTRATOR"
        );
  }

  @Test
  void unknownGroup_emitsOnlyRawAuthority() {
    Jwt jwt = baseJwtBuilder()
        .claim("cognito:groups", List.of("OTHER_GROUP"))
        .build();

    assertThat(authorityNames(converter.convert(jwt)))
        .containsExactlyInAnyOrder("ROLE_OTHER_GROUP");
  }

  @Test
  void multipleGroups_areAllConverted() {
    Jwt jwt = baseJwtBuilder()
        .claim("cognito:groups", List.of(
            "FSPTS_ADMINISTRATOR_DPG",
            "FSPTS_REVIEWER",
            "OTHER_GROUP"
        ))
        .build();

    assertThat(authorityNames(converter.convert(jwt)))
        .containsExactlyInAnyOrder(
            "ROLE_FSPTS_ADMINISTRATOR_DPG",
            "ROLE_FSPTS_ADMINISTRATOR",
            "ROLE_FSPTS_REVIEWER",
            "ROLE_OTHER_GROUP"
        );
  }

  @Test
  void duplicateGroups_areDeduplicated() {
    Jwt jwt = baseJwtBuilder()
        .claim("cognito:groups", List.of(
            "FSPTS_ADMINISTRATOR_DPG",
            "FSPTS_ADMINISTRATOR_DSE"
        ))
        .build();

    // Both org-suffixed groups emit the same canonical ROLE_FSPTS_ADMINISTRATOR;
    // the converter must not duplicate it.
    assertThat(authorityNames(converter.convert(jwt)))
        .containsExactlyInAnyOrder(
            "ROLE_FSPTS_ADMINISTRATOR_DPG",
            "ROLE_FSPTS_ADMINISTRATOR_DSE",
            "ROLE_FSPTS_ADMINISTRATOR"
        );
  }

  @Test
  void emptyGroups_emitsNoAuthorities() {
    Jwt jwt = baseJwtBuilder().claim("cognito:groups", List.of()).build();

    assertThat(converter.convert(jwt)).isEmpty();
  }

  @Test
  void missingGroupsClaim_emitsNoAuthorities() {
    Jwt jwt = baseJwtBuilder().build();

    assertThat(converter.convert(jwt)).isEmpty();
  }

  private static List<String> authorityNames(Collection<GrantedAuthority> authorities) {
    return authorities.stream().map(GrantedAuthority::getAuthority).toList();
  }

  private static Jwt.Builder baseJwtBuilder() {
    return Jwt.withTokenValue("token")
        .header("alg", "RS256")
        .claim("token_use", "access")
        .issuedAt(Instant.now())
        .expiresAt(Instant.now().plusSeconds(300));
  }
}
