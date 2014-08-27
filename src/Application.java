import builddb.*;
import userinterface.APointLocator;

import javax.swing.*;
import java.io.File;
import java.io.IOException;

/**
 * Created by Ani on 8/2/14.
 */
public class Application {

    public static void main(String[] args) {
        // current working directory
        System.out.println(System.getProperty("user.dir"));

//        extract_huyet_details();

        TextPreprocessor.trim_and_lower_case(new File("./text_src/3"), true);
        TextPreprocessor.trim_and_lower_case(new File("./text_src/4"), true);
        TextPreprocessor.trim_and_lower_case(new File("./text_src/5"), true);
        TextPreprocessor.trim_and_lower_case(new File("./text_src/6"), true);
        TextPreprocessor.trim_and_lower_case(new File("./text_src/Benh"), false);

        BenhRemastered benh = new BenhRemastered();
        benh.work();
        benh.write_sql_statements_nhombenh(new File("./sql/nhom_benh.sql"));

        JFrame frame = new JFrame("Acupuncture Image");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setContentPane(new APointLocator().getPanel());
        frame.pack();
        frame.setVisible(true);
    }

    public static void extract_huyet_details() {
        ConsoleFeedback.notify_start();
        // Kinh 0
        File kinh0_in = new File("./text_src/Kinh0");
        File kinh0_out = new File("./sql/kinh0.sql");
        try {
            boolean new_file = kinh0_out.createNewFile();
            ConsoleFeedback.create_file(kinh0_out, new_file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Kinh0 kinh00 = new Kinh0();
        kinh00.write_sql_statements(kinh0_in, kinh0_out);

        // Kinh 1
        File kinh1_in = new File("./text_src/Kinh1-14");
        File kinh1_out = new File("./sql/kinh1-14.sql");
        try {
            boolean new_file = kinh1_out.createNewFile();
            ConsoleFeedback.create_file(kinh1_out, new_file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Kinh1 kinh01 = new Kinh1();
        kinh01.write_sql_statements(kinh1_in, kinh1_out);

        ConsoleFeedback.notify_end();
    }
}