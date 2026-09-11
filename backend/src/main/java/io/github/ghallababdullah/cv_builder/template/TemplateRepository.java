package io.github.ghallababdullah.cv_builder.template;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TemplateRepository extends JpaRepository<Template, Long> {

    List<Template> findByIsActiveTrue();

    List<Template> findByCategoryAndIsActiveTrue(String category);
}
