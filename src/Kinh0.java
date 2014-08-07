import java.io.*;

/**
 * Created by Ani on 8/2/14.
 */
public class Kinh0 {
    private static final int so_kinh = 0;

    public void write_sql_statements(File source, File destination){
        try (BufferedReader br = new BufferedReader(new FileReader(source));
             PrintWriter pw = new PrintWriter(new FileWriter(destination))) {
            // first two lines Kinh description;
            br.readLine();
            br.readLine();
            String line;
            while ((line = br.readLine()) != null){
                line = line.toLowerCase();
                // so huyet, ten huyet
                String[] fields = line.split(" ");
                int so_huyet = Integer.parseInt(fields[0]);
                String vi_tri = "", ten_huyet = "";
                // extract vi_tri embedded in name between brackets ()
                if (line.contains("(")){
                    vi_tri += line.substring(line.indexOf('(')+1, line.indexOf(')')).trim();
                    ten_huyet = line.substring(0, line.indexOf('(')).replaceFirst(fields[0],"").trim();
                } else{
                    ten_huyet = line.replaceFirst(fields[0],"").trim();
                }
                // VT
                line = (br.readLine()).toLowerCase();
                if (line.endsWith(".")){
                    line = line.substring(0, line.length()-1);
                }
                fields = line.split(":");
                if (vi_tri.length() > 1){
                    vi_tri += ", ";
                }
                vi_tri += fields[1].trim();
                // CT
                line = (br.readLine()).toLowerCase();
                if (line.endsWith(".")){
                    line = line.substring(0, line.length()-1);
                }
                fields = line.split(":");
                String[] chu_tri_array = fields[1].split(",");
                // LH
                line = (br.readLine()).toLowerCase();
                if (line.endsWith(".")){
                    line = line.substring(0, line.length()-1);
                }
                fields = line.split(":");
                String lay_huyet = fields[1].trim();
                // TT
                line = (br.readLine()).toLowerCase();
                if (line.endsWith(".")){
                    line = line.substring(0, line.length()-1);
                }
                fields = line.split(":");
                String thu_thuat = fields[1].trim();

//                System.out.println(so_huyet);
//                System.out.println(vi_tri);
//                System.out.println(thu_thuat);
//                System.out.println(lay_huyet);

//                SỐ_KINH integer NOT NULL,
//                SỐ_HUYỆT integer NOT NULL,
//                TÊN_HUYỆT varchar(128) NOT NULL,
//                VỊ_TRÍ varchar(128) NOT NULL,
//                LẤY_HUYỆT varchar(256),
//                THỦ_THUẬT varchar(256)

                String sql_command = "insert into HUYỆT values(" +
                        so_kinh + ", " +
                        so_huyet + ", " +
                        "\'" + ten_huyet + "\', " +
                        "\'" + vi_tri + "\', " +
                        "\'" + lay_huyet + "\', " +
                        "\'" + thu_thuat + "\'" +
                        ");";

                pw.println(sql_command);

                for (String chu_tri : chu_tri_array){
                    sql_command = "insert into HUYỆT_CHỦ_TRỊ values(" +
                            so_kinh + ", " +
                            so_huyet + ", " +
                            "\'" + chu_tri.trim() + "\'" +
                            ");";

                    pw.println(sql_command);
                }
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
