import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Ani on 8/10/14.
 */
public class Benh {
    public void write_sql_statements(File source, File destination){
        File temp = new File("./text_src/temp");
        try{
            boolean new_file = temp.createNewFile();
            ConsoleFeedback.create_file(temp, new_file);
        } catch (IOException e){
            e.printStackTrace();
        }
        trim_text(source, temp);
        populate_nhom_benh(temp, destination);
        populate_benh_tc(temp, destination);
        populate_benh_ccsb(temp, destination);
        populate_benh_phdt(temp, destination);
        temp.delete();
    }

    private void populate_nhom_benh(File source, File destination){
        try (BufferedReader br = new BufferedReader(new FileReader(source));
             BufferedReader br_slow = new BufferedReader(new FileReader(source));
             PrintWriter pw = new PrintWriter(new FileWriter(destination))) {
            //set up readers
            br.readLine();

            String line, prev_line;
            String nhom_benh = "", ten_benh;
            List<String> benh_array = new ArrayList<>();

            while ((line = br.readLine()) != null){
                prev_line = br_slow.readLine();
                String regex = "^[0-9]+(\\s)*[–|-](\\s)*\\D{3}";
                Pattern pattern = Pattern.compile(regex);
                Matcher matcher = pattern.matcher(line);
                while(matcher.find()) {
                    // 1. write out previous benh to file
                    // 2. clear variables
                    // 3. store information about new benh

                    // Step 1
                    for (String benh : benh_array){
                        String sql_command = SQLCommand.insert_into("NHÓM_BỆNH", nhom_benh, benh);
                        pw.println(sql_command);
//                        System.out.println(sql_command);
                    }
                    // Step 2
                    benh_array.clear();
                    // Step 3
                    if (line.contains("–")){
                        nhom_benh = line.substring(line.indexOf("–")+1, line.length()).trim();
                    } else if (line.contains("-")) {
                        nhom_benh = line.substring(line.indexOf("-")+1, line.length()).trim();
                    }
                }
                if (line.startsWith("tc")){
                    ten_benh = extract_ten_benh(prev_line);
                    benh_array.add(ten_benh);
                }
            }
            for (String benh : benh_array){
                String sql_command = SQLCommand.insert_into("NHÓM_BỆNH", nhom_benh, benh);
                pw.println(sql_command);
//                System.out.println(sql_command);
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    private void populate_benh_tc(File source, File destination){
        try (BufferedReader br = new BufferedReader(new FileReader(source));
             BufferedReader br_slow = new BufferedReader(new FileReader(source));
             PrintWriter pw = new PrintWriter(new FileWriter(destination, true))) {
            //set up readers
            br.readLine();
            String line, prev_line;
            String ten_benh = "";
            List<String> tc_list = new ArrayList<>();
            while ((line = br.readLine()) != null){
                prev_line = br_slow.readLine();

                if (line.startsWith("tc")){
                    // Step 1
                    for (String tc : tc_list){
                        String sql_command = SQLCommand.insert_into("BỆNH_TC", ten_benh, tc);
                        pw.println(sql_command);
//                        System.out.println(sql_command);
                    }
                    // Step 2
                    tc_list.clear();

                    // Step 3
                    ten_benh = extract_ten_benh(prev_line);
                    String[] tc_array = line.substring(line.indexOf(':')+1, line.length()).split(",");
                    for (String tc : tc_array){
                        tc_list.add(tc.trim());
                    }
                }
            }
            for (String tc : tc_list){
                String sql_command = SQLCommand.insert_into("BỆNH_TC", ten_benh, tc);
                pw.println(sql_command);
//                System.out.println(sql_command);
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    private void populate_benh_ccsb(File source, File destination){
        try (BufferedReader br = new BufferedReader(new FileReader(source));
             BufferedReader br_slow = new BufferedReader(new FileReader(source));
             PrintWriter pw = new PrintWriter(new FileWriter(destination, true))) {
            //set up readers
            br.readLine();
            String line, prev_line;
            String ten_benh = "", ccsb;
            while ((line = br.readLine()) != null) {
                prev_line = br_slow.readLine();
                if (line.startsWith("tc")){
                    ten_benh = extract_ten_benh(prev_line);
                }
                if (line.startsWith("ccsb")){
                    ccsb = line.substring(line.indexOf(':')+1,line.length()).trim();
                    String sql_command = SQLCommand.insert_into("BỆNH_CCSB", ten_benh, ccsb);
                    if (!ccsb.isEmpty()){
                        pw.println(sql_command);
//                        System.out.println(sql_command);
                    }
                }
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    private void populate_benh_phdt(File source, File destination){
        try (BufferedReader br = new BufferedReader(new FileReader(source));
             BufferedReader br_slow = new BufferedReader(new FileReader(source));
             PrintWriter pw = new PrintWriter(new FileWriter(destination, true))) {
            //set up readers
            br.readLine();
            String line, prev_line;
            String ten_benh = "";
            List<String> phdt_list = new ArrayList<>();
            boolean phdt_catch = false;

            while ((line = br.readLine()) != null) {
                prev_line = br_slow.readLine();
                if (line.startsWith("tc")){
                    // Step 1
                    write_out_phdt(pw, ten_benh, phdt_list);

                    // Step 2
                    phdt_list.clear();

                    // Step 3
                    ten_benh = extract_ten_benh(prev_line);
                    phdt_catch = false;
                    continue;
                }
                if (line.startsWith("phdt")){
                    phdt_catch = true;
                    continue;
                }
                if (phdt_catch){
                    if (!((prev_line.startsWith("phdt") && prev_line.length() < 6) || prev_line.startsWith("3-14-"))){
                        phdt_list.add(prev_line);
//                        System.out.println(prev_line);
                    }
                }
                if (line.contains("gia giảm") || line.startsWith("mục") || line.startsWith("lưu ý") ||
                        line.contains("nếu")){
                    phdt_catch = false;
                }
            }
            write_out_phdt(pw, ten_benh, phdt_list);
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    private void write_out_phdt(PrintWriter pw, String ten_benh, List<String> phdt_list){
        for (String phdt : phdt_list){
            if (phdt.matches("^[i|v|x]+\\s*\\d+\\D*$")){
                int so_kinh = RomanNumeral.romanToInteger(phdt.replaceAll("\\s*\\d+\\D*$",""));
                int so_huyet = Integer.parseInt(phdt.replaceAll("\\D",""));
                String ten_huyet = phdt.replaceAll("^[i|v|x]+\\s*\\d+","").trim();
                String sql_command = SQLCommand.insert_into("BỆNH_PHDT", ten_benh, so_kinh, so_huyet, ten_huyet);
                pw.println(sql_command);
//                System.out.println(sql_command);
            } else if (phdt.matches("^[o|0]\\s*\\d+\\D*$")) {
                int so_kinh = 0;
                int so_huyet = Integer.parseInt(phdt.replaceAll("\\D",""));
                String ten_huyet = phdt.replaceAll("^[o|0]\\s*\\d+","").trim();
                String sql_command = SQLCommand.insert_into("BỆNH_PHDT", ten_benh, so_kinh, so_huyet, ten_huyet);
                pw.println(sql_command);
//                System.out.println(sql_command);
            } else {
                String sql_command = SQLCommand.insert_into("BỆNH_PHDT", ten_benh, -1, -1, phdt);
                pw.println(sql_command);
//                System.out.println(sql_command);
            }
        }
    }

    private String extract_ten_benh(String line){
        String ten_benh;
        if (line.contains(":")) {
            ten_benh = line.substring(line.indexOf(":") + 1, line.length());
        } else if (line.contains("–")){
            ten_benh = line.substring(line.indexOf("–") + 1, line.length());
        } else if (line.contains("-")) {
            ten_benh = line.substring(line.indexOf("-") + 1, line.length());
        } else if (line.contains("a)") && !line.endsWith("a)")) {
            ten_benh = line.substring(line.indexOf(")") + 1, line.length());
        } else if (line.contains("b)") && !line.endsWith("b)")) {
            ten_benh = line.substring(line.indexOf(")") + 1, line.length());
        } else if (line.contains("c)") && !line.endsWith("c)")) {
            ten_benh = line.substring(line.indexOf(")") + 1, line.length());
        } else {
            ten_benh = line;
        }
        return ten_benh.trim();
    }

    private void trim_text(File source, File destination){
        try (BufferedReader br = new BufferedReader(new FileReader(source));
             PrintWriter pw = new PrintWriter(new FileWriter(destination))) {
            String line;
            while ((line = br.readLine()) != null){
                if (line.isEmpty()){
                    continue;
                }
                if (line.endsWith(".")){
                    line = line.substring(0, line.length() - 1);
                }
                pw.println(line.trim().toLowerCase());
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
