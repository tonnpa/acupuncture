import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Ani on 8/4/14.
 */
public class Kinh1 {
//    private static final int so_kinh = 1;

    public void write_sql_statements(File source, File destination){
        try (BufferedReader br = new BufferedReader(new FileReader(source));
             PrintWriter pw = new PrintWriter(new FileWriter(destination))) {
            String line;
            String ten_huyet = "", vi_tri = "", lay_huyet = "", thu_thuat = "";
            List<String> chu_tri_array = new ArrayList<>();
            int so_kinh = -1, so_huyet = -1;

            while ((line = br.readLine()) != null){
                line = line.toLowerCase();
                if (line.endsWith(".")){
                    line = line.substring(0, line.length()-1);
                }

                String regex = "^[i|v|x]+\\s*[0-9]+";
                Pattern pattern = Pattern.compile(regex);
                Matcher matcher = pattern.matcher(line);
                while(matcher.find()) {
                    // new huyet
                    // 1. write out previous huyet to file
                    // 2. clear variables
                    // 3. store information about new huyet

                    // Step 1

                    String sql_command = insert_huyet_command(so_kinh, so_huyet, ten_huyet,
                                                              vi_tri, lay_huyet, thu_thuat);
                    pw.println(sql_command);

                    for (String chu_tri : chu_tri_array){
                        sql_command = insert_chu_tri_command(so_kinh, so_huyet, chu_tri);
                        pw.println(sql_command);
                    }

                    // Step 2

                    so_kinh = so_huyet = -1;
                    ten_huyet = ""; vi_tri = ""; lay_huyet = ""; thu_thuat = "";
                    chu_tri_array.clear();

                    // Step 3
                    String match = line.substring(matcher.start(),matcher.end());
                    so_kinh = romanToInteger(match.replaceAll("\\d","").trim());
                    so_huyet = Integer.parseInt(match.replaceAll("\\D",""));

                    if (line.contains("(")){
                        if (!line.contains(")")){
                            line = line + ")";
                        }
                        vi_tri += line.substring(line.indexOf('(')+1, line.indexOf(')')).trim();
                        System.out.println(so_kinh + " " + so_huyet + " " + vi_tri);
                        ten_huyet = line.substring(matcher.end(), line.indexOf('(')).replaceAll(":","").trim();
                    } else {
                        ten_huyet = line.substring(matcher.end(), line.length()).replaceAll(":","").trim();
                    }
                }
                String[] fields;
                if (line.contains("vt:")){
                    fields = line.split(":");
                    if (vi_tri.length() > 1) {
                        vi_tri += ", ";
                    }
                    vi_tri += fields[1].toLowerCase().trim();
                } else if (line.contains("ct:")){
                    fields = line.split(":");
                    for (String chu_tri : fields[1].trim().split(",")){
                        chu_tri_array.add(chu_tri);
                    }
                } else if (line.contains("lh:")){
                    fields = line.split(":");
                    lay_huyet = fields[1].trim();
                } else if (line.contains("tt:")){
                    fields = line.split(":");
                    thu_thuat = fields[1].trim();
                }
            }
            // write out last huyet
            String sql_command = insert_huyet_command(so_kinh, so_huyet, ten_huyet,
                    vi_tri, lay_huyet, thu_thuat);
            pw.println(sql_command);

            for (String chu_tri : chu_tri_array){
                sql_command = insert_chu_tri_command(so_kinh, so_huyet, chu_tri);
                pw.println(sql_command);
            }

        } catch (IOException e){
            e.printStackTrace();
        }
    }

    private String insert_huyet_command(int so_kinh, int so_huyet,
                                        String ten_huyet, String vi_tri,
                                        String lay_huyet, String thu_thuat){
        return "insert into HUYỆT values(" +
                so_kinh + ", " +
                so_huyet + ", " +
                "\'" + ten_huyet + "\', " +
                "\'" + vi_tri + "\', " +
                "\'" + lay_huyet + "\', " +
                "\'" + thu_thuat + "\'" +
                ");";
    }

    private String insert_chu_tri_command(int so_kinh, int so_huyet, String chu_tri){
        return "insert into HUYỆT_CHỦ_TRỊ values(" +
                so_kinh + ", " +
                so_huyet + ", " +
                "\'" + chu_tri.toLowerCase().trim() + "\'" +
                ");";
    }

    private static int romanToInteger(String val)
    {
        String aux=val.toUpperCase();
        int sum=0, max=aux.length(), i=0;
        while(i<max)
        {
            if ((i+1)<max && valueOf(aux.charAt(i+1))>valueOf(aux.charAt(i)))
            {
                sum+=valueOf(aux.charAt(i+1)) - valueOf(aux.charAt(i));
                i+=2;
            }
            else
            {
                sum+=valueOf(aux.charAt(i));
                i+=1;
            }
        }
        return sum;
    }

    private static int valueOf(Character c)
    {
        char aux = Character.toUpperCase(c);
        switch(aux)
        {
            case 'I':
                return 1;
            case 'V':
                return 5;
            case 'X':
                return 10;
            case 'L':
                return 50;
            case 'C':
                return 100;
            case 'D':
                return 500;
            case 'M':
                return 1000;
            default:
                return 0;
        }
    }

}

