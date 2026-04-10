package ca.bc.gov.nrs.fsp.api.struct.v1;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class FspRequest extends BaseRequest {

  private Long fspId;

  @NotBlank(message = "Client number is required")
  @Size(max = 10)
  private String clientNumber;

  @Size(max = 100)
  private String clientName;

  @NotBlank(message = "Status code is required")
  @Size(max = 10)
  private String fspStatusCode;

  private Long orgUnitNo;

  private LocalDate fspStartDate;

  private LocalDate fspEndDate;

  private Integer amendmentNumber;
}