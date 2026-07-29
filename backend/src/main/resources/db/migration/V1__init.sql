CREATE TABLE IF NOT EXISTS _flyway_check (
    id SERIAL PRIMARY KEY,
    created_at TIMESTAMP DEFAULT NOW()
);


INSERT INTO _flyway_check (id) VALUES (DEFAULT);
