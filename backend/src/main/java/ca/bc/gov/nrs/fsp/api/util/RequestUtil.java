package ca.bc.gov.nrs.fsp.api.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Map;

/**
 * The type Request util.
 */
public class RequestUtil {
  private RequestUtil() {
  }

  /**
   * Get the Sort.Order list from JSON string
   *
   * @param sortCriteriaJson The sort criteria JSON
   * @param objectMapper     The object mapper
   * @param sorts            The Sort.Order list
   * @throws JsonProcessingException the json processing exception
   */
  public static void getSortCriteria(String sortCriteriaJson, ObjectMapper objectMapper, List<Sort.Order> sorts) throws JsonProcessingException {
    if (StringUtils.isNotBlank(sortCriteriaJson)) {
      Map<String, String> sortMap = objectMapper.readValue(sortCriteriaJson, new TypeReference<>() {
      });
      sortMap.forEach((k, v) -> {
        if ("ASC".equalsIgnoreCase(v)) {
          sorts.add(new Sort.Order(Sort.Direction.ASC, k));
        } else {
          sorts.add(new Sort.Order(Sort.Direction.DESC, k));
        }
      });
    }
  }
}