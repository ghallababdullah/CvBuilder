package io.github.ghallababdullah.cv_builder.resume;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ResumeRepository extends JpaRepository<Resume,Long> {

    List<Resume> findByUserIdAndIsActiveTrue(Long userId);

    List<Resume> findByIsActiveTrue();
}
