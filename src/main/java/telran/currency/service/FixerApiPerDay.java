package telran.currency.service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.*;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.Instant;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.json.JSONObject;

public class FixerApiPerDay extends AbstractCurrencyConvertor {
   private static final String API_RATES = "rates";
private static final String API_TEMPSTAMP = "timestamp";
private static final long REFRESH_INTERVAL_SECONDS = 24 * 3600;
protected String uriString = "http://data.fixer.io/api/latest?access_key=4349417cbeea98dfd1aa87895ebecf75";
   protected HttpClient httpClient = HttpClient.newHttpClient();
   protected HttpRequest httpRequest = getHttpRequest();
   protected Instant nextRequestTime;
   public FixerApiPerDay() {
	   fillRates();
   }
private HttpRequest getHttpRequest() {
	try {
		HttpRequest request = HttpRequest.newBuilder(new URI(uriString)).build();
		return request;
	} catch (URISyntaxException e) {
		throw new RuntimeException("Wrong URI string");
	}
}
protected void fillRates() {
	try {
		HttpResponse<String> response = httpClient.send(httpRequest,
				BodyHandlers.ofString());
		JSONObject jsonObj = new JSONObject(response.body()); 
		setRates(jsonObj);
		setNextRequestTime(jsonObj);
	} catch (Exception e) {
		throw new RuntimeException(e.getMessage());
	}
	
}
private void setNextRequestTime(JSONObject jsonObj) {
	long lastUpdateSec = jsonObj.getLong(API_TEMPSTAMP);
	nextRequestTime = Instant.ofEpochSecond(lastUpdateSec)
			.plusSeconds(REFRESH_INTERVAL_SECONDS);
	
}
private void setRates(JSONObject jsonObj) {
	JSONObject ratesJSON = jsonObj.getJSONObject(API_RATES);
	rates = ratesJSON.keySet().stream()
			.collect(Collectors.toMap(Function.identity(), ratesJSON::getDouble));
	
}
@Override
public List<String> strongestCurrencies(int amount) {
	refresh();
	return super.strongestCurrencies(amount);
}
@Override
public List<String> weakestCurrencies(int amount) {
	refresh();
	return super.weakestCurrencies(amount);
}
@Override
public double convert(String codeFrom, String codeTo, int amount) {
	refresh();
	return super.convert(codeFrom, codeTo, amount);
}
private void refresh() {
	if (nextRequestTime.isBefore(Instant.now())) {
		fillRates();
	}
	
}
}