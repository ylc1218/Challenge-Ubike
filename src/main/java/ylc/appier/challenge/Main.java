package ylc.appier.challenge;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import ylc.appier.challenge.db.DbHandler;
import ylc.appier.challenge.ubike.UbikeInfo;

public class Main{
    public static void main(String[] args) throws Exception {
    	DbHandler.init();
    	UbikeInfo.init();
    	
        int port = Integer.parseInt(System.getenv("PORT"));
		HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
		server.createContext("/v1/ubike-station/taipei", new HttpRequestHandler());
        server.start();
    }
    
    private static class HttpRequestHandler implements HttpHandler{
    	private static Executor executor = Executors.newFixedThreadPool(5);	
    	
    	@Override
        public void handle(HttpExchange t) throws IOException {
    		executor.execute(new HttpWorker(t));
        }
    	
    }
}
