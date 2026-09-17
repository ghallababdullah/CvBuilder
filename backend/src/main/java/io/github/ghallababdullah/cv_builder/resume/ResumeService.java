package io.github.ghallababdullah.cv_builder.resume;

import io.github.ghallababdullah.cv_builder.auth.SecurityUtils;
import io.github.ghallababdullah.cv_builder.resume.dto.CreateResumeRequest;
import io.github.ghallababdullah.cv_builder.resume.dto.ResumeResponse;
import io.github.ghallababdullah.cv_builder.template.Template;
import io.github.ghallababdullah.cv_builder.template.TemplateNotFoundException;
import io.github.ghallababdullah.cv_builder.template.TemplateRepository;
import io.github.ghallababdullah.cv_builder.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ResumeService {

    private final ResumeRepository resumeRepository;
    private final TemplateRepository templateRepository;
    private final SecurityUtils securityUtils;

    @Transactional
    public ResumeResponse create(CreateResumeRequest request) {
        User currentUser = securityUtils.getCurrentUser();
        log.info("Creating resume for user {} with template {}",
                currentUser.getEmail(), request.getTemplateId());

        Template template = templateRepository.findById(request.getTemplateId())
                .orElseThrow(() -> new TemplateNotFoundException(request.getTemplateId()));

        Resume resume = new Resume();
        Instant now = Instant.now();

        resume.setUser(currentUser);
        resume.setTemplate(template);
        resume.setTitle(request.getTitle());
        resume.setFormData(request.getFormData());
        resume.setIsActive(true);
        resume.setCreatedAt(now);
        resume.setUpdatedAt(now);

        Resume saved = resumeRepository.save(resume);
        log.info("Resume created with id: {}", saved.getId());

        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<ResumeResponse> getMyResumes() {
        Long userId = securityUtils.getCurrentUserId();
        log.info("Fetching resumes for current user id: {}", userId);

        return resumeRepository.findByUserIdAndIsActiveTrue(userId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public ResumeResponse getById(Long id) {
        log.info("Fetching resume by id: {}", id);
        Resume resume = resumeRepository.findById(id)
                .orElseThrow(() -> new ResumeNotFoundException(id));

        checkOwnership(resume);

        return toResponse(resume);
    }

    @Transactional
    public void delete(Long id) {
        log.info("Soft-deleting resume with id: {}", id);
        Resume resume = resumeRepository.findById(id)
                .orElseThrow(() -> new ResumeNotFoundException(id));

        checkOwnership(resume);

        resume.setIsActive(false);
        resume.setUpdatedAt(Instant.now());
        resumeRepository.save(resume);
    }

    /**
     * Проверяет что текущий user — владелец резюме.
     * Если нет — бросает AccessDeniedException (Spring вернёт 403).
     */
    private void checkOwnership(Resume resume) {
        Long currentUserId = securityUtils.getCurrentUserId();
        if (!resume.getUser().getId().equals(currentUserId)) {
            log.warn("User {} tried to access resume {} owned by user {}",
                    currentUserId, resume.getId(), resume.getUser().getId());
            throw new AccessDeniedException("You don't have access to this resume");
        }
    }

    private ResumeResponse toResponse(Resume r) {
        return new ResumeResponse(
                r.getId(),
                r.getUser().getId(),
                r.getTemplate().getId(),
                r.getTemplate().getName(),
                r.getTitle(),
                r.getFormData(),
                r.getIsActive(),
                r.getCreatedAt(),
                r.getUpdatedAt()
        );
    }
}