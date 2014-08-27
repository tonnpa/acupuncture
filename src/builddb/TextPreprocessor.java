package builddb;

import java.io.*;
import java.nio.file.Files;

/**
 * Created by Ani on 8/10/14.
 */
public class TextPreprocessor {

    // removes empty lines, dots from end of sentences and
    // writes everything to lowercase letters
    public static void trim_and_lower_case(File source, boolean remove_page_number) {
        try {
            File temp = new File("./text_src/temp");
            temp.createNewFile();
            try (BufferedReader br = new BufferedReader(new FileReader(source));
                 PrintWriter pw = new PrintWriter(new FileWriter(temp))) {
                String line;
                while ((line = br.readLine()) != null) {
                    if (line.isEmpty()) {
                        continue;
                    }
                    if (line.endsWith(".")) {
                        line = line.substring(0, line.length() - 1);
                    }
                    if (remove_page_number) {
                        line = line.replaceAll("\\s\\d+$", "");
                    }
                    pw.println(line.trim().toLowerCase());
                }
            }
            source.delete();
            Files.move(temp.toPath(), source.toPath());
        } catch (IOException e) {
            e.printStackTrace(System.err);
        }
    }

    // remove page number from end of line
    public static void remove_page_number(File source) {
        try {
            File temp = new File("./text_src/temp");
            temp.createNewFile();
            try (BufferedReader br = new BufferedReader(new FileReader(source));
                 PrintWriter pw = new PrintWriter(new FileWriter(temp))) {
                String line;
                while ((line = br.readLine()) != null) {
                    line = line.replaceAll("\\s\\d+$", "");
                    pw.println(line.trim().toLowerCase());
                }
            }
            source.delete();
            Files.move(temp.toPath(), source.toPath());
        } catch (IOException e) {
            e.printStackTrace(System.err);
        }
    }
}
