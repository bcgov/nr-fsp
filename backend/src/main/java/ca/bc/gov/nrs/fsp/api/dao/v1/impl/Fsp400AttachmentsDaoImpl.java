package ca.bc.gov.nrs.fsp.api.dao.v1.impl;

import ca.bc.gov.nrs.fsp.api.dao.v1.AbstractStoredProcedureDao;
import ca.bc.gov.nrs.fsp.api.dao.v1.Fsp400AttachmentsDao;
import oracle.jdbc.OracleTypes;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.io.ByteArrayInputStream;
import java.sql.Blob;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

@Repository
public class Fsp400AttachmentsDaoImpl extends AbstractStoredProcedureDao implements Fsp400AttachmentsDao {

  // GET: 27 positional params — see Fsp400AttachmentsDataManager.populateRequest
  private static final String CALL_GET = callSql(PACKAGE_NAME, "GET", 27);
  private static final String CALL_GET_ATTACH_BLOB = callSql(PACKAGE_NAME, "GET_ATTACH_BLOB", 4);
  private static final String CALL_CREATE_ATTACHMENT = callSql(PACKAGE_NAME, "CREATE_ATTACHMENT", 11);
  private static final String CALL_SAVE_ATTACHMENT_CONTENT = callSql(PACKAGE_NAME, "SAVE_ATTACHMENT_CONTENT", 3);
  private static final String CALL_SAVE_REFERENCE = callSql(PACKAGE_NAME, "SAVE_REFERENCE", 5);
  private static final String CALL_REMOVE_ATTACHMENT = callSql(PACKAGE_NAME, "REMOVE_ATTACHMENT", 4);

  public Fsp400AttachmentsDaoImpl(JdbcTemplate jdbcTemplate) {
    super(jdbcTemplate);
  }

  @Override
  public GetResult get(
      String pFspId, String pNewFspId,
      String pFspAmendmentNumber, String pNewFspAmendmentNumber,
      String pUserClientNumber, String pUserRole,
      String pAllAttachesInd) {
    return executeCall(CALL_GET,
        cs -> {
          // 1: p_fsp_id (INOUT, with IN value)
          setInOutString(cs, 1, pFspId);
          // 2: p_new_fsp_id (INOUT, with IN value)
          setInOutString(cs, 2, pNewFspId);
          // 3: p_fsp_plan_name — registered OUT only in legacy
          cs.registerOutParameter(3, Types.VARCHAR);
          // 4: p_fsp_org_units — OUT-only ARRAY in legacy
          cs.registerOutParameter(4, Types.ARRAY, ORG_UNIT_VARRAY_TYPE);
          // 5,6: p_fsp_status_code, p_fsp_status_desc — OUT only
          cs.registerOutParameter(5, Types.VARCHAR);
          cs.registerOutParameter(6, Types.VARCHAR);
          // 7: p_fsp_amendment_number INOUT
          setInOutString(cs, 7, pFspAmendmentNumber);
          // 8: p_new_fsp_amendment_number INOUT
          setInOutString(cs, 8, pNewFspAmendmentNumber);
          // 9: p_user_client_number INOUT
          setInOutString(cs, 9, pUserClientNumber);
          // 10: p_user_role INOUT
          setInOutString(cs, 10, pUserRole);
          // 11..15: OUT-only VARCHAR (submission_id, expiry_date, amendment_name,
          // amendment_efftv_date, agreement_holder_ind)
          for (int i = 11; i <= 15; i++) cs.registerOutParameter(i, Types.VARCHAR);
          // 16: p_all_attaches_ind INOUT
          setInOutString(cs, 16, pAllAttachesInd);
          // 17..25: 9 cursors
          for (int i = 17; i <= 25; i++) cs.registerOutParameter(i, OracleTypes.CURSOR);
          // 26,27: p_user_id, p_error_message — OUT only
          cs.registerOutParameter(26, Types.VARCHAR);
          cs.registerOutParameter(27, Types.VARCHAR);
        },
        cs -> {
          List<AttachmentRow> legalDocs       = readAttachmentCursor(cs, 17);
          List<AttachmentRow> amendDesc       = readAttachmentCursor(cs, 18);
          List<AttachmentRow> stockStandards  = readAttachmentCursor(cs, 19);
          List<AttachmentRow> fduMap          = readAttachmentCursor(cs, 20);
          List<AttachmentRow> identAreas1961  = readAttachmentCursor(cs, 21);
          List<AttachmentRow> identAreas1962  = readAttachmentCursor(cs, 22);
          List<AttachmentRow> declaredAreas   = readAttachmentCursor(cs, 23);
          List<AttachmentRow> supportingDocs  = readAttachmentCursor(cs, 24);
          List<AttachmentRow> ddmDecision     = readAttachmentCursor(cs, 25);
          String error = cs.getString(27);
          throwIfError(PACKAGE_NAME, "GET", error);
          return new GetResult(legalDocs, amendDesc, stockStandards, fduMap,
              identAreas1961, identAreas1962, declaredAreas, supportingDocs, ddmDecision,
              error);
        });
  }

  private List<AttachmentRow> readAttachmentCursor(java.sql.CallableStatement cs, int index) throws java.sql.SQLException {
    List<AttachmentRow> rows = new ArrayList<>();
    Object obj = cs.getObject(index);
    if (obj instanceof ResultSet rs) {
      try (ResultSet auto = rs) {
        while (auto.next()) {
          rows.add(new AttachmentRow(
              auto.getString("fsp_attachment_id"),
              auto.getString("fsp_amendment_number"),
              auto.getString("attachment_name"),
              auto.getString("attachment_description"),
              auto.getString("attachment_size"),
              auto.getString("consolidated_ind")));
        }
      }
    }
    return rows;
  }

  @Override
  public AttachBlob getAttachBlob(Long pAttachmentId) {
    return executeCall(CALL_GET_ATTACH_BLOB,
        cs -> {
          // 1: p_attachment_id INOUT VARCHAR (legacy: setString)
          setInOutString(cs, 1, String.valueOf(pAttachmentId));
          // 2: p_file_name OUT VARCHAR
          cs.registerOutParameter(2, Types.VARCHAR);
          // 3: p_blob OUT BLOB
          cs.registerOutParameter(3, Types.BLOB);
          // 4: p_error_message OUT VARCHAR
          cs.registerOutParameter(4, Types.VARCHAR);
        },
        cs -> {
          String fileName = cs.getString(2);
          byte[] content = readBlob(cs.getBlob(3));
          String error = cs.getString(4);
          throwIfError(PACKAGE_NAME, "GET_ATTACH_BLOB", error);
          return new AttachBlob(fileName, content, error);
        });
  }

  private static byte[] readBlob(Blob blob) throws java.sql.SQLException {
    if (blob == null) return new byte[0];
    long len = blob.length();
    if (len > Integer.MAX_VALUE) {
      throw new java.sql.SQLException("BLOB too large to load into memory: " + len + " bytes");
    }
    return blob.getBytes(1L, (int) len);
  }

  @Override
  public CreateAttachmentResult createAttachment(
      Long pFspId, String pFspAmendmentNumber, String attachmentType,
      String filename, Long fileSize, String description,
      String consolidatedInd, String userId) {
    return executeCall(CALL_CREATE_ATTACHMENT,
        cs -> {
          cs.setString(1, pFspId == null ? null : String.valueOf(pFspId));
          cs.setString(2, pFspAmendmentNumber);
          cs.setString(3, attachmentType);
          cs.setString(4, filename);
          cs.setString(5, fileSize == null ? null : String.valueOf(fileSize));
          cs.setString(6, description);
          cs.registerOutParameter(7, Types.BLOB);          // empty blob OUT
          cs.setString(8, consolidatedInd);
          cs.setString(9, userId);
          cs.registerOutParameter(10, Types.VARCHAR);     // created attachment_id
          cs.registerOutParameter(11, Types.VARCHAR);     // p_error_message
        },
        cs -> {
          String idStr = cs.getString(10);
          String error = cs.getString(11);
          throwIfError(PACKAGE_NAME, "CREATE_ATTACHMENT", error);
          Long createdId = (idStr == null || idStr.isBlank()) ? null : Long.valueOf(idStr.trim());
          return new CreateAttachmentResult(createdId, error);
        });
  }

  @Override
  public String saveAttachmentContent(Long attachmentId, byte[] content) {
    return executeCall(CALL_SAVE_ATTACHMENT_CONTENT,
        cs -> {
          cs.setString(1, String.valueOf(attachmentId));
          cs.setBlob(2, new ByteArrayInputStream(content), content.length);
          cs.registerOutParameter(3, Types.VARCHAR);
        },
        cs -> {
          String error = cs.getString(3);
          throwIfError(PACKAGE_NAME, "SAVE_ATTACHMENT_CONTENT", error);
          return error;
        });
  }

  @Override
  public String saveReference(Long fspId, String amendmentNumber, Long attachmentId, String userId) {
    return executeCall(CALL_SAVE_REFERENCE,
        cs -> {
          cs.setString(1, String.valueOf(fspId));
          cs.setString(2, amendmentNumber);
          cs.setString(3, String.valueOf(attachmentId));
          cs.setString(4, userId);
          cs.registerOutParameter(5, Types.VARCHAR);
        },
        cs -> {
          String error = cs.getString(5);
          throwIfError(PACKAGE_NAME, "SAVE_REFERENCE", error);
          return error;
        });
  }

  @Override
  public String removeAttachment(Long fspId, String amendmentNumber, Long attachmentId) {
    return executeCall(CALL_REMOVE_ATTACHMENT,
        cs -> {
          cs.setString(1, String.valueOf(fspId));
          cs.setString(2, amendmentNumber);
          cs.setString(3, String.valueOf(attachmentId));
          cs.registerOutParameter(4, Types.VARCHAR);
        },
        cs -> {
          String error = cs.getString(4);
          throwIfError(PACKAGE_NAME, "REMOVE_ATTACHMENT", error);
          return error;
        });
  }
}
