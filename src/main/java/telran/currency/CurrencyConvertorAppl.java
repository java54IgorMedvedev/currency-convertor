package telran.currency;

import telran.currency.service.CurrencyConvertor;
import telran.currency.service.FixerApiPerDay;
import telran.view.Item;
import telran.view.InputOutput;

import java.util.List;
import java.util.Scanner;

public class CurrencyConvertorAppl {

    public static void main(String[] args) {
        CurrencyConvertor currencyConvertor = new FixerApiPerDay();
        InputOutput io = new ConsoleInputOutput();

        List<Item> items = CurrencyItems.getItems(currencyConvertor);
        boolean exit = false;

        while (!exit) {
            io.writeLine("Currency Converter:");
            for (int i = 0; i < items.size(); i++) {
                io.writeLine((i + 1) + ". " + items.get(i).displayName()); 
            }

            int choice = io.readInt("Choose option: ", "Invalid choice");
            if (choice >= 1 && choice <= items.size()) {
                Item selectedItem = items.get(choice - 1);
                selectedItem.perform(io); 
                exit = selectedItem.isExit(); 
            } else {
                io.writeLine("Invalid choice. Please try again.");
            }
        }
    }

    static class ConsoleInputOutput implements InputOutput {
        private final Scanner scanner = new Scanner(System.in);

        @Override
        public String readString(String prompt) {
            writeString(prompt);
            return scanner.nextLine();
        }

        @Override
        public void writeString(String str) {
            System.out.print(str);
        }
    }
}
