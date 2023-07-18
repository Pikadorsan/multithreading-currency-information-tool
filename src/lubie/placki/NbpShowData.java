package lubie.placki;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.Semaphore;

import org.json.JSONArray;
import org.json.JSONObject;

public class NbpShowData extends Thread{
    NbpDownloadData nbpDownloadData;
    List<String> currencies;
    JSONArray rates;
    Semaphore semaphore;
    Semaphore semaphore2;


    public NbpShowData(NbpDownloadData nbpDownloadData, Semaphore semaphore, Semaphore semaphore2)
    {
        this.nbpDownloadData = nbpDownloadData;
        this.semaphore = semaphore;
        this.semaphore2 = semaphore2;
    }

    @Override
    public void run(){
        try {
            semaphore.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            FileWriter zapis = new FileWriter("exchange_rates.txt");

            currencies = nbpDownloadData.getCurrencies();
            rates = nbpDownloadData.getRates();

            String[][] table = new String[currencies.size() + 1][4];
            table[0][0] = "ID";
            table[0][1] = "code";
            table[0][2] = "rate";
            table[0][3] = "rate date";


            for (int i = 0; i < rates.length(); i++) {
                JSONObject rate = rates.getJSONObject(i);
                String currency = rate.getString("code");
                int index = currencies.indexOf(currency) + 1;
                table[index][0] = Integer.toString(index);
                table[index][1] = currency;
                double mid = rate.getDouble("mid");
                table[index][2] = mid > 0 ? Double.toString(mid) : "Data NOT available";

                table[index][3] = String.valueOf(new Date());
            }
            StringBuilder sb = new StringBuilder();
            for (String[] row : table) {
                sb.append(String.join(",", row)).append("\n");
                System.out.format("%-5s %-5s %-10s %-10s%n", row[0], row[1], row[2], row[3]);
                zapis.write(String.join(" , ", row[1], row[2]) + "\n");
            }
            zapis.close();
            semaphore2.release();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}



