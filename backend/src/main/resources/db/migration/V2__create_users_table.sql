Create Table users(
     id BIGSERIAL PRIMARY KEY,
     email VARCHAR(255) NOT NULL UNIQUE,
     password_hash VARCHAR(255) NOT NULL ,
     full_name VARCHAR(255) NOT NULL ,
     created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW() ,
     updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW()

);


COMMENT ON TABLE users IS 'Таблица для хранения учетных записей пользователей';
COMMENT ON COLUMN users.id IS 'Уникальный автоинкрементный идентификатор пользователя';
COMMENT ON COLUMN users.email IS 'Электронная почта пользователя (используется как логин)';
COMMENT ON COLUMN users.password_hash IS 'Хэш пароля (например, Bcrypt/Argon2)';
COMMENT ON COLUMN users.full_name IS 'Имя и фамилия пользователя';
COMMENT ON COLUMN users.created_at IS 'Дата и время регистрации аккаунта';
COMMENT ON COLUMN users.updated_at IS 'Дата и время последнего обновления профиля';

