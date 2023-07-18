package lubie.placki;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.*;

public class Main {
    public static void main(String[] args) {

        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(2);

        Semaphore semaphore = new Semaphore(0);
        Semaphore semaphore2 = new Semaphore(0);
        Semaphore semaphore3 = new Semaphore(0);
        Semaphore semaphore4 = new Semaphore(0);
        NbpDownloadData nbpDownloadData = new NbpDownloadData(semaphore);
        NbpShowData nbpShowData = new NbpShowData(nbpDownloadData, semaphore, semaphore2);

        PeripheralsDownload peripheralsDownload = new PeripheralsDownload(semaphore3);
        PeripheralsShowData peripheralsShowData = new PeripheralsShowData(peripheralsDownload, semaphore3, semaphore4);

        LocalTime now = LocalTime.now();
        LocalTime scheduledTime = LocalTime.of(13, 0);
        long delayInSeconds = now.until(scheduledTime, ChronoUnit.SECONDS);

        if (now.isAfter(scheduledTime)) {
            delayInSeconds += 24 * 60 * 60;
        }

        nbpDownloadData.start();
        nbpShowData.start();

        executorService.scheduleAtFixedRate(nbpDownloadData, delayInSeconds, 24 * 60 * 60, TimeUnit.SECONDS);
        executorService.scheduleAtFixedRate(nbpShowData, delayInSeconds, 24 * 60 * 60, TimeUnit.SECONDS);

        executorService.scheduleAtFixedRate(peripheralsDownload, 0, 2, TimeUnit.MINUTES);
        executorService.scheduleAtFixedRate(peripheralsShowData, 0, 2, TimeUnit.MINUTES);
    }

}
