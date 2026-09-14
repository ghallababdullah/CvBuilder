package io.github.ghallababdullah.cv_builder.resume;

import io.github.ghallababdullah.cv_builder.resume.dto.CreateResumeRequest;
import io.github.ghallababdullah.cv_builder.resume.dto.ResumeResponse;
import io.github.ghallababdullah.cv_builder.template.Template;
import io.github.ghallababdullah.cv_builder.template.TemplateNotFoundException;
import io.github.ghallababdullah.cv_builder.template.TemplateRepository;
import io.github.ghallababdullah.cv_builder.user.User;
import io.github.ghallababdullah.cv_builder.user.UserNotFoundException;
import io.github.ghallababdullah.cv_builder.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ResumeService {

    private final ResumeRepository resumeRepository;
    private final UserRepository userRepository;
    private final TemplateRepository templateRepository;

    @Transactional
    public ResumeResponse create(CreateResumeRequest request) {
        log.info("Creating resume for user {} with template {}",
                request.getUserId(), request.getTemplateId());

        // Проверяем что user существует
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new UserNotFoundException(request.getUserId()));

        // Проверяем что template существует
        Template template = templateRepository.findById(request.getTemplateId())
                .orElseThrow(() -> new TemplateNotFoundException(request.getTemplateId()));

        Resume resume = new Resume();
        Instant now = Instant.now();

        resume.setUser(user);
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
    public List<ResumeResponse> getAllActive() {
        log.info("Fetching all active resumes");
        return resumeRepository.findByIsActiveTrue()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ResumeResponse> getByUserId(Long userId) {
        log.info("Fetching resumes for user: {}", userId);
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
        return toResponse(resume);
    }

    @Transactional
    public void delete(Long id) {
        log.info("Soft-deleting resume with id: {}", id);
        Resume resume = resumeRepository.findById(id)
                .orElseThrow(() -> new ResumeNotFoundException(id));
        resume.setIsActive(false);
        resume.setUpdatedAt(Instant.now());
        resumeRepository.save(resume);
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