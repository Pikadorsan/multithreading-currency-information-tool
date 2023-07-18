package lubie.placki;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Semaphore;

public class PeripheralsDownload extends Thread {

    URL url;
    private final Semaphore semaphore3;

    PeripheralsDownload(Semaphore semaphore)
    {
        this.semaphore3 = semaphore;
    }

    @Override
    public void run() {
        try {
            URL url = new URL("https://mocki.io/v1/ecf9ce97-38fc-48d3-8ccb-5e2f617aca95");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();
            this.url = url;
            int responseCode = conn.getResponseCode();
            if (responseCode != 200) {
                throw new RuntimeException("HTTP Error: " + responseCode);
            }

            conn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }

        semaphore3.release();
    }

    public URL getUrl() {return url;}
}