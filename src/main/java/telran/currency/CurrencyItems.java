package telran.currency;

import java.util.List;
import java.util.ArrayList;
import telran.currency.service.CurrencyConvertor;
import telran.view.Item;
import telran.view.InputOutput;

public class CurrencyItems {
    private static CurrencyConvertor currencyConvertor;

    public static List<Item> getItems(CurrencyConvertor currencyConvertor) {
        CurrencyItems.currencyConvertor = currencyConvertor;
        List<Item> items = new ArrayList<>();

        items.add(Item.of("Convert Currency", CurrencyItems::convertCurrency));
        items.add(Item.of("Show Strongest Currencies", CurrencyItems::showStrongestCurrencies));
        items.add(Item.of("Show Weakest Currencies", CurrencyItems::showWeakestCurrencies));
        items.add(Item.ofExit());

        return items;
    }

    private static void convertCurrency(InputOutput io) {
        String from = io.readString("Enter currency code to convert from: ");
        String to = io.readString("Enter currency code to convert to: ");
        int amount = io.readInt("Enter amount: ", "Invalid number");

        double result = currencyConvertor.convert(from, to, amount);
        io.writeLine("Converted amount: " + result);
    }

    private static void showStrongestCurrencies(InputOutput io) {
        int amount = io.readInt("Enter the number of strongest currencies to display: ", "Invalid number");

        List<String> strongestCurrencies = currencyConvertor.strongestCurrencies(amount);
        io.writeLine("Strongest currencies: " + strongestCurrencies);
    }

    private static void showWeakestCurrencies(InputOutput io) {
        int amount = io.readInt("Enter the number of weakest currencies to display: ", "Invalid number");

        List<String> weakestCurrencies = currencyConvertor.weakestCurrencies(amount);
        io.writeLine("Weakest currencies: " + weakestCurrencies);
    }
}
