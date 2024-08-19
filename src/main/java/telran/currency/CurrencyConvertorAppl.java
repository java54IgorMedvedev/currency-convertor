package telran.currency;

import java.util.List;

import telran.currency.service.*;
import telran.view.*;

public class CurrencyConvertorAppl {

	public static void main(String[] args) {
		CurrencyConvertor convertor = new FixerApiPerDay();
		List<Item> items = CurrencyItems.getItems(convertor);
		Menu menu = new Menu("Currencies Application", items.toArray(Item[]::new));
		menu.perform(new SystemInputOutput());

	}

}