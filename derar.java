/* -- LIBRERIE -- */
import java.util.Scanner;
import java.io.IOException;
import java.nio.file.Files;
import java.io.File;
// import itertools, os, time, shutil
// from subprocess import run

public class derar {
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

        fileName = fileName + ".rar";
        System.out.println(fileName);

        String[] comando = {"unrar", "e", "-inul", "-p", fileName};
        // System.out.println(comando[0] + " " + comando[1] + " " + comando[2] + " " + comando[3] + " " + comando[4]);

        utils u = new utils();
        u.select_option();

        totalPsw = u.count_total_psw(passLen);

        u.print_charset();
        System.out.println();
        System.out.println("Password totali: " + totalPsw);
        forward(in);
        u.clear();

        startTime = System.nanoTime();
        filesNum = countFileNumber();

        for (int i = 0; i <= passLen; i++) {
            generateCombinations(new String(), i, u, comando);
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
                        System.out.println("--- Password trovata: " + pass + " ---");
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