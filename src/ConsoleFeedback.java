import java.io.File;

/**
 * Created by Ani on 8/13/14.
 */
public class ConsoleFeedback {

    private static int call_depth = 0;

    public static void create_file(File file, boolean creation_result){
        print_tabs();
        if (creation_result){
            System.out.println("-> " + file.getName() + " successfully created");
        } else {
            System.out.println("-> " + file.getName() + " already exists, work will be done on that instance");
        }
    }

    public static void notify_start(){
        StackTraceElement[] stack = Thread.currentThread().getStackTrace();
        print_tabs();
        System.out.println(stack[2].getMethodName() + " has started");

        call_depth++;
    }

    public static void notify_end(){
        call_depth--;

        StackTraceElement[] stack = Thread.currentThread().getStackTrace();
        print_tabs();
        System.out.println(stack[2].getMethodName() + " has finished");
    }

    private static void print_tabs(){
        for (int i = 0; i < call_depth; i++) {
            System.out.print("\t");
        }
    }
}
