package io.github.ghallababdullah.cv_builder.resume;

import io.github.ghallababdullah.cv_builder.resume.dto.CreateResumeRequest;
import io.github.ghallababdullah.cv_builder.resume.dto.ResumeResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/resumes")
@RequiredArgsConstructor
public class ResumeController {

    private final ResumeService resumeService;

    @PostMapping
    public ResponseEntity<ResumeResponse> create(@Valid @RequestBody CreateResumeRequest request) {
        ResumeResponse response = resumeService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<ResumeResponse>> getMyResumes() {
        List<ResumeResponse> resumes = resumeService.getMyResumes();
        return ResponseEntity.ok(resumes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResumeResponse> getById(@PathVariable Long id) {
        ResumeResponse response = resumeService.getById(id);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        resumeService.delete(id);
        return ResponseEntity.noContent().build();
    }
}