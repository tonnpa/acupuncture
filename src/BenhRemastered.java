import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ani on 8/23/14.
 */
public class BenhRemastered {

    private List<String> nhom_benh_list;
    private List<String> benh_list;
    private List<Integer> benh_line_numbers;

    private int size;

    private final int BUFFER_SIZE = 8;

    public BenhRemastered(){
        nhom_benh_list = new ArrayList<>();
        benh_list = new ArrayList<>();
        benh_line_numbers = new ArrayList<>();

        size = 0;
    }

    public void work() {
        // Initialize file resources
        File input3 = new File("./text_src/3_clean");
        File input4 = new File("./text_src/4_clean");
        File input5 = new File("./text_src/5_clean");
        File input6 = new File("./text_src/6_clean");
        File input_benh = new File("./text_src/Benh_clean");

        try (
                BufferedReader br_in3 = new BufferedReader(new FileReader(input3));
                BufferedReader br_in4 = new BufferedReader(new FileReader(input4));
                BufferedReader br_in5 = new BufferedReader(new FileReader(input5));
                BufferedReader br_in6 = new BufferedReader(new FileReader(input6));
                BufferedReader br_benh = new BufferedReader(new FileReader(input_benh));
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
            for (int i = 3; i < br.length-1; i++) {
                last_line_from_file[i] = br[i].readLine();
            }

            // first line has fault and therefore is skipped
            br_benh.readLine();
            // process first header3 - nhom benh
            String benh_line = br_benh.readLine();
            benh_line_number++;
            // get level of first line read
            for (int i = 3; i < last_line_from_file.length; i++) {
                if (benh_line.equals(last_line_from_file[i])){
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
                for (int i = current_level + 1; i >= 3 ; i--) {
                    if (benh_line.equals(last_line_from_file[i])){
//                        System.out.println("Match! " + i + " " + last_line_from_file[i]);
                        match = true;
                        prev_level = current_level;
                        current_level = i;
                        last_line_from_file[i] = br[i].readLine();
                        break;
                    }
                }
                // skip if the line is not a header
                if (!match){
                    continue;
                }
                if (current_level > prev_level) {
                    last_header[current_level] = benh_line;
                } else {
                    System.out.println(
                            last_header[3] + " || " +
                            last_header[4] +
                            last_header[5] +
                            last_header[6] +
                            benh_line_number
                    );
                    for (int i = current_level; i < last_header.length ; i++) {
                        last_header[i] = "";
                    }
                    last_header[current_level] = benh_line;
                }
            }

            System.out.println(
                    last_header[3] + " || " +
                            last_header[4] +
                            last_header[5] +
                            last_header[6] +
                            benh_line_number
            );

        } catch (IOException e) {
            e.printStackTrace(System.err);
        }

//        System.out.println("vao class BenhRemastered");
//        File input3 = new File("./text_src/3");
//        try (BufferedReader br = new BufferedReader(new FileReader(input3));) {
//            String line = br.readLine();
//            System.out.println(line);
//
//            while ((line = br.readLine()) != null) {
//                header3.add(line);
//                if (line.matches("^\\d+.*")){
//                    nhom_benh_list.add(line);
//                }
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        for (String h:nhom_benh_list){
//            System.out.println(h);
//        }
//        for (int i = 0; i < header3.size(); i++) {
//            System.out.println(header3.get(i));
//        }
// hai ciklus nhw nhau.
    }
}
