package ca.bc.gov.nrs.fsp.api.mapper;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

/**
 * The type String mapper.
 */
@Component
public class StringMapper {

  /**
   * Map string.
   *
   * @param value the value
   * @return the string
   */
  public String map(String value) {
    if (StringUtils.isNotEmpty(value)) {
      return value.trim();
    }
    return value;
  }
}
