package Taller1.server;

import java.io.*;
import java.net.*;
import java.nio.file.*;
import java.util.*;

/**
 * 
 * Autor: Manuel Barrera
 */
public class SimpleHttpServer {

    private static final int PUERTO = 35000;
    private static final String BASE_DIR = "src/main/resources";

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PUERTO)) {
            System.out.println("Servidor escuchando en el puerto " + PUERTO);

            while (true) {
                try (Socket cliente = serverSocket.accept()) {
                    procesarSolicitud(cliente);
                }
            }
        } catch (IOException e) {
            System.err.println("Error al iniciar el servidor: " + e.getMessage());
        }
    }

    /**
     * Procesa la solicitud HTTP recibida de un cliente.
     */
    private static void procesarSolicitud(Socket cliente) {
        try (
            BufferedReader entrada = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
            OutputStream salidaBytes = cliente.getOutputStream();
            PrintWriter salidaTexto = new PrintWriter(salidaBytes, true)
        ) {
            String lineaInicial = entrada.readLine();
            if (lineaInicial == null || lineaInicial.isBlank()) return;

            System.out.println("Solicitud: " + lineaInicial);
            String[] partes = lineaInicial.split(" ");
            String metodo = partes[0];
            String ruta = partes[1];

            if ("GET".equals(metodo)) {
                if (ruta.startsWith("/api")) {
                    manejarApiGet(ruta, salidaTexto);
                } else {
                    servirArchivoEstatico(ruta, salidaTexto, salidaBytes);
                }
            } else if ("POST".equals(metodo) && ruta.startsWith("/api")) {
                manejarApiPost(ruta, entrada, salidaTexto);
            } else {
                enviarRespuesta(salidaTexto, 405, "Method Not Allowed", "{\"error\":\"Método no permitido\"}");
            }

        } catch (IOException e) {
            System.err.println("Error procesando la solicitud: " + e.getMessage());
        }
    }

    /**
     * Devuelve un archivo desde el directorio base.
     */
    private static void servirArchivoEstatico(String ruta, PrintWriter salidaTexto, OutputStream salidaBytes) throws IOException {
        String rutaArchivo = BASE_DIR + ("/".equals(ruta) ? "/index.html" : ruta);
        File archivo = new File(rutaArchivo);

        if (archivo.exists() && !archivo.isDirectory()) {
            String tipoContenido = Files.probeContentType(archivo.toPath());
            byte[] datos = Files.readAllBytes(archivo.toPath());

            salidaTexto.println("HTTP/1.1 200 OK");
            salidaTexto.println("Content-Type: " + tipoContenido);
            salidaTexto.println("Content-Length: " + datos.length);
            salidaTexto.println();
            salidaTexto.flush();

            salidaBytes.write(datos);
            salidaBytes.flush();
        } else {
            enviarRespuesta(salidaTexto, 404, "Not Found", "{\"error\":\"Archivo no encontrado\"}");
        }
    }

    /**
     * Maneja solicitudes GET de la API.
     */
    private static void manejarApiGet(String ruta, PrintWriter salidaTexto) {
        switch (ruta) {
            case "/api/saludo":
                enviarRespuesta(salidaTexto, 200, "OK", "{\"mensaje\":\"¡Hola desde el servidor!\"}");
                break;
            case "/api/fecha":
                enviarRespuesta(salidaTexto, 200, "OK", "{\"fecha\":\"" + new Date() + "\"}");
                break;
            default:
                enviarRespuesta(salidaTexto, 404, "Not Found", "{\"error\":\"Recurso no encontrado\"}");
        }
    }

    /**
     * Maneja solicitudes POST de la API.
     */
    private static void manejarApiPost(String ruta, BufferedReader entrada, PrintWriter salidaTexto) throws IOException {
        if (!ruta.equals("/api/enviar")) {
            enviarRespuesta(salidaTexto, 404, "Not Found", "{\"error\":\"Recurso no encontrado\"}");
            return;
        }
        int contentLength = 0;
        String linea;
        while (!(linea = entrada.readLine()).isEmpty()) {
            if (linea.startsWith("Content-Length:")) {
                contentLength = Integer.parseInt(linea.replace("Content-Length:", "").trim());
            }
        }
        char[] buffer = new char[contentLength];
        entrada.read(buffer);
        String cuerpo = new String(buffer);

        enviarRespuesta(salidaTexto, 200, "OK", "{\"mensaje\":\"Datos recibidos: " + cuerpo + "\"}");
    }

    /**
     * Envía una respuesta HTTP en formato JSON.
     */
    private static void enviarRespuesta(PrintWriter salidaTexto, int codigo, String mensaje, String cuerpo) {
        salidaTexto.printf("HTTP/1.1 %d %s%n", codigo, mensaje);
        salidaTexto.println("Content-Type: application/json");
        salidaTexto.println("Content-Length: " + cuerpo.getBytes().length);
        salidaTexto.println();
        salidaTexto.println(cuerpo);
        salidaTexto.flush();
    }
}
