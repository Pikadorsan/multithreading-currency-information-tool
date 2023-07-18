package lubie.placki;

import java.io.*;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.*;
import java.util.concurrent.Semaphore;

import org.json.JSONArray;
import org.json.JSONObject;

public class NbpDownloadData extends Thread{

    List<String> currencies;
    JSONArray rates;
    private final Semaphore semaphore;

    NbpDownloadData(Semaphore semaphore)
    {
        this.semaphore = semaphore;
    }



    @Override
    public void run(){
        List<String> currencies = new ArrayList<>();
        try {
        URL url = new URL("https://api.nbp.pl/api/exchangerates/tables/A?format=json");
        BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
        String json = in.readLine();
        in.close();

        JSONArray jsonArray = new JSONArray(json);
        JSONObject jsonObject = jsonArray.getJSONObject(0);
        JSONArray rates = jsonObject.getJSONArray("rates");

        for (int i = 0; i < rates.length(); i++) {
            JSONObject rate = rates.getJSONObject(i);
            String currency = rate.getString("code");
            currencies.add(currency);
        }

            this.currencies = currencies;
            this.rates = rates;

        } catch (UnknownHostException e) {
            System.err.println("Error: No internet connection.");
            System.exit(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        semaphore.release();
    }

    public List<String> getCurrencies() {return currencies;}

    public JSONArray getRates() {return rates;}
}

