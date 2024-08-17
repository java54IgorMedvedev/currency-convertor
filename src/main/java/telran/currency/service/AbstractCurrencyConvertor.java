package telran.currency.service;

import java.util.*;
import java.util.stream.Collectors;

public abstract class AbstractCurrencyConvertor implements CurrencyConvertor {
    protected Map<String, Double> rates;

    @Override
    public List<String> strongestCurrencies(int amount) {
        return rates.entrySet().stream()
                .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                .limit(amount)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    @Override
    public List<String> weakestCurrencies(int amount) {
        return rates.entrySet().stream()
                .sorted(Map.Entry.<String, Double>comparingByValue())
                .limit(amount)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    @Override
    public HashSet<String> getAllCodes() {
        return new HashSet<>(rates.keySet());
    }
}
