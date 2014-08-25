import java.io.*;

/**
 * Created by Ani on 8/2/14.
 */
public class Application {

    public static void main(String[] args){
        System.out.println("number of argumentums:" + args.length);
        if (args.length > 0){
            System.out.println(args[0]);
        }
        // /Users/Ani/IdeaProjects/Acupuncture
        System.out.println(System.getProperty("user.dir"));

//        extract_huyet_details();

//        extract_benh_details();
        Benh cleaner = new Benh();
        cleaner.clean_input_files();

        BenhRemastered benh = new BenhRemastered();
        benh.work();
    }

    public static void extract_huyet_details(){
        ConsoleFeedback.notify_start();
        // Kinh 0
        File kinh0_in = new File("./text_src/Kinh0");
        File kinh0_out = new File("./sql/kinh0.sql");
        try{
            boolean new_file = kinh0_out.createNewFile();
            ConsoleFeedback.create_file(kinh0_out, new_file);
        } catch (IOException e){
            e.printStackTrace();
        }
        Kinh0 kinh00 = new Kinh0();
        kinh00.write_sql_statements(kinh0_in,kinh0_out);

        // Kinh 1
        File kinh1_in = new File("./text_src/Kinh1-14");
        File kinh1_out = new File("./sql/kinh1-14.sql");
        try{
            boolean new_file = kinh1_out.createNewFile();
            ConsoleFeedback.create_file(kinh1_out, new_file);
        } catch (IOException e){
            e.printStackTrace();
        }
        Kinh1 kinh01 = new Kinh1();
        kinh01.write_sql_statements(kinh1_in, kinh1_out);

        ConsoleFeedback.notify_end();
    }

    public static void extract_benh_details(){
        ConsoleFeedback.notify_start();

        File benh_in = new File("./text_src/Benh");
        File benh_out = new File("./sql/benh.sql");
        if (benh_out.exists()){
            benh_out.delete();
        }
        try{
            boolean new_file = benh_out.createNewFile();
            ConsoleFeedback.create_file(benh_out, new_file);
        } catch (IOException e){
            e.printStackTrace();
        }
        Benh benh = new Benh();
        Integer number = 5;
        benh.write_sql_statements(benh_in,benh_out);

        ConsoleFeedback.notify_end();
    }
}
