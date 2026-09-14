package io.github.ghallababdullah.cv_builder.resume;

public class ResumeNotFoundException extends RuntimeException {
    public ResumeNotFoundException(long id) {
        super("Resume with id " + id + " not found");
    }
}
