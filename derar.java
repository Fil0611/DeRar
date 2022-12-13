/* -- LIBRERIE -- */
import java.util.Scanner;
import java.io.IOException;
import java.io.File;
// import itertools, os, time, shutil
// from subprocess import run

public class derar {
    /* -- VARIABILI -- */
    String password;
    static int passlen;
    static int totalpsw;
    static String filename;
    String currdir;     // = os.path.dirname(os.path.realpath(__file__))
    String srcunrar = "C:\\Program Files\\WinRAR\\UnRAR.exe";

    String[] comando = {"unrar", "e", "-inul", "-p", filename};

    /* -- MAIN -- */
    public static void main (String[] args) {
        Scanner in = new Scanner(System.in);

        while (true) {
            try {
                System.out.print("Inserire la lunghezza della password: ");
                passlen = in.nextInt();

                if (passlen > 0) {
                    break;
                }else if (passlen <= 0) {
                    System.out.println("Inserire un numero maggiore di 0. Riprova.");
                    continue;
                }
            }catch (IllegalArgumentException e) {
                System.out.println("Inserire un numero. Riprova.");
            }
        }

        while (true) {
            System.out.print("Inserire il nome del file (non specificare '.rar'): ");
            filename = in.nextLine();
            filename = in.nextLine();

            //controllare esistenza file
            File f = new File(".\\" + filename + ".rar");
            if (f.exists()) {
                break;
            }else {
                System.out.println("\nIl file non esiste. Riprova.");
                continue;
            }
        }

        filename = filename + ".rar";
        System.out.println(filename);

        utils u = new utils();
        u.select_option();

        totalpsw = u.count_total_psw(passlen);

        u.print_charset();
        System.out.println();
        System.out.println("Password totali: " + totalpsw);
        forward(in);
        clear();

        // try {
        //     ProcessBuilder pb = new ProcessBuilder(/*comando*/);
        //     pb.redirectErrorStream(true);
        //     Process process = pb.start();
// 
        //     int exitCode = process.waitFor();
        //     System.out.println(exitCode);
        // }catch (IOException | InterruptedException e) {
        //     System.out.println(e.getMessage());
        // }
    }

    // [spostare in utils.java]
    static void clear () {
        if (System.getProperty("os.name").equals("Windows")) {
            System.out.print("\033[H\033[2J");
        } else {
            System.out.print("\033c");
        }
    }

    static void forward (Scanner in) {
        System.out.print("Premere INVIO per continuare");
        in.nextLine();
    }
}