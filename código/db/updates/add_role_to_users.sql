-- Agregar campo role a la tabla users
ALTER TABLE users ADD COLUMN role TEXT NOT NULL DEFAULT 'USER';