package Taller1.server;

import org.junit.jupiter.api.*;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SimpleHttpServerTest {

    private static Thread serverThread;

    @BeforeAll
    static void iniciarServidor() {
        serverThread = new Thread(() -> SimpleHttpServer.main(new String[]{}));
        serverThread.setDaemon(true); // Para que se detenga con el proceso de pruebas
        serverThread.start();

        // Espera para que el servidor levante
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ignored) {}
    }

    private String leerRespuesta(HttpURLConnection conexion) throws IOException {
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(conexion.getInputStream(), StandardCharsets.UTF_8))) {
            StringBuilder sb = new StringBuilder();
            String linea;
            while ((linea = br.readLine()) != null) {
                sb.append(linea);
            }
            return sb.toString();
        }
    }

    @Test
    @Order(1)
    void testGetApiSaludo() throws IOException {
        HttpURLConnection conexion = (HttpURLConnection) new URL("http://localhost:35000/api/saludo").openConnection();
        conexion.setRequestMethod("GET");

        assertEquals(200, conexion.getResponseCode());
        String respuesta = leerRespuesta(conexion);
        assertTrue(respuesta.contains("¡Hola desde el servidor!"));
    }

    @Test
    @Order(2)
    void testGetApiFecha() throws IOException {
        HttpURLConnection conexion = (HttpURLConnection) new URL("http://localhost:35000/api/fecha").openConnection();
        conexion.setRequestMethod("GET");

        assertEquals(200, conexion.getResponseCode());
        String respuesta = leerRespuesta(conexion);
        assertTrue(respuesta.contains("fecha"));
    }

    @Test
@Order(3)
void testGetApiNoEncontrada() throws IOException {
    HttpURLConnection conexion = (HttpURLConnection) new URL("http://localhost:35000/api/nada").openConnection();
    conexion.setRequestMethod("GET");

    int codigo = conexion.getResponseCode();
    assertEquals(404, codigo);

    InputStream is = (codigo >= 400) ? conexion.getErrorStream() : conexion.getInputStream();
    StringBuilder respuesta = new StringBuilder();

    try (BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
        String linea;
        while ((linea = br.readLine()) != null) {
            respuesta.append(linea);
        }
    }

    assertTrue(respuesta.toString().contains("Recurso no encontrado"));
}



    @Test
    @Order(4)
    void testPostApiEnviar() throws IOException {
        HttpURLConnection conexion = (HttpURLConnection) new URL("http://localhost:35000/api/enviar").openConnection();
        conexion.setRequestMethod("POST");
        conexion.setDoOutput(true);

        String datos = "{\"nombre\":\"Manuel\"}";
        byte[] out = datos.getBytes(StandardCharsets.UTF_8);
        conexion.setFixedLengthStreamingMode(out.length);
        conexion.setRequestProperty("Content-Type", "application/json; charset=UTF-8");

        try (OutputStream os = conexion.getOutputStream()) {
            os.write(out);
        }

        assertEquals(200, conexion.getResponseCode());
        String respuesta = leerRespuesta(conexion);
        assertTrue(respuesta.contains("Datos recibidos"));
    }

    @Test
    @Order(5)
    void testPostApiNoEncontrada() throws IOException {
        HttpURLConnection conexion = (HttpURLConnection) new URL("http://localhost:35000/api/otro").openConnection();
        conexion.setRequestMethod("POST");
        conexion.setDoOutput(true);

        byte[] out = "{}".getBytes(StandardCharsets.UTF_8);
        conexion.setFixedLengthStreamingMode(out.length);
        conexion.setRequestProperty("Content-Type", "application/json; charset=UTF-8");

        try (OutputStream os = conexion.getOutputStream()) {
            os.write(out);
        }

        assertEquals(404, conexion.getResponseCode());
    }
}
