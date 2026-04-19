CREATE DATABASE IF NOT EXISTS make_it_db;
USE make_it_db;

# Usuarios
CREATE TABLE usuarios (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    passwd VARCHAR(255) NOT NULL,
    fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

# Grupos
CREATE TABLE grupos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre_grupo VARCHAR(100) NOT NULL,
    descripcion TEXT,
    codigo_acceso VARCHAR(10) NOT NULL UNIQUE,
    admin_id BIGINT NOT NULL,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_grupo_admin FOREIGN KEY (admin_id) REFERENCES usuarios(id) ON DELETE CASCADE
);

# Retos individuales y grupales
CREATE TABLE retos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    titulo VARCHAR(150) NOT NULL,
    descripcion TEXT,
    tipo ENUM('INDIVIDUAL', 'GRUPAL') NOT NULL,
    hora_aviso TIME, #Para las notificaciones programadas
    grupo_id BIGINT DEFAULT NULL, #NULL si es un reto personal
    usuario_id BIGINT NOT NULL, #Creador del reto
    CONSTRAINT fk_reto_grupo FOREIGN KEY (grupo_id) REFERENCES grupos(id) ON DELETE SET NULL,
    CONSTRAINT fk_reto_usuario FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE
);

# Participación (Dudoso de implementarlo)
CREATE TABLE participacion (
    usuario_id BIGINT NOT NULL,
    grupo_id BIGINT NOT NULL,
    fecha_union TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (usuario_id, grupo_id),
    CONSTRAINT fk_part_usuario FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE,
    CONSTRAINT fk_part_grupo FOREIGN KEY (grupo_id) REFERENCES grupos(id) ON DELETE CASCADE
);

# Progreso
CREATE TABLE progreso (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    usuario_id BIGINT NOT NULL,
    reto_id BIGINT NOT NULL,
    fecha DATE NOT NULL, #Fecha del check-in
    completado BOOLEAN DEFAULT FALSE,
    fecha_registro_log TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_progreso_usuario FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE,
    CONSTRAINT fk_progreso_reto FOREIGN KEY (reto_id) REFERENCES retos(id) ON DELETE CASCADE,
    #Evita que un usuario haga check-in dos veces en el mismo reto el mismo día
    UNIQUE KEY unique_daily_checkin (usuario_id, reto_id, fecha)
);
