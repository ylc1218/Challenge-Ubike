package ylc.appier.challenge.ubike;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class Main{
    public static void main(String[] args) throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(80), 0);
        server.createContext("/v1/ubike-station/taipei", new MyHandler());
        server.start();
    }

    static class MyHandler implements HttpHandler{
    	@Override
        public void handle(HttpExchange t) throws IOException {
    		System.out.println(t.getRequestURI().getQuery());
            String response = "This is the response";
            t.sendResponseHeaders(200, response.getBytes().length);
            OutputStream os = t.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }    
}