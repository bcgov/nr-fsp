package ca.bc.gov.nrs.fsp.api.dao.v1.bean;

/**
 * Mirrors the THE.FSP_300_LICENSEE_OBJECT Oracle type used inside
 * THE.FSP_300_LICENSEE_VARRAY. Attribute order MUST match the SQL type definition.
 */
public record LicenseeArrayElement(
    String clientNumber,
    String clientName,
    String agreementDescription
) {}
