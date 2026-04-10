package ca.bc.gov.nrs.fsp.api.struct.v1;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class WorkflowRequest extends BaseRequest {

  @NotBlank(message = "Action is required")
  @Size(max = 20)
  private String action;

  @Size(max = 2000)
  private String comments;
}