-- 1. Crear la base de datos
CREATE DATABASE IF NOT EXISTS mak_it_db;
USE mak_it_db;

-- 2. Tabla de Usuarios
CREATE TABLE usuarios (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL, -- Aquí se guardará el hash de BCrypt
    hora_aviso TIME DEFAULT '09:00:00', -- Para la futura funcionalidad de alertas
    fecha_registro DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- 3. Tabla de Categorías (Intereses)
CREATE TABLE categorias (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL,
    descripcion TEXT
);

-- 4. Tabla de Catálogo de Retos
CREATE TABLE retos_catalogo (
    id INT AUTO_INCREMENT PRIMARY KEY,
    categoria_id INT,
    titulo VARCHAR(100) NOT NULL,
    descripcion TEXT,
    CONSTRAINT fk_categoria FOREIGN KEY (categoria_id) 
        REFERENCES categorias(id) ON DELETE CASCADE
);

-- 5. Tabla Intermedia: Intereses del Usuario
CREATE TABLE user_intereses (
    user_id INT,
    categoria_id INT,
    PRIMARY KEY (user_id, categoria_id),
    CONSTRAINT fk_user_int FOREIGN KEY (user_id) REFERENCES usuarios(id),
    CONSTRAINT fk_cat_int FOREIGN KEY (categoria_id) REFERENCES categorias(id)
);

-- 6. Tabla de Progreso (Reto Diario y Check-in)
CREATE TABLE progreso_diario (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    reto_id INT,
    fecha DATE NOT NULL,
    completado BOOLEAN DEFAULT FALSE,
    CONSTRAINT fk_user_prog FOREIGN KEY (user_id) REFERENCES usuarios(id),
    CONSTRAINT fk_reto_prog FOREIGN KEY (reto_id) REFERENCES retos_catalogo(id)
);