<p align="center">
  <img src="./makit_logo.png" alt="Mak-IT Logo" width="160" />
</p>

# Mak-IT

Mak-IT es una aplicacion de habitos y retos diarios pensada para ayudar al usuario a mantener constancia mediante retos cortos, seguimiento del progreso y una experiencia visual sencilla tanto en movil como en backend.

El proyecto esta dividido en dos partes:
- `app/`: backend en Spring Boot.
- `Front_MaKit/`: app Android en Jetpack Compose.

## Web de descarga

La APK del proyecto tambien se puede descargar desde la web oficial:

https://sram505.github.io/Makit_web/

En esa pagina encontraras una presentacion visual de Mak-IT y un boton para descargar la APK publicada en la web. El archivo se baja directamente desde la propia pagina, sin pasos extra.

## Que hace la aplicacion

La app permite:
- registrarse e iniciar sesion
- seleccionar categorias preferidas
- recibir retos diarios aleatorios segun esas categorias
- marcar retos como completados
- consultar estadisticas y racha
- crear nuevos retos de catalogo
- gestionar perfil e intereses

El sistema esta pensado para que cada usuario tenga una experiencia personalizada. Los retos se asignan en funcion de las categorias que tenga activas y se guardan en un historial diario.

## Arquitectura general

La arquitectura es de tipo cliente-servidor:

- La app Android consume la API REST del backend.
- El backend Spring Boot expone los endpoints.
- La base de datos es MySQL alojada en Amazon RDS.
- El backend desplegado esta en AWS Elastic Beanstalk.

Flujo general:

1. El usuario inicia sesion en la app Android.
2. El front obtiene un token JWT.
3. Las peticiones posteriores llevan ese token en la cabecera `Authorization`.
4. El backend valida el token y accede a la base de datos.
5. El usuario recibe retos, historial y estadisticas.

## Frontend Android

El frontend esta hecho con:
- Kotlin
- Jetpack Compose
- Retrofit
- OkHttp
- Coil

### Conexion del front al backend

La app apunta por defecto al backend desplegado en AWS:

- `http://mak-it-backend-env.eba-2m3irqqv.us-east-1.elasticbeanstalk.com/`

La URL se inyecta mediante `BuildConfig.API_BASE_URL`, definida en `Front_MaKit/app/build.gradle.kts`.

Si se quiere sobrescribir para desarrollo o pruebas, se puede usar:
- variable de entorno `MAKIT_API_BASE_URL`
- propiedad Gradle `MAKIT_API_BASE_URL`
- `local.properties`

## Backend Spring Boot

El backend esta hecho con:
- Spring Boot
- Spring Security
- Spring Data JPA
- MySQL
- JWT
- Hibernate

### Conexion a la base de datos

El backend usa MySQL en Amazon RDS.

La configuracion se encuentra en:
- [`app/src/main/resources/application.properties`](./app/src/main/resources/application.properties)

Ahí se define:
- URL de conexion JDBC
- usuario de la base de datos
- contraseña
- dialecto de Hibernate
- puerto del servidor
- secreto JWT

### Despliegue en AWS

El backend se despliega en:
- Elastic Beanstalk

La plataforma utilizada es:
- Java
- Corretto 17
- Amazon Linux 2023

El artefacto de despliegue es el `.jar` generado con Maven, empaquetado despues en `.zip` para Elastic Beanstalk.

## Autenticacion

Mak-IT usa JWT.

Funcionamiento:
- el usuario inicia sesion con nombre de usuario y contrasena
- el backend devuelve un token JWT
- la app lo guarda localmente
- las peticiones protegidas envian `Authorization: Bearer <token>`

## Logica de retos

Los retos diarios se guardan en la tabla de progreso diario. La app trabaja con tres conceptos:
- reto del dia
- retos pendientes
- retos completados

Ademas:
- los retos aleatorios se generan entre las categorias preferidas del usuario
- un reto puede volver a salir en otro dia
- la racha diaria se calcula a partir del historial de progreso
- el sistema evita ensenar errores tecnicos al usuario final y prioriza mensajes de interfaz

## Endpoints del backend

### Salud

#### `GET /api/health`
Comprueba que el backend esta arrancado.

Respuesta esperada:
- estado del servidor
- mensaje de confirmacion

---

### Autenticacion

#### `POST /api/auth/register`
Registra un usuario nuevo.

Campos principales:
- `username`
- `email`
- `password`
- `horaAviso` opcional

#### `POST /api/auth/login`
Inicia sesion y devuelve un JWT.

#### `GET /api/auth/me`
Devuelve la identidad del usuario autenticado.

---

### Perfil de usuario

#### `GET /api/users/me/profile`
Obtiene el perfil del usuario actual.

#### `PUT /api/users/me/profile`
Actualiza datos de perfil:
- nombre de usuario
- email
- hora de aviso

#### `PUT /api/users/me/password`
Cambia la contrasena del usuario.

---

### Categorias e intereses

#### `GET /api/categorias`
Lista todas las categorias existentes.

#### `GET /api/users/me/interests`
Devuelve las categorias preferidas activas del usuario.

#### `PUT /api/users/me/interests`
Guarda las categorias preferidas activas del usuario.

---

### Retos

#### `GET /api/retos`
Lista los retos activos del catalogo.

#### `POST /api/retos`
Crea un nuevo reto de catalogo.

#### `GET /api/retos/aleatorio`
Devuelve un reto aleatorio del catalogo.

Parametro opcional:
- `categoriaId`

#### `POST /api/challenges/random`
Asigna un reto aleatorio de hoy al usuario autenticado.

Se usa desde la pantalla principal cuando el usuario pide un nuevo reto aleatorio.

#### `GET /api/challenges/today`
Devuelve el reto del dia del usuario.

#### `GET /api/challenges/today/all`
Devuelve todos los retos pendientes o activos de hoy.

#### `GET /api/challenges/mine`
Devuelve el historial de retos del usuario.

#### `PUT /api/challenges/{id}/checkin`
Marca un reto como completado.

---

### Progreso

#### `GET /api/users/me/progress/summary`
Devuelve el resumen de progreso del usuario:
- total asignados
- total completados
- total pendientes
- tasa de completado
- serie diaria

## Scheduler automatico

El backend incluye un proceso programado que revisa periodicamente a los usuarios con `horaAviso` configurada y genera el reto diario cuando llega la hora correspondiente.

Esto permite que el reto aparezca de forma automatica sin depender de abrir manualmente la app.

## Como ejecutar el proyecto en local

### Backend

1. Configurar MySQL o usar RDS.
2. Ajustar `app/src/main/resources/application.properties` o variables de entorno.
3. Ejecutar:
   ```bash
   .\\mvnw.cmd spring-boot:run
   ```
   o generar el `.jar` con:
   ```bash
   .\\mvnw.cmd clean package
   ```

### Frontend Android

1. Abrir `Front_MaKit` en Android Studio.
2. Asegurarse de que `MAKIT_API_BASE_URL` apunte al backend deseado.
3. Ejecutar la app en emulador o dispositivo.

## Build de produccion del backend

Para generar el `.jar`:

```bash
cd app
.\\mvnw.cmd clean package -DskipTests
```

El artefacto final queda en:
- `app/target/app-0.0.1-SNAPSHOT.jar`

## Despliegue en AWS

### Elastic Beanstalk

El backend se despliega como aplicacion Java SE:
- plataforma `Java`
- rama `Corretto 17 running on 64bit Amazon Linux 2023`
- bundle `.zip` con el `.jar` en la raiz

### Amazon RDS

La base de datos MySQL se conecta mediante el endpoint RDS configurado en el backend.

Importante:
- RDS debe permitir trafico desde el security group de Elastic Beanstalk
- el puerto de MySQL es `3306`

## Estructura de carpetas

- `app/`
  - backend Spring Boot
- `Front_MaKit/`
  - app Android
- `makit_logo.png`
  - logo principal del proyecto

## Notas tecnicas

- El front usa `Retrofit` + `OkHttp` para consumir la API.
- El backend usa `Spring Security` con JWT.
- El progreso y la racha se calculan a partir del historial de retos.
- Los retos diarios se guardan en la tabla de progreso, permitiendo historial por fecha.
- La app visualiza categorias y retos con iconos y colores consistentes.

## Estado actual

El proyecto esta preparado para:
- app Android apuntando al backend de AWS
- backend desplegado en Elastic Beanstalk
- persistencia en RDS
- login, retos diarios, intereses, historial y estadisticas
