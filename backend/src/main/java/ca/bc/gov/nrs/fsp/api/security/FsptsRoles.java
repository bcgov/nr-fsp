package ca.bc.gov.nrs.fsp.api.security;

import java.util.List;

/**
 * Canonical FSPTS role names. Cognito groups may carry an organization suffix
 * (e.g. {@code FSPTS_ADMINISTRATOR_DPG}); a group is considered to have a role
 * if it equals the canonical name or starts with the canonical name followed
 * by an underscore.
 */
public final class FsptsRoles {

  public static final String ADMINISTRATOR   = "FSPTS_ADMINISTRATOR";
  public static final String DECISION_MAKER  = "FSPTS_DECISION_MAKER";
  public static final String REVIEWER        = "FSPTS_REVIEWER";
  public static final String VIEW_ALL        = "FSPTS_VIEW_ALL";
  public static final String SUBMITTER       = "FSPTS_SUBMITTER";
  public static final String VIEW_ONLY       = "FSPTS_VIEW_ONLY";

  public static final List<String> ALL = List.of(
      ADMINISTRATOR, DECISION_MAKER, REVIEWER, VIEW_ALL, SUBMITTER, VIEW_ONLY
  );

  private FsptsRoles() {}

  /**
   * Returns the canonical FSPTS role that matches the given Cognito group, or
   * {@code null} if the group does not map to any known role. A group matches
   * a role when it equals the role name or starts with {@code role + "_"}.
   */
  public static String canonicalRoleFor(String group) {
    if (group == null) return null;
    for (String role : ALL) {
      if (group.equals(role) || group.startsWith(role + "_")) {
        return role;
      }
    }
    return null;
  }
}
