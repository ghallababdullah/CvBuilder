package io.github.ghallababdullah.cv_builder.resume.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResumeResponse {

    private Long id;
    private Long userId;
    private Long templateId;
    private String templateName;      // удобно для клиента
    private String title;
    private String formData;
    private Boolean isActive;
    private Instant createdAt;
    private Instant updatedAt;
}