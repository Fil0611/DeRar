/* -- LIBRERIE -- */
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.lang.Math;

public class utils {
    /* -- ATTRIBUTI -- */
    public List<Character> charset = new ArrayList<>();          // complesso dei charset selezionati

    // charsets
    private final char[] lowercase = {'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z'};
    private final char[] uppercase = {'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z'};
    private final char[] accented_lowercase = {'à','è','ì','ò','ù','á','é','í','ó','ú','ý','â','ê','î','ô','û','ã','ñ','õ','ä','ë','ï','ö','ü','ÿ','å','æ','œ','ç','ð','ø','ā','ē','ī','ō','ū'};
    private final char[] accented_uppercase = {'À','È','Ì','Ò','Ù','Á','É','Í','Ó','Ú','Ý','Â','Ê','Î','Ô','Û','Ã','Ñ','Õ','Ä','Ë','Ï','Ö','Ü','Ÿ','Å','Æ','Œ','Ç','Ð','Ø','Ā','Ē','Ī','Ō','Ū'};
    private final char[] special = {'.',':',',',';','-','_','!','?','\'','"','/','\\','|','^','°','+','*','=','&','%','@','€','$','£','§','ç','[',']','{','}'};
    private final char[] digits = {'0','1','2','3','4','5','6','7','8','9'};

    /* -- FUNZIONI -- */
    // stampa del menù di selezione iniziale
    public void select_option () {
        Scanner in = new Scanner(System.in);

        System.out.println("OPZIONI\n");

        System.out.println("------------------");
        System.out.println("| 1. Brute force |");
        System.out.println("|                |");
        System.out.println("| 0. Esci        |");
        System.out.println("------------------\n");

        int sel = in.nextInt();
        clear();

        // [cambiare con switch case]
        if (sel == 1) {
            select_charset();
        }else if (sel == 0) {
            System.out.println("Programma terminato.");
            System.exit(0);
        }else {
            System.out.println("Scelta non valida.");
        }

        //in.close();
    }

    // stampa del menù di selezione dei charset
    private void select_charset () {
        Scanner in = new Scanner(System.in);

        System.out.println("CHARSET\n");

        int sel = -1;

        while (sel != 0) {
            System.out.println("-------------------------");
            System.out.println("| 1. Lowercase          |");
            System.out.println("| 2. Uppercase          |");
            System.out.println("| 3. Accented lowercase |");
            System.out.println("| 4. Accented uppercase |");
            System.out.println("| 5. Special characters |");
            System.out.println("| 6. Digits             |");
            System.out.println("|                       |");
            System.out.println("| 0. Stop               |");
            System.out.println("-------------------------\n");

            System.out.print("Inserire la scelta: ");
            sel = in.nextInt();
            clear();

            // [cambiare con switch case]
            if (sel == 1) {
                comb_charsets(lowercase);
            }else if (sel == 2) {
                comb_charsets(uppercase);
            }else if (sel == 3) {
                comb_charsets(accented_lowercase);
            }else if (sel == 4) {
                comb_charsets(accented_uppercase);
            }else if (sel == 5) {
                comb_charsets(special);
            }else if (sel == 6) {
                comb_charsets(digits);
            }else if (sel == 0) {
                break;
            }else {
                System.out.println("Scelta non valida.");
            }
        }

        //in.close();
    }

    // combina i charset selezionati in un'unica lista
    private void comb_charsets (char[] sel_charset) {
        for (int i = 0; i < sel_charset.length; i++) {
            charset.addAll(Arrays.asList(sel_charset[i]));
        }
    }

    // calcola il numero totale di password che verranno provate
    public int count_total_psw (int lun) {
        int tot_pass = 0;

        for (int i = 1; i < lun + 1; i++) {
            tot_pass += Math.pow(charset.size(), i);
        }

        return tot_pass;
    }

    // pulisce il terminale da ogni carattere
    public void clear () {
        if (System.getProperty("os.name").equals("Windows")) {
            System.out.print("\033[H\033[2J");
        } else {
            System.out.print("\033c");
        }
    }

    // stampa tutti i caratteri in charset
    public void print_charset () {
        for (Character c : charset) {
            System.out.print(c + " ");
        }
    }
}