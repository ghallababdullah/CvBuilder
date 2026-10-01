package io.github.ghallababdullah.cv_builder.template;

import io.github.ghallababdullah.cv_builder.template.dto.CreateTemplateRequest;
import io.github.ghallababdullah.cv_builder.template.dto.TemplateResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;         // ← ИМПОРТ
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/templates")
@RequiredArgsConstructor
@Tag(name = "Templates", description = "Resume templates management")
@SecurityRequirement(name = "bearerAuth")
public class TemplateController {

    private final TemplateService templateService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")                                    // ← ТОЛЬКО ADMIN
    @Operation(summary = "Create a new template (ADMIN only)")
    public ResponseEntity<TemplateResponse> create(@Valid @RequestBody CreateTemplateRequest request) {
        TemplateResponse response = templateService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    @Operation(summary = "Get all active templates")
    public ResponseEntity<List<TemplateResponse>> getAll() {
        return ResponseEntity.ok(templateService.getAllActive());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get template by ID")
    public ResponseEntity<TemplateResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(templateService.getById(id));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")                                    // ← ТОЛЬКО ADMIN
    @Operation(summary = "Delete template — soft delete (ADMIN only)")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        templateService.delete(id);
        return ResponseEntity.noContent().build();
    }
}