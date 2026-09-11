package io.github.ghallababdullah.cv_builder.template;

public class TemplateNotFoundException extends RuntimeException {
    public TemplateNotFoundException(Long id) {
        super("Template with id " + id + " not found");
    }
}
