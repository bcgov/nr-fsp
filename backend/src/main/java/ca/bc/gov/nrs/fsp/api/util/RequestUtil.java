package ca.bc.gov.nrs.fsp.api.util;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

/**
 * The type Request util.
 */
public class RequestUtil {
  private RequestUtil() {
  }



  /**
   * Add to existing RequestUtil class.
   * Gets the current authenticated user's username from the Cognito JWT.
   * Cognito puts the username in "username" or "cognito:username" — adjust if needed.
   */
  public static String getCurrentUserName() {
    Jwt jwt = getCurrentJwt();
    String username = jwt.getClaimAsString("username");
    if (username == null) {
      username = jwt.getClaimAsString("cognito:username");
    }
    return username != null ? username : jwt.getSubject();
  }

  public static Jwt getCurrentJwt() {
    return (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
  }
}