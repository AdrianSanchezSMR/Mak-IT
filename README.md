# Make-IT

## Backend desplegado

El backend apunta directamente a la base de datos RDS de Mak-IT desde `app/src/main/resources/application.properties`.

Para compilar la app Android contra el backend desplegado, define `MAKIT_API_BASE_URL` en `Front_MaKit/local.properties`, como propiedad Gradle o como variable de entorno.
