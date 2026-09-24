package io.github.ghallababdullah.cv_builder.resume.dto;


import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateResumeRequest {
    @Size (min = 1, max = 255, message = "Title must be between 1 and 255 characters")
    private String title;


    private String formData;

}
