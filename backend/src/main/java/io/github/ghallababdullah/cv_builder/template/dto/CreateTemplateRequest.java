package io.github.ghallababdullah.cv_builder.template.dto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateTemplateRequest {

    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 255, message = "Name must be between 2 and 255 characters")
    private String name;

    @Size(max = 1000, message = "Description too long")
    private String description;

    @NotBlank(message = "Category is required")
    @Size(max = 100, message = "Category too long")
    private String category;

    @NotBlank(message = "TeX content is required")
    private String texContent;
}