package ca.bc.gov.nrs.fsp.api.dao.v1.impl;

import ca.bc.gov.nrs.fsp.api.dao.v1.AbstractStoredProcedureDao;
import ca.bc.gov.nrs.fsp.api.dao.v1.Fsp700WorkflowDao;
import ca.bc.gov.nrs.fsp.api.dao.v1.bean.OrgUnitArrayElement;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class Fsp700WorkflowDaoImpl extends AbstractStoredProcedureDao implements Fsp700WorkflowDao {

  private static final int PARAM_COUNT = 70;
  private static final String CALL = callSql(PACKAGE_NAME, PROCEDURE_NAME, PARAM_COUNT);

  public Fsp700WorkflowDaoImpl(JdbcTemplate jdbcTemplate) {
    super(jdbcTemplate);
  }

  @Override
  public Result mainline(
      String pAction, String pNewFspId, String pFspId, String pFspPlanName,
      @Nullable List<OrgUnitArrayElement> pFspOrgUnits,
      String pFspStatusCode, String pFspStatusDesc,
      String pFspAmendmentCode, String pFspAmendmentDesc,
      String pNewFspAmendmentNumber, String pFspAmendmentNumber,
      String pUserClientNumber, String pUserRole, String pSubmissionId,
      String pFspExpiryDate, String pAmendmentName, String pAmendmentEfftvDate,
      String pFnrCompletedInd, String pFnrItemDescription, String pFnrEntryUserId, String pFnrEntryTimestamp, String pFnrComment,
      String pRsCompletedInd, String pRsItemDescription, String pRsEntryUserId, String pRsEntryTimestamp, String pRsComment,
      String pOrsCompletedInd, String pOrsItemDescription, String pOrsEntryUserId, String pOrsEntryTimestamp, String pOrsComment,
      String pDdmCompletedInd, String pDdmItemDescription, String pDdmEntryUserId, String pDdmEntryTimestamp, String pDdmComment,
      String pOtherCompletedInd, String pOtherItemDescription, String pOtherEntryUserId, String pOtherEntryTimestamp, String pOtherComment,
      String pEnableExtension,
      String pOtbhOfferedDate, String pOtbhOfferedComment, String pOtbhHeardDate, String pOtbhHeardComment,
      String pDdmOnlyStatusCode, String pDdmOnlyName, String pDdmOnlyDecisionDate, String pDdmOnlyComment,
      String pDdmOnlySubmissionDate, String pDdmOnlyEffectiveDate,
      String pExtensionStatusCode, String pExtensionId, String pExtensionName,
      String pExtensionDecisionDate, String pExtensionComment,
      String pExtensionSubmissionDate, String pExtensionEffectiveDate,
      String pFspReviewMilestoneTypCd, String pCompletedInd,
      String pOtbhDate, String pPlanStartDate, String pSubmissionDate, String pDecisionDate,
      String pReviewComment, String pUpdateUserid, String pExtensionIds) {

    return executeCall(CALL,
        cs -> {
          setInOutString(cs, 1, pAction);
          setInOutString(cs, 2, pNewFspId);
          setInOutString(cs, 3, pFspId);
          setInOutString(cs, 4, pFspPlanName);
          setInOutOrgUnits(cs, 5, pFspOrgUnits);
          setInOutString(cs, 6, pFspStatusCode);
          setInOutString(cs, 7, pFspStatusDesc);
          setInOutString(cs, 8, pFspAmendmentCode);
          setInOutString(cs, 9, pFspAmendmentDesc);
          setInOutString(cs, 10, pNewFspAmendmentNumber);
          setInOutString(cs, 11, pFspAmendmentNumber);
          setInOutString(cs, 12, pUserClientNumber);
          setInOutString(cs, 13, pUserRole);
          setInOutString(cs, 14, pSubmissionId);
          setInOutString(cs, 15, pFspExpiryDate);
          setInOutString(cs, 16, pAmendmentName);
          setInOutString(cs, 17, pAmendmentEfftvDate);
          setInOutString(cs, 18, pFnrCompletedInd);
          setInOutString(cs, 19, pFnrItemDescription);
          setInOutString(cs, 20, pFnrEntryUserId);
          setInOutString(cs, 21, pFnrEntryTimestamp);
          setInOutString(cs, 22, pFnrComment);
          setInOutString(cs, 23, pRsCompletedInd);
          setInOutString(cs, 24, pRsItemDescription);
          setInOutString(cs, 25, pRsEntryUserId);
          setInOutString(cs, 26, pRsEntryTimestamp);
          setInOutString(cs, 27, pRsComment);
          setInOutString(cs, 28, pOrsCompletedInd);
          setInOutString(cs, 29, pOrsItemDescription);
          setInOutString(cs, 30, pOrsEntryUserId);
          setInOutString(cs, 31, pOrsEntryTimestamp);
          setInOutString(cs, 32, pOrsComment);
          setInOutString(cs, 33, pDdmCompletedInd);
          setInOutString(cs, 34, pDdmItemDescription);
          setInOutString(cs, 35, pDdmEntryUserId);
          setInOutString(cs, 36, pDdmEntryTimestamp);
          setInOutString(cs, 37, pDdmComment);
          setInOutString(cs, 38, pOtherCompletedInd);
          setInOutString(cs, 39, pOtherItemDescription);
          setInOutString(cs, 40, pOtherEntryUserId);
          setInOutString(cs, 41, pOtherEntryTimestamp);
          setInOutString(cs, 42, pOtherComment);
          setInOutString(cs, 43, pEnableExtension);
          setInOutString(cs, 44, pOtbhOfferedDate);
          setInOutString(cs, 45, pOtbhOfferedComment);
          setInOutString(cs, 46, pOtbhHeardDate);
          setInOutString(cs, 47, pOtbhHeardComment);
          setInOutString(cs, 48, pDdmOnlyStatusCode);
          setInOutString(cs, 49, pDdmOnlyName);
          setInOutString(cs, 50, pDdmOnlyDecisionDate);
          setInOutString(cs, 51, pDdmOnlyComment);
          setInOutString(cs, 52, pDdmOnlySubmissionDate);
          setInOutString(cs, 53, pDdmOnlyEffectiveDate);
          setInOutString(cs, 54, pExtensionStatusCode);
          setInOutString(cs, 55, pExtensionId);
          setInOutString(cs, 56, pExtensionName);
          setInOutString(cs, 57, pExtensionDecisionDate);
          setInOutString(cs, 58, pExtensionComment);
          setInOutString(cs, 59, pExtensionSubmissionDate);
          setInOutString(cs, 60, pExtensionEffectiveDate);
          setInOutString(cs, 61, pFspReviewMilestoneTypCd);
          setInOutString(cs, 62, pCompletedInd);
          setInOutString(cs, 63, pOtbhDate);
          setInOutString(cs, 64, pPlanStartDate);
          setInOutString(cs, 65, pSubmissionDate);
          setInOutString(cs, 66, pDecisionDate);
          setInOutString(cs, 67, pReviewComment);
          setInOutString(cs, 68, pUpdateUserid);     // legacy: p_UPDATE_USERID
          setInOutString(cs, 69, null);              // P_ERROR_MESSAGE
          setInOutString(cs, 70, pExtensionIds);
        },
        cs -> {
          Result r = new Result(
              cs.getString(1),  cs.getString(2),  cs.getString(3),  cs.getString(4),
              readOrgUnits(cs, 5),
              cs.getString(6),  cs.getString(7),  cs.getString(8),  cs.getString(9),
              cs.getString(10), cs.getString(11), cs.getString(12), cs.getString(13),
              cs.getString(14), cs.getString(15), cs.getString(16), cs.getString(17),
              cs.getString(18), cs.getString(19), cs.getString(20), cs.getString(21),
              cs.getString(22), cs.getString(23), cs.getString(24), cs.getString(25),
              cs.getString(26), cs.getString(27), cs.getString(28), cs.getString(29),
              cs.getString(30), cs.getString(31), cs.getString(32), cs.getString(33),
              cs.getString(34), cs.getString(35), cs.getString(36), cs.getString(37),
              cs.getString(38), cs.getString(39), cs.getString(40), cs.getString(41),
              cs.getString(42), cs.getString(43), cs.getString(44), cs.getString(45),
              cs.getString(46), cs.getString(47), cs.getString(48), cs.getString(49),
              cs.getString(50), cs.getString(51), cs.getString(52), cs.getString(53),
              cs.getString(54), cs.getString(55), cs.getString(56), cs.getString(57),
              cs.getString(58), cs.getString(59), cs.getString(60), cs.getString(61),
              cs.getString(62), cs.getString(63), cs.getString(64), cs.getString(65),
              cs.getString(66), cs.getString(67), cs.getString(68), cs.getString(69),
              cs.getString(70));
          throwIfError(PACKAGE_NAME, PROCEDURE_NAME, r.pErrorMessage());
          return r;
        });
  }
}
