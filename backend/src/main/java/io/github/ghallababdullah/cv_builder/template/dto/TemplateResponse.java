package io.github.ghallababdullah.cv_builder.template.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TemplateResponse {

    private Long id;
    private String name;
    private String description;
    private String category;
    private String texContent;
    private Boolean isActive;
    private Instant createdAt;
    private Instant updatedAt;
}