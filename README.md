# Taller01-Servidor-Web-AREP

En este taller, se reviso y implemento el funcionamiento de un servidor web capaz de manejar multiples solicitudes no concurrentes. La idea principal es que el servidor lea los archivos desde el disco local y este responda con los archivos solicitados por el documento, como:

Paginas HTML
Archivos JavaScript
Formatos CSS
Imagenes

## Descripcion del aplicativo

La aplicacion web se diseño con la idea de explorar y gestionar los archivos. La idea es proporcionar una interfaz de facil uso la cual permita interactuar con elementos de JavaScript, CSS, HTML y imagenes de forma eficaz.

## Arquitectura

- Usuario:
Es aquel que realizara las solicitudes HTTP

- Navegador:

Es el "mensajero" entre el el usuarios y el servidor.

- Servidor HTTP

Es el servidor que procesa las solicitudes HTTP dadas por el navegador, este puede tener una infraestructura mas amplia.

El navegador envia las solicitudes HTTP al servidor por el puerto 35000 para diferentes rutas:

- ./code.js: Archivo de JavaScript.
- ./index.html: Cargar el archivo principal de la pagina web.
- ./format.css: Archivo de los estilos CSS.
- ./PNG/parrot.JPG: Obtener una imagen en una ruta especifica.

El servidor procesa estas solicitudes y da los archivos correspondientes desde el sistema propio de archivos.

<img width="1536" height="1024" alt="image" src="https://github.com/user-attachments/assets/f04df1d2-fa9e-4356-a765-7571eb8e662c" />

En ese mismo orden, el usuario solicita contenido al servidor, de ahi el sistema de construcción (Maven) compila y empaqueta la aplicacion del servidor, se verifica que el servidor funcione correctamente con el sistema de pruebas, en este punto se inicializa y se gestiona el ciclo de vida del servidor, procesando las solicitudes y envia las respuestas correspondientes de los archivos HTML, CSS, JavaScript e Imagenes.

## Instruciones de uso

Con estas instrucciones se puede obtener una copia del servidor en la maquina local con la finalidad de desarrollar y probar el msimo.

# Tecnologias usadas

- Maven como gestionar de dependencias y automatizacion de construccion para Java.
- JavaScript como lenguaje de programacion para interactividad en la web.
- Java como lenguaje de programacion fuerte para aplicaciones empresariales y backend.

# Instalación

Con esto se clona el proyecto en la maquina local:

```
git clone https://github.com/ManuelB16/Taller01-Servidor-Web-AREP
cd Taller01-Servidor-Web-AREP
git checkout Taller01-Servidor-Web-AREP
mvn clean compile
```

# Ejecución de la aplicación

Para inicializar la aplicacion se usa:

```
mvn exec:java -Dexec.mainClass="Taller1.server.SimpleHttpServer"
```

Con esto se limpiara cualquier construccion previa, se compilará y empaquetará en un jar para poder ejecutar la aplicación.

## Pruebas

Para ejecutar las pruebas se usa el siguiente:

```
mvn exec:java -Dexec.mainClass="Taller1.server.SimpleHttpServer"
```
<img width="1463" height="539" alt="image" src="https://github.com/user-attachments/assets/091a61fe-23c9-44c9-8ff3-2015b8f05f32" />

## Descripcion de las pruebas

- testGetApiSaludo
 
Verifica que la solicitud GET a la ruta /api/saludo responda con código HTTP 200 OK y contenga el mensaje esperado "¡Hola desde el servidor!".

- testGetApiFecha
 
Valida que la solicitud GET a /api/fecha devuelva código HTTP 200 OK y que la respuesta incluya la palabra "fecha".

- testGetApiNoEncontrada

Comprueba que una solicitud GET a una ruta inexistente (/api/nada) devuelva código HTTP 404 Not Found y contenga el texto "Recurso no encontrado".

- testPostApiEnviar

Evalúa que una solicitud POST a /api/enviar con un cuerpo JSON como {"nombre":"Manuel"} sea procesada correctamente, respondiendo con HTTP 200 OK y el mensaje "Datos recibidos".

- testPostApiNoEncontrada

Confirma que una solicitud POST a una ruta inexistente (/api/otro) devuelva código HTTP 404 Not Found.

## Caracteristicas iniciales

1. Interfaz

* Diseño minimalista con esquema de colores suaves en tono pastel.
* Diseño responsivo

2. Gestión de archivos:

* Botones interactivos para abrir y visualizar archivos claves como:
* JavaScript (code.js)
* CSS (format.css)
* HTML (index.html)
* Imágenes (parrot.jpg)

** Muestra del aplicativo

<img width="771" height="711" alt="image" src="https://github.com/user-attachments/assets/2ff16708-a092-4997-9dfc-726a31fd3608" />

## Autor

* Manuel Felipe Barrera
