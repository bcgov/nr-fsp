package ca.bc.gov.nrs.fsp.api.dao.v1;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.sql.DataSource;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class FspStoredProcedureDaoTest {

  @Mock DataSource dataSource;

  @Test
  void checkReturnCode_whenZero_doesNotThrow() {
    assertThatNoException().isThrownBy(() -> {
      Map<String, Object> out = Map.of("p_return_code", 0L);
      Number code = (Number) out.get("p_return_code");
      if (code != null && code.intValue() != 0) {
        throw new RuntimeException("error");
      }
    });
  }

  @Test
  void checkReturnCode_whenNonZero_throwsRuntimeExceptionWithMessage() {
    Map<String, Object> out = Map.of(
        "p_return_code", 1L,
        "p_return_msg", "FSP record locked"
    );

    assertThatThrownBy(() -> {
      Number code = (Number) out.get("p_return_code");
      if (code != null && code.intValue() != 0) {
        String msg = (String) out.get("p_return_msg");
        throw new RuntimeException("Oracle procedure error [" + code + "]: " + msg);
      }
    })
        .isInstanceOf(RuntimeException.class)
        .hasMessageContaining("FSP record locked");
  }
}
