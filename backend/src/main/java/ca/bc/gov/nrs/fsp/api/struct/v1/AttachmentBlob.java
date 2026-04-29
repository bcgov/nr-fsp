package ca.bc.gov.nrs.fsp.api.struct.v1;

/** Result of FSP_400_ATTACHMENTS.GET_ATTACH_BLOB exposed to the controller. */
public record AttachmentBlob(String fileName, byte[] content) {}
