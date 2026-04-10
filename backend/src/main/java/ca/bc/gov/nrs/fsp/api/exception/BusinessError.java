package ca.bc.gov.nrs.fsp.api.exception;

import lombok.Getter;

public enum BusinessError {
  FSP_NOT_FOUND("FSP with id :: $? was not found."),
  FSP_ALREADY_SUBMITTED("FSP with id :: $? has already been submitted."),
  STANDARD_NOT_FOUND("Stocking standard with id :: $? was not found."),
  ATTACHMENT_NOT_FOUND("Attachment with id :: $? was not found."),
  INVALID_WORKFLOW_ACTION("Action :: $? is not valid for the current FSP status."),
  ATTACHMENT_FSP_MISMATCH("Attachment :: $? does not belong to FSP :: $?.");

  @Getter
  private final String code;

  BusinessError(final String code) {
    this.code = code;
  }
}