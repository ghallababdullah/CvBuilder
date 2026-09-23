package io.github.ghallababdullah.cv_builder.resume;

import io.github.ghallababdullah.cv_builder.resume.dto.CreateResumeRequest;
import io.github.ghallababdullah.cv_builder.resume.dto.ResumeResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/resumes")
@RequiredArgsConstructor
@Tag(name = "Resumes", description = "CRUD operations for user resumes")
@SecurityRequirement(name = "bearerAuth")
public class ResumeController {

    private final ResumeService resumeService;

    @PostMapping
    @Operation(
            summary = "Create a new resume",
            description = "Creates a new resume for the authenticated user. User is taken from JWT token."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Resume created"),
            @ApiResponse(responseCode = "400", description = "Invalid data"),
            @ApiResponse(responseCode = "401", description = "Not authenticated"),
            @ApiResponse(responseCode = "404", description = "Template not found")
    })
    public ResponseEntity<ResumeResponse> create(@Valid @RequestBody CreateResumeRequest request) {
        ResumeResponse response = resumeService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    @Operation(
            summary = "Get all resumes of the current user",
            description = "Returns only resumes owned by the authenticated user (from JWT)."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "List of resumes"),
            @ApiResponse(responseCode = "401", description = "Not authenticated")
    })
    public ResponseEntity<List<ResumeResponse>> getMyResumes() {
        List<ResumeResponse> resumes = resumeService.getMyResumes();
        return ResponseEntity.ok(resumes);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get resume by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Resume found"),
            @ApiResponse(responseCode = "401", description = "Not authenticated"),
            @ApiResponse(responseCode = "403", description = "Not owner of this resume"),
            @ApiResponse(responseCode = "404", description = "Resume not found")
    })
    public ResponseEntity<ResumeResponse> getById(
            @Parameter(description = "Resume ID", example = "1")
            @PathVariable Long id) {
        ResumeResponse response = resumeService.getById(id);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete resume (soft delete)",
            description = "Sets is_active to false. Only owner can delete."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Resume deleted"),
            @ApiResponse(responseCode = "401", description = "Not authenticated"),
            @ApiResponse(responseCode = "403", description = "Not owner of this resume"),
            @ApiResponse(responseCode = "404", description = "Resume not found")
    })
    public ResponseEntity<Void> delete(
            @Parameter(description = "Resume ID", example = "1")
            @PathVariable Long id) {
        resumeService.delete(id);
        return ResponseEntity.noContent().build();
    }
}