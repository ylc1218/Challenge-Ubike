package ylc.appier.challenge;

import java.io.IOException;
import java.io.OutputStream;
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
		System.out.println(t.getRequestURI().getQuery());
		Map<String, String> params = queryToMap(t.getRequestURI().getQuery());
		
		//TODO: check param(exist, parse), check taipei
		double lat = Double.parseDouble(params.get("lat"));
		double lng = Double.parseDouble(params.get("lng"));
		
		UbikeSelector selector = new UbikeSelector();
		List<UbikeResult> results = selector.selectNearestStations(lat, lng, 2); // TODO: count
		System.out.println(results.toString());
		
        String response = genResponse(results); //TODO: system error code
        try {
			t.sendResponseHeaders(200, response.getBytes().length);
			OutputStream os = t.getResponseBody();
	        os.write(response.getBytes());
	        os.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
       
	}
	
	private String genResponse(List<UbikeResult> results){
		int code = 0;
		if (results.size() == 0){
			code = 1;
		}
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("code", code);
		map.put("result", results);
				
		Gson gson = new Gson();
		return gson.toJson(map);		
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
