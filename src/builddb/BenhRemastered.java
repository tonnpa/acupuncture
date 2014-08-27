package builddb;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ani on 8/23/14.
 */
public class BenhRemastered {

    private final int BUFFER_SIZE = 8;
    private List<String> nhom_benh_list;
    private List<String> benh_list;
    private List<Integer> benh_line_numbers;
    private List<String> benh_notes;
    private int size;

    public BenhRemastered() {
        nhom_benh_list = new ArrayList<>();
        benh_list = new ArrayList<>();
        benh_line_numbers = new ArrayList<>();
        benh_notes = new ArrayList<>();

        size = 0;
    }

    public void work() {
        // Initialize file resources
        File input3 = new File("./text_src/3");
        File input4 = new File("./text_src/4");
        File input5 = new File("./text_src/5");
        File input6 = new File("./text_src/6");
        File input_benh = new File("./text_src/Benh");

        try (
                BufferedReader br_in3 = new BufferedReader(new FileReader(input3));
                BufferedReader br_in4 = new BufferedReader(new FileReader(input4));
                BufferedReader br_in5 = new BufferedReader(new FileReader(input5));
                BufferedReader br_in6 = new BufferedReader(new FileReader(input6));
                BufferedReader br_benh = new BufferedReader(new FileReader(input_benh))
        ) {
            // declaration and initialization of variables
            BufferedReader[] br = {null, null, null, br_in3, br_in4, br_in5, br_in6, null};
            String[] last_line_from_file = new String[BUFFER_SIZE];
            String[] last_header = new String[BUFFER_SIZE];
            for (int i = 0; i < last_header.length; i++) {
                last_line_from_file[i] = last_header[i] = "";
            }
            int prev_level = 0, current_level = 0;
            int benh_line_number = 1;
            for (int i = 3; i < br.length - 1; i++) {
                last_line_from_file[i] = br[i].readLine();
            }

            // first line has fault and therefore is skipped
            br_benh.readLine();
            // process first header3 - nhom benh
            String benh_line = br_benh.readLine();
            benh_line_number++;
            // get level of first line read
            for (int i = 3; i < last_line_from_file.length; i++) {
                if (benh_line.equals(last_line_from_file[i])) {
                    current_level = i;
                    last_header[i] = benh_line;
                    last_line_from_file[i] = br[i].readLine();
                }
            }
            System.out.println("Program starting with current level = " + current_level);
            while ((benh_line = br_benh.readLine()) != null) {
                benh_line_number++;
                // get level
                boolean match = false;
                for (int i = current_level + 1; i >= 3; i--) {
                    if (benh_line.equals(last_line_from_file[i])) {
//                        System.out.println("Match: " + last_line_from_file[i] + " " + i);
                        match = true;
                        prev_level = current_level;
                        current_level = i;
                        last_line_from_file[i] = br[i].readLine();
                        break;
                    }
                }
                // skip if the line is not a header
                if (!match) {
                    continue;
                }
                if (current_level > prev_level) {
                    last_header[current_level] = benh_line;
                } else { //current_level <= previous_level
                    write_table_record_nhombenh(last_header, benh_line_number);
                    // clear variables
                    for (int i = current_level; i < last_header.length; i++) {
                        last_header[i] = "";
                    }
                    // store new line
                    last_header[current_level] = benh_line;
                }
            }
            // write last record
            write_table_record_nhombenh(last_header, benh_line_number);
            System.out.println("Total number of benh: " + size);
        } catch (IOException e) {
            e.printStackTrace(System.err);
        }
    }

    public void write_sql_statements_nhombenh(File destination) {
        if (size == 0) {
            work();
        } else {
            try (PrintWriter pw = new PrintWriter(new FileWriter(destination))) {
                for (int i = 0; i < size; i++) {
                    String sql_command = SQLCommand.insert_into("NHOM_BENH", nhom_benh_list.get(i), benh_list.get(i),
                            benh_notes.get(i));
                    pw.println(sql_command);
                }
            } catch (IOException e) {
                e.printStackTrace(System.err);
            }
        }

    }

    private void write_table_record_nhombenh(String[] last_header, int benh_line_number) {
        String note = "";
        //create note
        for (int i = 3; i < 7; i++) {
            if (!last_header[i].isEmpty()) {
                if (last_header[i].startsWith("mục")) {
                    note = last_header[i].replaceFirst("\\D*$", "");
                } else if (last_header[i].matches("^[abcdefgh]\\s?[)].*$") && note.contains("mục")) {
                    String temp = last_header[i].replaceFirst("^[abcdefgh]\\s?[)]", "");
                    note += " " + last_header[i].replace(temp, "").trim();
                }
            }
        }
        String nhom_benh = extract_name(last_header[3]);
        String ten_benh;
        if (last_header[4].isEmpty()) {
            ten_benh = nhom_benh;
        } else {
            ten_benh = extract_name(last_header[4]);
            for (int i = 5; i < 7; i++) {
                if (!last_header[i].isEmpty()) {
                    ten_benh += " " + extract_name(last_header[i]);
                }
            }
        }
        // add to lists
        nhom_benh_list.add(size, nhom_benh);
        benh_list.add(size, ten_benh);
        benh_line_numbers.add(size, benh_line_number);
        benh_notes.add(size, note);
        size++;
    }

    private String extract_name(String line) {
        if (line.startsWith("mục")) {
            String temp = line.replaceFirst("\\D*$", "");
            return line.replace(temp, "").trim();
        } else if (line.matches("^[abcdefgh]\\s?[)].*$")) {
            return line.replaceFirst("^[abcdefgh]\\s?[)]", "").trim();
        } else {
            return line.replaceFirst("^\\d+\\s*[-–]\\s*", "").trim();
        }
    }
}
