package telran.currency.service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.json.JSONObject;

public class FixerApiPerDay extends AbstractCurrencyConvertor {
    private static final String URI_STRING = "http://data.fixer.io/api/latest?access_key=dbb5f371fde7f42901e6410d20b37fbf";
    private static final long REFRESH_INTERVAL_SECONDS = 86400; 

    private long lastUpdateTimestamp;

    public FixerApiPerDay() {
        lastUpdateTimestamp = System.currentTimeMillis() / 1000;
        rates = fetchRates();
    }

    private HashMap<String, Double> fetchRates() {
        try {
            HttpClient httpClient = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder(new URI(URI_STRING)).build();
            HttpResponse<String> response = httpClient.send(request, BodyHandlers.ofString());
            JSONObject jsonObject = new JSONObject(response.body());
            JSONObject jsonRates = jsonObject.getJSONObject("rates");

            return jsonRates.keySet().stream()
                    .collect(Collectors.toMap(key -> key, jsonRates::getDouble, (a, b) -> b, HashMap::new));
        } catch (Exception e) {
            e.printStackTrace();
            return new HashMap<>();
        }
    }

    private void refreshRatesIfNeeded() {
        long currentTimestamp = System.currentTimeMillis() / 1000;
        if (currentTimestamp - lastUpdateTimestamp >= REFRESH_INTERVAL_SECONDS) {
            rates = fetchRates();
            lastUpdateTimestamp = currentTimestamp;
        }
    }

    @Override
    public List<String> strongestCurrencies(int amount) {
        refreshRatesIfNeeded();
        return rates.entrySet().stream()
                .sorted(Map.Entry.<String, Double>comparingByValue())
                .limit(amount)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    @Override
    public List<String> weakestCurrencies(int amount) {
        refreshRatesIfNeeded();
        return rates.entrySet().stream()
                .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                .limit(amount)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    @Override
    public double convert(String codeFrom, String codeTo, int amount) {
        refreshRatesIfNeeded();
        Double rateFrom = rates.get(codeFrom);
        Double rateTo = rates.get(codeTo);
        if (rateFrom == null || rateTo == null) {
            throw new IllegalArgumentException("Invalid currency code.");
        }
        return amount * (rateTo / rateFrom);
    }
}
