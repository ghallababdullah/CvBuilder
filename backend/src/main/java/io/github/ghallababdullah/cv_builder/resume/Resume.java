package io.github.ghallababdullah.cv_builder.resume;


import io.github.ghallababdullah.cv_builder.template.Template;
import io.github.ghallababdullah.cv_builder.user.User;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;


@Entity
@Table(name= "resumes")
@Data
@NoArgsConstructor
public class Resume {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id ;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user ;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "template_id", nullable = false)
    private Template template ;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "form_data", nullable = false, columnDefinition = "TEXT")
    private String formData;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;



}
