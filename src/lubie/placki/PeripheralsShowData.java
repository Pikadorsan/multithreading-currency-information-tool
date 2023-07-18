package lubie.placki;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.util.Scanner;
import java.util.concurrent.Semaphore;

public class PeripheralsShowData extends Thread{
    PeripheralsDownload peripheralsDownload;
    URL url;
    Semaphore semaphore3;
    Semaphore semaphore4;

    public PeripheralsShowData(PeripheralsDownload peripheralsDownload, Semaphore semaphore3, Semaphore semaphore4)
    {
        this.peripheralsDownload = peripheralsDownload;
        this.semaphore3 = semaphore3;
        this.semaphore4 = semaphore4;
    }


    @Override
    public void run(){
        try {
            semaphore3.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Scanner inputScanner = new Scanner(System.in);
        System.out.println("Filter by Name:");
        String searchTerm = inputScanner.nextLine();
        url = peripheralsDownload.getUrl();
        Scanner scanner = null;
        try {
            scanner = new Scanner(url.openStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        String response = scanner.useDelimiter("\\Z").next();
        JSONArray products = new JSONArray(response);


        System.out.printf("%-40s %-57s %-14s %-18s %n", "ID", "name", "cost", "calculated cost");
        for (int i = 0; i < products.length(); i++) {
            JSONObject product = products.getJSONObject(i);
            String id = product.getString("id");
            String name = product.getString("name");
            BigDecimal cost = new BigDecimal(product.getString("cost"));
            String currency = product.getString("currency");

            BigDecimal calculatedCost = null;
            try {
                calculatedCost = convertToPLN(cost, currency);
            } catch (IOException e) {
                e.printStackTrace();
            }
            String formattedCalculatedCost = String.format("%.2f", calculatedCost);
            if (name.toLowerCase().contains(searchTerm.toLowerCase())) {
                System.out.printf("%-40s %-57s %-14s %-18s %n", id, name, cost + " " + currency, formattedCalculatedCost + " PLN");
            }
        }
        scanner.close();
        semaphore4.release();
    }

    private static BigDecimal convertToPLN(BigDecimal amount, String currencyCode) throws IOException {
        File file = new File("exchange_rates.txt");
        Scanner scanner = new Scanner(file);
        scanner.nextLine(); // Pominięcie nagłówka

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] parts = line.split(",");
            if (parts.length == 2) {
                String code = parts[0].trim();
                String rateStr = parts[1].trim();
                if (code.equalsIgnoreCase(currencyCode)) {
                    BigDecimal rate = new BigDecimal(rateStr);
                    return amount.multiply(rate);
                }
            }
        }

        throw new IllegalArgumentException("No exchange rate found for the given currency code: " + currencyCode);
    }
}
