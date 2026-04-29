package ca.bc.gov.nrs.fsp.api.endpoint.v1;

import ca.bc.gov.nrs.fsp.api.constants.v1.URL;
import ca.bc.gov.nrs.fsp.api.struct.v1.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * Authorization is enforced at the JWT validation layer: every authenticated
 * request is guaranteed to carry at least one FSPTS role (see
 * {@code FsptsRoleValidator}). For now any FSPTS role can hit any endpoint;
 * per-role {@code @PreAuthorize("hasRole('FSPTS_*')")} checks will be added
 * when role-scoped access is finalized.
 */
@RequestMapping(URL.BASE_URL)
@Tag(name = "FSP API", description = "Forest Stewardship Plan operations")
public interface FspApiEndpoint {

    // --- FSP ---

    @GetMapping(URL.FSP_SEARCH)
    @Operation(summary = "Search FSPs by client number, district, or status")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Search results returned"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    ResponseEntity<List<FspSearchResult>> searchFsp(@Valid FspSearchRequest request);

    @GetMapping(URL.FSP_BY_ID)
    @Operation(summary = "Get FSP by ID")
    ResponseEntity<FspRequest> getFspById(@PathVariable Long fspId);

    @PutMapping(URL.FSP_BY_ID)
    @Operation(summary = "Update FSP")
    ResponseEntity<FspRequest> updateFsp(
            @PathVariable Long fspId, @Valid @RequestBody FspRequest fspRequest);

    // --- Workflow ---

    @GetMapping(URL.WORKFLOW)
    @Operation(summary = "Get workflow history for an FSP")
    ResponseEntity<List<WorkflowResponse>> getWorkflow(@PathVariable Long fspId);

    @PostMapping(URL.WORKFLOW_ACTION)
    @Operation(summary = "Submit a workflow action for an FSP")
    ResponseEntity<Void> submitWorkflowAction(
            @PathVariable Long fspId, @Valid @RequestBody WorkflowRequest workflowRequest);

    // --- Stocking Standards ---

    @GetMapping(URL.STANDARDS)
    @Operation(summary = "Get stocking standards for an FSP")
    ResponseEntity<List<StandardRequest>> getStandards(@PathVariable Long fspId);

    @PutMapping(URL.STANDARDS)
    @Operation(summary = "Replace all stocking standards for an FSP")
    ResponseEntity<List<StandardRequest>> saveStandards(
            @PathVariable Long fspId, @Valid @RequestBody List<StandardRequest> standards);

    @DeleteMapping(URL.STANDARD_BY_ID)
    @Operation(summary = "Delete a stocking standard")
    ResponseEntity<Void> deleteStandard(
            @PathVariable Long fspId, @PathVariable Long standardId);

    // --- Attachments ---

    @GetMapping(URL.ATTACHMENTS)
    @Operation(summary = "List attachments for an FSP (metadata only)")
    ResponseEntity<List<AttachmentResponse>> getAttachments(@PathVariable Long fspId);

    @PostMapping(value = URL.ATTACHMENTS, consumes = "multipart/form-data")
    @Operation(summary = "Upload an attachment for an FSP")
    ResponseEntity<AttachmentResponse> uploadAttachment(
            @PathVariable Long fspId,
            @RequestParam("file") MultipartFile file,
            @RequestParam("typeCode") String typeCode) throws IOException;

    @GetMapping(URL.ATTACHMENT_DOWNLOAD)
    @Operation(summary = "Download an attachment")
    ResponseEntity<byte[]> downloadAttachment(
            @PathVariable Long fspId, @PathVariable Long attachmentId);

    @DeleteMapping(URL.ATTACHMENT_BY_ID)
    @Operation(summary = "Delete an attachment")
    ResponseEntity<Void> deleteAttachment(
            @PathVariable Long fspId, @PathVariable Long attachmentId);

    // --- Inbox ---

    @GetMapping(URL.INBOX)
    @Operation(summary = "Get FSP inbox items for the current user's district")
    ResponseEntity<List<FspSearchResult>> getInbox();

    // --- History ---

    @GetMapping(URL.HISTORY)
    @Operation(summary = "Get audit history for an FSP")
    ResponseEntity<List<WorkflowResponse>> getHistory(@PathVariable Long fspId);
}
