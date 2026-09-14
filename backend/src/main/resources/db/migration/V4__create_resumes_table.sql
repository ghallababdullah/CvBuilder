CREATE TABLE resumes (
    id            BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    user_id       BIGINT NOT NULL REFERENCES users(id),
    template_id   BIGINT NOT NULL REFERENCES templates(id),
    title         VARCHAR(255) NOT NULL,
    form_data     TEXT NOT NULL,
    is_active     BOOLEAN NOT NULL DEFAULT true,
    created_at    TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    updated_at    TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_resumes_user_id ON resumes(user_id);
CREATE INDEX idx_resumes_template_id ON resumes(template_id);
CREATE INDEX idx_resumes_is_active ON resumes(is_active);

COMMENT ON TABLE resumes IS 'Резюме пользователей на основе шаблонов';
COMMENT ON COLUMN resumes.user_id IS 'ID владельца резюме (FK на users)';
COMMENT ON COLUMN resumes.template_id IS 'ID используемого шаблона (FK на templates)';
COMMENT ON COLUMN resumes.title IS 'Название резюме (задаёт пользователь)';
COMMENT ON COLUMN resumes.form_data IS 'JSON с данными формы';