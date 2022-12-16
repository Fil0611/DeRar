/* -- LIBRERIE -- */
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.io.IOException;
import java.nio.file.Files;
import java.io.File;
// import itertools, os, time, shutil
// from subprocess import run

public class derar extends Thread {
    /* -- VARIABILI -- */
    // [compattare la dichiarazione delle variabili come per start e stopTime]
    static int passLen;
    static int totalPsw;
    static String fileName;
    static String destCopy = ".\\UnRAR.exe";
    static String srcCopy = "C:\\Program Files\\WinRAR\\UnRAR.exe";
    static int s = 0;
    static int exitCode;
    static int filesNum;
    static long startTime, stopTime, elapsedTime;
    
    static Thread[] threads;
    static List<String> comb = new ArrayList<>();
    static String[][] subArray;
    static int numThread;
    static int perThread;
    static String[] comando = new String[5];
    static utils u;

    /* -- MAIN -- */
    public static void main (String[] args) throws IOException {
        Scanner in = new Scanner(System.in);

        // copio il fire UnRAR.exe se non esiste già nella cartella
        File x = new File(destCopy);
        if (!x.exists()){
            Files.copy(new File(srcCopy).toPath(), new File(destCopy).toPath());
        }

        // controllo sull'inserimento della password
        while (true) {
            try {
                System.out.print("Inserire la lunghezza della password: ");
                passLen = in.nextInt();

                if (passLen > 0) {
                    break;
                }else if (passLen <= 0) {
                    System.out.println("Inserire un numero maggiore di 0. Riprova.");
                    continue;
                }
            }catch (IllegalArgumentException e) {
                System.out.println("Inserire un numero. Riprova.");
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

        System.out.print("Inserire il numero di thread: ");
        numThread = in.nextInt();

        // aggiungo l'estensione al nome del file inserito
        fileName = fileName + ".rar";
        System.out.println(fileName + "\n");

        // assemblo il comando da inserire
        //comando = {"unrar", "e", "-inul", "-p", fileName};
        comando[0] = "unrar";
        comando[1] = "e";
        comando[2] = "-inul";
        comando[3] = "-p";
        comando[4] = fileName;

        u = new utils();
        u.select_option();                                          // seleziono l'azione da eseguire

        totalPsw = u.count_total_psw(passLen);                      // calcolo il numero totale di combinazioni da provare

        perThread = totalPsw/numThread;

        u.print_charset();                                          // stampo il charset
        System.out.println();
        System.out.println("Password totali: " + totalPsw);
        forward(in);
        forward(in);
        u.clear();

        startTime = System.nanoTime();                              // prendo il tempo all'inizio
        filesNum = countFileNumber();                               // conto i file nella cartella all'inizio

        // genero e controllo le combinazioni
        for (int i = 0; i <= passLen; i++) {
            combinate(new String(), i, u.charset, 0);
        }
        comb.remove(0);

        System.out.println("Dimensione comb: " + comb.size());
        System.out.println(comb.get(0) + " " + comb.get(comb.size() - 1));
        System.out.println("Numero thread: " + numThread);
        System.out.println("Per thread: " + perThread);

        subArray = new String[numThread][];

        for (int i = 0; i < numThread; i++) {
            subArray[i] =  comb.subList(perThread*i, perThread*(i+1)).toArray(new String[perThread]);
        }

        System.out.println();

        for (int i = 0; i < numThread; i++) {
            System.out.print(subArray[i][0] + " " + subArray[i][perThread - 1] + "\n");
        }

        System.out.println(comb.size() + " " + perThread + " " + (comb.size()%perThread));

        u.clear();

        System.out.println("INIZIO");

        threads = new Thread[numThread];

        for (int i = 0; i < numThread; i++) {
            derar ci = new derar();
            Thread t = new Thread(ci, String.valueOf(i));
            threads[i] = t;
        }

        forward(in);

        for (int i = 0; i < numThread; i++) {
            threads[i].start();
        }
    }

    @Override
    public void run () {
        int k = Integer.parseInt(Thread.currentThread().getName());
        String[] subCharset = subArray[k];

        System.out.println(subCharset[0] + " " + subCharset[subCharset.length - 1] + " - " + Thread.currentThread().getName());

        for (String st : subCharset) {
            // controllo se la stringa corrisponde con la keyword
            comando[3] = "-p" + st;
            String command = comando[0] + " " + comando[1] + " " + comando[2] + " " + comando[3] + " " + comando[4];
            try {
                Process process = Runtime.getRuntime().exec(command);
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
        }
    }

    // genera le combinazioni e le salva nella lista comb
    public static void combinate (String pass, int lun, List<Character> arr, int s) {
        // se la lunghezza della parola è zero, stampa la parola
        if (lun == 0) {
            comb.add(pass);
            return;
        }

        // altrimenti, genera tutte le combinazioni usando i caratteri all'interno dell'array arr
        for (char c : arr) {
            combinate(pass + c, lun - 1, arr, s);
        }
    }

    // genera le combinazioni e le prova per estrarre il file rar
    public static void generateCombinations(String pass, int lun, utils u, String[] comando) {
        // se la lunghezza della parola è zero, stampa la parola
        if (lun == 0) {
            System.out.println("Provando: " + pass);

            if (s > 0) {
                comando[3] = "-p" + pass;
                String command = comando[0] + " " + comando[1] + " " + comando[2] + " " + comando[3] + " " + comando[4];
                try {
                    Process process = Runtime.getRuntime().exec(command);
                    //process.waitFor();
                    //exitCode = process.exitValue();
                    if (countFileNumber() > filesNum) {
                        u.clear();
                        System.out.println("--- File estratto ---");
                        stopTime = System.nanoTime();
                        elapsedTime = (stopTime - startTime) / 1000000000;
                        System.out.println("--- " + elapsedTime + " secondi ---");
                        System.exit(0);
                    }
                }catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }else {
                s++;
            }

            return;
        }
    
        // altrimenti, genera tutte le combinazioni usando i caratteri all'interno dell'array arr
        for (char c : u.charset) {
            generateCombinations(pass + c, lun - 1, u, comando);
        }
    }

    // controlla il numero di file nella working dir
    public static int countFileNumber () {
        File folder = new File(".\\");
        File[] files = folder.listFiles();
        int numFiles = files.length;
        return numFiles;
    }

    // chiede di premere INVIO per continuare
    static void forward (Scanner in) {
        System.out.print("Premere INVIO per continuare");
        in.nextLine();
    }
}