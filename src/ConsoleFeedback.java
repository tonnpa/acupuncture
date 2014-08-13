import java.io.File;

/**
 * Created by Ani on 8/13/14.
 */
public class ConsoleFeedback {

    public static void create_file(File file, boolean creation_result){
        if (creation_result){
            System.out.println("-> " + file.getName() + " successfully created");
        } else {
            System.out.println("-> " + file.getName() + " already exists, work will be done on that instance");
        }
    }
}
