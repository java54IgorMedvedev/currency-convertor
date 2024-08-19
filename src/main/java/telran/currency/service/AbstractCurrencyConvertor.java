package telran.currency.service;

import java.time.Instant;
import java.util.*;

public class AbstractCurrencyConvertor implements CurrencyConvertor {
protected Map<String, Double> rates; //key - currency ISO code;
//value - amount of code's units in 1 EUR
	@Override
	public List<String> weakestCurrencies(int amount) {
		
		return strongestWeakest(amount, Comparator.reverseOrder());
	}

	@Override
	public List<String> strongestCurrencies(int amount) {
		
		return strongestWeakest(amount, Comparator.naturalOrder());
	}

	private List<String> strongestWeakest(int amount,
			Comparator<Double> comparator) {
		
		return rates.entrySet().stream()
        .sorted(Map.Entry.comparingByValue(comparator))
        .limit(amount)
        .map(Map.Entry::getKey)
        .toList();
	}

	@Override
	public double convert(String codeFrom, String codeTo, int amount) {
		double rateFrom = rates.get(codeFrom);
		double rateTo = rates.get(codeTo);
		return rateTo / rateFrom * amount;
	}

	@Override
	public HashSet<String> getAllCodes() {
		
		return new HashSet<>(rates.keySet());
	}

}