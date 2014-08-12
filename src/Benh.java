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
            temp.createNewFile();
        } catch (IOException e){
            e.printStackTrace();
        }
        trim_text(source, temp);
        populate_nhom_benh(temp, destination);
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
            SQLCommand sql = new SQLCommand();

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
                        String sql_command = sql.insert_into("NHÓM_BỆNH", nhom_benh, benh);
                        pw.println(sql_command);
                        System.out.println(sql_command);
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
                    if (prev_line.contains(":")) {
                        ten_benh = prev_line.substring(prev_line.indexOf(":")+1, prev_line.length()).trim();
                    } else if (prev_line.contains("–")){
                        ten_benh = prev_line.substring(prev_line.indexOf("–")+1, prev_line.length()).trim();
                    } else if (prev_line.contains("-")) {
                        ten_benh = prev_line.substring(prev_line.indexOf("-")+1, prev_line.length()).trim();
                    } else if (prev_line.contains("a)") && !prev_line.endsWith("a)")) {
                        ten_benh = prev_line.substring(prev_line.indexOf(")")+1, prev_line.length()).trim();
                    } else if (prev_line.contains("b)") && !prev_line.endsWith("b)")) {
                        ten_benh = prev_line.substring(prev_line.indexOf(")")+1, prev_line.length()).trim();
                    } else if (prev_line.contains("c)") && !prev_line.endsWith("c)")) {
                        ten_benh = prev_line.substring(prev_line.indexOf(")")+1, prev_line.length()).trim();
                    } else {
                        ten_benh = prev_line;
                    }
                    benh_array.add(ten_benh);
                }
            }
            for (String benh : benh_array){
                String sql_command = sql.insert_into("NHÓM_BỆNH", nhom_benh, benh);
                pw.println(sql_command);
                System.out.println(sql_command);
            }
        } catch (IOException e){
            e.printStackTrace();
        }
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
