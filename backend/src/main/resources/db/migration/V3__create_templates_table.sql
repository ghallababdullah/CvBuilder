CREATE TABLE templates (
    id            BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name          VARCHAR(255) NOT NULL,
    description   TEXT,
    category      VARCHAR(100) NOT NULL,
    tex_content   TEXT NOT NULL,
    is_active     BOOLEAN NOT NULL DEFAULT true,
    created_at    TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    updated_at    TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_templates_category ON templates(category);
CREATE INDEX idx_templates_is_active ON templates(is_active);

COMMENT ON TABLE templates IS 'Шаблоны CV в формате LaTeX';
COMMENT ON COLUMN templates.name IS 'Название шаблона';
COMMENT ON COLUMN templates.description IS 'Описание шаблона (необязательно)';
COMMENT ON COLUMN templates.category IS 'Категория: modern, classic, creative, academic';
COMMENT ON COLUMN templates.tex_content IS 'LaTeX-код шаблона';
COMMENT ON COLUMN templates.is_active IS 'Активен ли шаблон (для soft delete)';