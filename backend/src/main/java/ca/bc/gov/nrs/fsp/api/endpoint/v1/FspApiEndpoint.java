package ca.bc.gov.nrs.fsp.api.endpoint.v1;

import ca.bc.gov.nrs.fsp.api.constants.v1.URL;
import ca.bc.gov.nrs.fsp.api.struct.v1.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RequestMapping(URL.BASE_URL)
@Tag(name = "FSP API", description = "Forest Stewardship Plan operations")
public interface FspApiEndpoint {

    // --- FSP ---

    @GetMapping(URL.FSP_SEARCH)
    @PreAuthorize("hasAuthority('SCOPE_fsp:read')")
    @Operation(summary = "Search FSPs by client number, district, or status")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Search results returned"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    ResponseEntity<List<FspSearchResult>> searchFsp(@Valid FspSearchRequest request);

    @GetMapping(URL.FSP_BY_ID)
    @PreAuthorize("hasAuthority('SCOPE_fsp:read')")
    @Operation(summary = "Get FSP by ID")
    ResponseEntity<FspRequest> getFspById(@PathVariable Long fspId);

    @PutMapping(URL.FSP_BY_ID)
    @PreAuthorize("hasAuthority('SCOPE_fsp:write')")
    @Operation(summary = "Update FSP")
    ResponseEntity<FspRequest> updateFsp(
            @PathVariable Long fspId, @Valid @RequestBody FspRequest fspRequest);

    // --- Workflow ---

    @GetMapping(URL.WORKFLOW)
    @PreAuthorize("hasAuthority('SCOPE_fsp:read')")
    @Operation(summary = "Get workflow history for an FSP")
    ResponseEntity<List<WorkflowResponse>> getWorkflow(@PathVariable Long fspId);

    @PostMapping(URL.WORKFLOW_ACTION)
    @PreAuthorize("hasAuthority('SCOPE_fsp:write')")
    @Operation(summary = "Submit a workflow action for an FSP")
    ResponseEntity<Void> submitWorkflowAction(
            @PathVariable Long fspId, @Valid @RequestBody WorkflowRequest workflowRequest);

    // --- Stocking Standards ---

    @GetMapping(URL.STANDARDS)
    @PreAuthorize("hasAuthority('SCOPE_fsp:read')")
    @Operation(summary = "Get stocking standards for an FSP")
    ResponseEntity<List<StandardRequest>> getStandards(@PathVariable Long fspId);

    @PutMapping(URL.STANDARDS)
    @PreAuthorize("hasAuthority('SCOPE_fsp:write')")
    @Operation(summary = "Replace all stocking standards for an FSP")
    ResponseEntity<List<StandardRequest>> saveStandards(
            @PathVariable Long fspId, @Valid @RequestBody List<StandardRequest> standards);

    @DeleteMapping(URL.STANDARD_BY_ID)
    @PreAuthorize("hasAuthority('SCOPE_fsp:write')")
    @Operation(summary = "Delete a stocking standard")
    ResponseEntity<Void> deleteStandard(
            @PathVariable Long fspId, @PathVariable Long standardId);

    // --- Attachments ---

    @GetMapping(URL.ATTACHMENTS)
    @PreAuthorize("hasAuthority('SCOPE_fsp:read')")
    @Operation(summary = "List attachments for an FSP (metadata only)")
    ResponseEntity<List<AttachmentResponse>> getAttachments(@PathVariable Long fspId);

    @PostMapping(value = URL.ATTACHMENTS, consumes = "multipart/form-data")
    @PreAuthorize("hasAuthority('SCOPE_fsp:write')")
    @Operation(summary = "Upload an attachment for an FSP")
    ResponseEntity<AttachmentResponse> uploadAttachment(
            @PathVariable Long fspId,
            @RequestParam("file") MultipartFile file,
            @RequestParam("typeCode") String typeCode) throws IOException;

    @GetMapping(URL.ATTACHMENT_DOWNLOAD)
    @PreAuthorize("hasAuthority('SCOPE_fsp:read')")
    @Operation(summary = "Download an attachment")
    ResponseEntity<byte[]> downloadAttachment(
            @PathVariable Long fspId, @PathVariable Long attachmentId);

    @DeleteMapping(URL.ATTACHMENT_BY_ID)
    @PreAuthorize("hasAuthority('SCOPE_fsp:write')")
    @Operation(summary = "Delete an attachment")
    ResponseEntity<Void> deleteAttachment(
            @PathVariable Long fspId, @PathVariable Long attachmentId);

    // --- Inbox ---

    @GetMapping(URL.INBOX)
    @PreAuthorize("hasAuthority('SCOPE_fsp:read')")
    @Operation(summary = "Get FSP inbox items for the current user's district")
    ResponseEntity<List<FspSearchResult>> getInbox();

    // --- History ---

    @GetMapping(URL.HISTORY)
    @PreAuthorize("hasAuthority('SCOPE_fsp:read')")
    @Operation(summary = "Get audit history for an FSP")
    ResponseEntity<List<WorkflowResponse>> getHistory(@PathVariable Long fspId);
}
