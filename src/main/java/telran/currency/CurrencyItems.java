package telran.currency;

import java.util.*;

import telran.currency.service.CurrencyConvertor;
import telran.view.InputOutput;
import telran.view.Item;

public class CurrencyItems {
private static CurrencyConvertor currencyConvertor;
private static HashSet<String> codes;
public static List<Item> getItems(CurrencyConvertor currencyConvertor) {
	CurrencyItems.currencyConvertor = currencyConvertor;
	codes = currencyConvertor.getAllCodes();
	return List.of(
			Item.of("Strongest Currencies", io -> strongestWeakest(io, "Strongest")),
			Item.of("Weakest Currencies", io -> strongestWeakest(io, "Weakest")),
			Item.of("Convert currencies", CurrencyItems::convert),
			Item.ofExit()
			);
}
private static void strongestWeakest(InputOutput io, String title) {
	int amount = io.readNumberRange(String.format
			("Enter amount of top %s currencies",title), "Wrong amount",1,
			Integer.MAX_VALUE).intValue();
	if(title.toLowerCase().contains("strongest")) {
		currencyConvertor.strongestCurrencies(amount).forEach(io::writeLine);
	} else {
		currencyConvertor.weakestCurrencies(amount).forEach(io::writeLine);
	}
	
}
private static void convert(InputOutput io) {
	String currencyFrom = io.readStringOptions("Enter currency from", 
			"Wrong currency code", codes);
	String currencyTo = io.readStringOptions("Enter currency to", 
			"Wrong currency code", codes);
	int amount = io.readNumberRange(String.format("Enter amount of %s currency", currencyFrom), "Wrong amount", 1,
			Integer.MAX_VALUE).intValue();
	io.writeLine(String.format("%d of %s <=> %f of %s ", amount, currencyFrom,
			currencyConvertor.convert(currencyFrom, currencyTo, amount), currencyTo));
}
}