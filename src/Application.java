import java.io.*;

/**
 * Created by Ani on 8/2/14.
 */
public class Application {

    public static void main(String[] args){
        System.out.println(System.getProperty("user.dir"));
        File benh_in = new File("./text_src/Benh");
        File benh_out = new File("./sql/benh.sql");
        try{
            benh_out.createNewFile();
        } catch (IOException e){
            e.printStackTrace();
        }
        Benh benh = new Benh();
        benh.write_sql_statements(benh_in,benh_out);
    }

    public static void extract_huyet_details(){
        // Kinh 0
        File kinh0_in = new File("./text_src/Kinh0");
        File kinh0_out = new File("./sql/kinh0.sql");
        try{
            kinh0_out.createNewFile();
        } catch (IOException e){
            e.printStackTrace();
        }
        Kinh0 kinh00 = new Kinh0();
        kinh00.write_sql_statements(kinh0_in,kinh0_out);

        // Kinh 1
        File kinh1_in = new File("./text_src/Kinh1-14");
        File kinh1_out = new File("./sql/kinh1-14.sql");
        try{
            kinh1_out.createNewFile();
        } catch (IOException e){
            e.printStackTrace();
        }
        Kinh1 kinh01 = new Kinh1();
        kinh01.write_sql_statements(kinh1_in, kinh1_out);
    }
}
