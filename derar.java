// javac -encoding UTF-8 derar.java
// java derar

/* -- LIBRERIE -- */
import java.util.ArrayList;
import java.util.Scanner;
import java.util.List;
import javax.swing.JProgressBar;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.event.KeyEvent;
import java.awt.AWTException;
import java.awt.Robot;
import java.io.IOException;
import java.io.File;
import java.math.RoundingMode;
import java.math.BigDecimal;
import java.nio.file.Files;

public class derar extends Thread {
    /* -- VARIABILI -- */
    static int passLen, totalPsw, exitCode, filesNum, numThread, perThread;
    static int s = 0;
    static long startTime, stopTime;
    static double elapsedTime;
    static String fileName;
    static String destCopy = ".\\UnRAR.exe";
    static String srcCopy = "C:\\Program Files\\WinRAR\\UnRAR.exe";
    static Thread[] threads;
    static List<String> comb = new ArrayList<>();
    static String[][] subArray;
    static String[] comando = new String[5];
    static utils u;

    /* -- MAIN -- */
    public static void main (String[] args) throws IOException, AWTException {
        Scanner in = new Scanner(System.in);

        // copio il fire UnRAR.exe se non esiste già nella cartella
        File x = new File(destCopy);
        if (!x.exists()){
            Files.copy(new File(srcCopy).toPath(), new File(destCopy).toPath());
        }
        
        // controllo sull'inserimento della password
        boolean validInput = false;
        while (!validInput) {
            System.out.print("Inserire la lunghezza della password: ");
            if (in.hasNextInt()) {
                passLen = in.nextInt();
                if (passLen > 0) {
                    validInput = true;
                }else {
                    System.out.println("Inserire un numero maggiore di 0.");
                }
            } else {
                System.out.println("Inserire un numero.");
                in.next();  // scarto l'input non valido
            }
        }

        // controllo sull'inserimento del nome del file
        while (true) {
            System.out.print("Inserire il nome del file (non specificare '.rar'): ");
            fileName = in.nextLine();
            fileName = in.nextLine();

            //controllare esistenza file
            File f = new File(".\\" + fileName + ".rar");
            if (f.exists()) {
                break;
            }else {
                System.out.println("\nIl file non esiste. Riprova.");
                continue;
            }
        }

        // controllo sull'inserimento del numero di thread
        while (true) {
            System.out.print("Inserire il numero di thread: ");
            numThread = in.nextInt();

            if (numThread > 0) {
                break;
            }else {
                System.out.println("Inserire un numero positivo.");
                continue;
            }
        }

        // aggiungo l'estensione al nome del file inserito
        fileName = fileName + ".rar";

        // assemblo il comando da inserire
        comando[0] = "unrar";
        comando[1] = "e";
        comando[2] = "-inul";
        comando[3] = "-p";
        comando[4] = fileName;

        u = new utils();
        u.clear();
        u.select_option();                                          // seleziono l'azione da eseguire

        totalPsw = u.count_total_psw(passLen);                      // calcolo il numero totale di combinazioni da provare

        perThread = totalPsw/numThread;                             // calcolo gli elementi da controllare per ogni thread

        u.print_charset();                                          // stampo il charset
        System.out.println();
        System.out.println("Password totali: " + totalPsw);
        u.forward(in);
        u.clear();

        filesNum = countFileNumber();                               // conto i file nella cartella all'inizio

        System.out.println("Sto generando le combinazioni...");
        // genero e controllo le combinazioni
        for (int i = 0; i <= passLen; i++) {
            combinate(new String(), i, u.charset, 0);
        }
        comb.remove(0);                                      // rimuovo il primo elemento della lista (vuoto)
        System.out.println("Ho finito la generazione delle combinazioni!");
        u.forward(in);
        u.clear();

        System.out.println("Numero combinazioni: " + comb.size());
        System.out.println("Numero thread: " + numThread);
        System.out.println("Combinazioni per thread: " + perThread + "\n");
        System.out.println("Prima password: " + comb.get(0) + ", ultima password: " + comb.get(comb.size() - 1));

        subArray = new String[numThread][];

        // divido la lista di password generate nei sottoarray che verranno analizzati dai thread
        for (int i = 0; i < numThread; i++) {
            subArray[i] =  comb.subList(perThread*i, perThread*(i+1)).toArray(new String[perThread]);
        }

        System.out.println();

        u.forward(in);
        u.clear();

        System.out.println("Le finestre verranno minimizzate\ne verrà mostrato il progresso nel controllo delle combinazioni.");

        threads = new Thread[numThread];

        // inserisco i thread nell'array...
        for (int i = 0; i < numThread; i++) {
            derar ci = new derar();
            Thread t = new Thread(ci, String.valueOf(i));
            threads[i] = t;
        }

        u.forward(in);

        // ...mostro il desktop...
        Robot robot = new Robot();
        robot.keyPress(KeyEvent.VK_WINDOWS);
        robot.keyPress(KeyEvent.VK_D);
        robot.keyRelease(KeyEvent.VK_WINDOWS);
        robot.keyRelease(KeyEvent.VK_D);

        startTime = System.nanoTime();                              // prendo il tempo all'inizio dell'esecuzione

        // ...e faccio eseguire i thread
        for (int i = 0; i < numThread; i++) {
            threads[i].start();
        }
    }

    // codice eseguito dai thread
    @Override
    public void run () {
        // istanzio i componenti per l'interfaccia grafica
        JFrame frame = new JFrame();
        JPanel panel = new JPanel();
        JLabel label = new JLabel("Thread " + Thread.currentThread().getName() + ":");
        JProgressBar progressBar = new JProgressBar(0, perThread);

        // imposto i componenti
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setUndecorated(true);

        progressBar.setStringPainted(true);
        progressBar.setMinimum(0);
        progressBar.setMaximum(perThread);

        // aggiungo i componenti al pannello, poi al frame
        panel.add(label);
        panel.add(progressBar);

        frame.add(panel);

        // faccio il pack del frame
        frame.pack();

        frame.setVisible(true);

        int i = 0;                                                      // contatore per tracciare il progresso di controllo delle combinazioni
        int k = Integer.parseInt(Thread.currentThread().getName());     // numero del thread
        String[] subCharset = subArray[k];                              // imposto il sottoarray associato al singolo thread

        frame.setLocation((int) (k * frame.getSize().getWidth()), (int) (540 - (frame.getSize().getHeight() / 2)));

        System.out.println(subCharset[0] + " " + subCharset[subCharset.length - 1] + " - " + Thread.currentThread().getName());

        // controllo le combinazioni
        for (String st : subCharset) {
            // aggiorno la barra di progresso
            progressBar.setValue(i);
            Double progress = BigDecimal.valueOf((progressBar.getValue() / Integer.valueOf(progressBar.getMaximum()).doubleValue()) * 100).setScale(4, RoundingMode.HALF_UP).doubleValue();
            progressBar.setString(String.valueOf(progress) + "%");

            // imposto il comando da eseguire
            comando[3] = "-p" + st;
            String command = comando[0] + " " + comando[1] + " " + comando[2] + " " + comando[3] + " " + comando[4];

            // controllo se la stringa corrisponde con la keyword
            try {
                Process process = Runtime.getRuntime().exec(command);

                // se il numero di file nella cartella cambia significa che il file è stato estratto e posso terminare il programma
                if (countFileNumber() > filesNum) {
                    u.clear();
                    System.out.println("--- File estratto " + Thread.currentThread().getName() + " ---");
                    stopTime = System.nanoTime();
                    elapsedTime = (stopTime - startTime) / 1000000000;
                    System.out.println("--- " + elapsedTime + " secondi ---");

                    System.exit(0);
                }
            }catch (IOException e) {
                System.out.println(e.getMessage());
            }

            i += 1;
        }
    }

    // genera le combinazioni e le salva nella lista delle combinazioni
    public static void combinate (String pass, int lun, List<Character> arr, int s) {
        // se la lunghezza della parola è zero, aggiunge la parola alla lista
        if (lun == 0) {
            comb.add(pass);
            return;
        }

        // altrimenti, genera tutte le combinazioni usando i caratteri all'interno della lista arr
        for (char c : arr) {
            combinate(pass + c, lun - 1, arr, s);
        }
    }

    // controlla il numero di file nella working dir
    public static int countFileNumber () {
        File folder = new File(".\\");
        File[] files = folder.listFiles();
        int numFiles = files.length;

        return numFiles;
    }
}