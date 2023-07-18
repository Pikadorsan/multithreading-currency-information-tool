package lubie.placki;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.*;

public class Main {
    public static void main(String[] args) {

        if (args.length == 1 && (args[0].equals("-h") || args[0].equals("--help") || args[0].equals("/?") || args[0].equals("/h"))) {
            System.out.println("The application is used for downloading and displaying prices of computer peripherals.");
            System.out.println("This application was created for development purposes.");
            System.out.println("Invocation parameters:");
            System.out.println("-h, --help, /?, /h - display the list of parameters");
            System.out.println("-f <file name>, --file <file name> - save the result to a file with the specified name");
            System.exit(0);
        }
        
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
