package ylc.appier.challenge;

import java.net.InetSocketAddress;
import com.sun.net.httpserver.HttpServer;

import ylc.appier.challenge.http.HttpRequestHandler;

public class Main{
    public static void main(String[] args) throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(80), 0);
        server.createContext("/v1/ubike-station/taipei", new HttpRequestHandler());
        server.start();
    }
}