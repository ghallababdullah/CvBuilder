package io.github.ghallababdullah.cv_builder.template;

import io.github.ghallababdullah.cv_builder.template.dto.CreateTemplateRequest;
import io.github.ghallababdullah.cv_builder.template.dto.TemplateResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j

public class TemplateService {
    private final TemplateRepository templateRepository ;
    public TemplateResponse create(CreateTemplateRequest request){
        log.info("Creating template with name: {}", request.getName());
        Template template = new Template();
        Instant now = Instant.now();


        template.setName(request.getName());
        template.setDescription(request.getDescription());
        template.setCategory(request.getCategory());
        template.setTexContent(request.getTexContent());
        template.setIsActive(true);
        template.setCreatedAt(now);
        template.setUpdatedAt(now);

        Template saved = templateRepository.save(template) ;

        return toResponse(saved) ;


    }

    public List<TemplateResponse> getAllActive(){
        log.info("Fetching all active templates");
        return templateRepository.findByIsActiveTrue()
                .stream()
                .map(this::toResponse)
                .toList();



    }

    public TemplateResponse getById(Long id) {
        log.info("Fetching template by id: {}", id);
        Template template = templateRepository.findById(id)
                .orElseThrow(() -> new TemplateNotFoundException(id));
        return toResponse(template);
    }
    public void delete(Long id) {
        log.info("Soft-deleting template with id: {}", id);
        Template template = templateRepository.findById(id)
                .orElseThrow(() -> new TemplateNotFoundException(id));
        template.setIsActive(false);
        template.setUpdatedAt(Instant.now());
        templateRepository.save(template);
    }

    // Приватный helper для маппинга Entity → DTO
    private TemplateResponse toResponse(Template t) {
        return new TemplateResponse(
                t.getId(),
                t.getName(),
                t.getDescription(),
                t.getCategory(),
                t.getTexContent(),
                t.getIsActive(),
                t.getCreatedAt(),
                t.getUpdatedAt()
        );
    }
}
