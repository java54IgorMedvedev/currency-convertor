package telran.currency;
import java.net.*;
import java.net.http.*;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.Arrays;
import java.util.stream.StreamSupport;

import org.json.*;

public class HttpTestRestApi {
static String uriStr = "https://api.restful-api.dev/objects";
	public static void main(String[] args) throws Exception {
		HttpClient httpClient = HttpClient.newHttpClient();
		HttpRequest httpRequest = HttpRequest.newBuilder(new URI(uriStr)).build();
		HttpResponse<String> response = httpClient.send(httpRequest,
				BodyHandlers.ofString());
		String[]properties = getProperties("price", response.body());
		System.out.println(Arrays.toString(properties));

	}
	private static String[] getProperties(String property, String jsonStr) {
		JSONArray jsonArray = new JSONArray(jsonStr);
		return StreamSupport.stream(jsonArray.spliterator(), false)
				.map(obj -> (JSONObject)obj)
				.map(jsonObj -> jsonObj.optJSONObject("data"))
				.filter(d -> d != null)
				.map(d -> d.optString(property))
				.filter(s -> !s.isEmpty()) 
				.toArray(String[]::new);
	}

}