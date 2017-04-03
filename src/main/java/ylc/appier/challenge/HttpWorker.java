package ylc.appier.challenge;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;

import ylc.appier.challenge.ubike.UbikeResult;
import ylc.appier.challenge.ubike.UbikeSelector;

class HttpWorker implements Runnable{
	private HttpExchange t;
	HttpWorker(HttpExchange t){
		this.t= t;
	}
	
	@Override
	public void run(){
		try{
			System.out.println(t.getRequestURI().getQuery());
			Map<String, String> params = queryToMap(t.getRequestURI().getQuery());
			
			// -1: invalid latitude or longitude
			if (!validLocation(params)){ 
				respond(genResponse(-1, null));
				return;
			};
			
			double lat = Double.parseDouble(params.get("lat"));
			double lng = Double.parseDouble(params.get("lng"));
			
			// -2: not in Taipei city
			if (!inTaipeiCity(lat, lng)){
				respond(genResponse(-2, null));
				return;
			}
								
			UbikeSelector selector = new UbikeSelector();
			List<UbikeResult> results = selector.selectNearestStations(lat, lng, 2); // TODO: const count
			if (results.isEmpty()){ // 1: ubike station full
				respond(genResponse(1, null));
			}
			else{ // 0: ok
				respond(genResponse(0, results));
			}
			
			System.out.println(results.toString());
		}
		catch(Exception e){ // -3: system error
			respond(genResponse(-3, null));
		}		                       
	}
	
	
	
	private void respond(String response){
		try {
			t.sendResponseHeaders(200, response.getBytes().length);
			OutputStream os = t.getResponseBody();
	        os.write(response.getBytes());
	        os.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private String genResponse(int code, List<UbikeResult> results){
		if (code != 0){
			results = new ArrayList<UbikeResult>();
		}
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("code", code);
		map.put("result", results);
				
		Gson gson = new Gson();
		return gson.toJson(map);		
	}
	
	private boolean validLocation(Map<String, String> params){
		if (!params.containsKey("lat") || !params.containsKey("lng")){
			return false;
		}
		
		try{
			double lat = Double.parseDouble(params.get("lat"));
			double lng = Double.parseDouble(params.get("lng"));
			if (-90 <= lat && lat <= 90 && -180 <= lng && lng <= 180){
				return true;
			}
			else{
				return false;
			}
		}catch(NumberFormatException e){
			return false;
		}		
	}
	
	private boolean inTaipeiCity(double lat, double lng){ //TODO: check location
		return true;
	}
	
	private Map<String, String> queryToMap(String query){
        Map<String, String> result = new HashMap<String, String>();
        for (String param : query.split("&")) {
            String pair[] = param.split("=");
            if (pair.length > 1) {
                result.put(pair[0], pair[1]);
            }else{
                result.put(pair[0], "");
            }
        }
        return result;
    }
}
