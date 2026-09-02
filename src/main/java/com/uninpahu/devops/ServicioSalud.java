package com.uninpahu.devops;

import com.sun.net.httpserver.HttpServer;
import java.io.OutputStream;
import java.net.InetSocketAddress;

public class ServicioSalud {

    public static void main(String[] args) throws Exception {

        HttpServer server = HttpServer.create(
                new InetSocketAddress(9090), 0
        );

        server.createContext("/salud", exchange -> {

            String response = "OK";

            exchange.sendResponseHeaders(
                    200,
                    response.getBytes().length
            );

            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        });

        server.setExecutor(null);
        server.start();

        System.out.println("Servicio de salud iniciado en :9090");
    }
}
