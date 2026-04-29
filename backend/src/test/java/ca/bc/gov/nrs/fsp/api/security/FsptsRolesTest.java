package ca.bc.gov.nrs.fsp.api.security;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

class FsptsRolesTest {

  @ParameterizedTest(name = "[{index}] {0} -> {1}")
  @CsvSource({
      // Canonical names match themselves
      "FSPTS_ADMINISTRATOR,           FSPTS_ADMINISTRATOR",
      "FSPTS_DECISION_MAKER,          FSPTS_DECISION_MAKER",
      "FSPTS_REVIEWER,                FSPTS_REVIEWER",
      "FSPTS_VIEW_ALL,                FSPTS_VIEW_ALL",
      "FSPTS_VIEW_ONLY,               FSPTS_VIEW_ONLY",
      "FSPTS_SUBMITTER,               FSPTS_SUBMITTER",
      // Org-suffixed names map back to the canonical role
      "FSPTS_ADMINISTRATOR_DPG,       FSPTS_ADMINISTRATOR",
      "FSPTS_ADMINISTRATOR_DPG_DSE,   FSPTS_ADMINISTRATOR",
      "FSPTS_DECISION_MAKER_DSE,      FSPTS_DECISION_MAKER",
      "FSPTS_VIEW_ALL_X,              FSPTS_VIEW_ALL",
      "FSPTS_VIEW_ONLY_Y,             FSPTS_VIEW_ONLY",
  })
  void canonicalRoleFor_matchesKnownRoles(String group, String expected) {
    assertThat(FsptsRoles.canonicalRoleFor(group)).isEqualTo(expected);
  }

  @ParameterizedTest(name = "[{index}] {0} -> null")
  @CsvSource({
      "FSPTS_ADMIN",                 // shorter than any canonical name
      "FSPTS_VIEW",                  // ambiguous prefix shared by VIEW_ALL/VIEW_ONLY but not equal/followed by '_'
      "FSPTS",                       // bare prefix
      "OTHER_FSPTS_ADMINISTRATOR",   // canonical role is not at the start
      "fsp_administrator",           // case-sensitive
      "''",                          // empty
  })
  void canonicalRoleFor_rejectsUnrelatedGroups(String group) {
    assertThat(FsptsRoles.canonicalRoleFor(group)).isNull();
  }

  @Test
  void canonicalRoleFor_nullGroupReturnsNull() {
    assertThat(FsptsRoles.canonicalRoleFor(null)).isNull();
  }

  @Test
  void viewAllAndViewOnly_doNotCollide() {
    // Both share the prefix "FSPTS_VIEW", but only their canonical names map back.
    assertThat(FsptsRoles.canonicalRoleFor("FSPTS_VIEW_ALL_DPG")).isEqualTo("FSPTS_VIEW_ALL");
    assertThat(FsptsRoles.canonicalRoleFor("FSPTS_VIEW_ONLY_DPG")).isEqualTo("FSPTS_VIEW_ONLY");
  }
}
