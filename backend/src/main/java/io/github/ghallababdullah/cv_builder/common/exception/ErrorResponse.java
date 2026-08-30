package io.github.ghallababdullah.cv_builder.common.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;
// DTO для стандартного ответа при ошибке. Все ошибки будут возвращаться в этом формате
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {
    private Instant timestamp;
    private int status;
    private String error;
    private String message;
    private String path;
    private List<FieldError> fieldErrors;
    @Data
    @AllArgsConstructor
    //для случая когда несколько полей невалидны. Один класс внутри другого — это static nested class.
    public static class FieldError {
        private String field;
        private String message;
    }


}
