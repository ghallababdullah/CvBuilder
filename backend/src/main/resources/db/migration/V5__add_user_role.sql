-- Add role column to users table
ALTER TABLE users
    ADD COLUMN role VARCHAR(20) NOT NULL DEFAULT 'USER';

-- Constraint: role must be one of allowed values
ALTER TABLE users
    ADD CONSTRAINT chk_users_role CHECK (role IN ('USER', 'ADMIN'));

-- Create index for faster role queries
CREATE INDEX idx_users_role ON users(role);

-- Promote existing user (abdullah) to admin
UPDATE users SET role = 'ADMIN' WHERE email = 'abdullah@example.com';

COMMENT ON COLUMN users.role IS 'User role: USER (default) or ADMIN';