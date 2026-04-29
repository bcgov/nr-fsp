package ca.bc.gov.nrs.fsp.api.dao.v1.bean;

/**
 * Mirrors the THE.FSP_ORG_UNIT_OBJECT Oracle type used inside
 * THE.FSP_ORG_UNIT_VARRAY. Attribute order MUST match the SQL type definition.
 */
public record OrgUnitArrayElement(
    String orgUnitNo,
    String orgUnitCode,
    String orgUnitName
) {}
