package userinterface;

import java.io.*;

/**
 * Created by NguyenTA on 17/8/2014.
 */
public class Serializer {
    File data;

    public Serializer(File data) {
        this.data = data;
    }

    public Object deserialize() {
        Object object = null;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(data))) {
            object = ois.readObject();
        } catch (Exception e) {
            System.out.println("deserialization failure");
            e.printStackTrace();
        }
        return object;
    }

    public void serialize(Object object) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(data))) {
            oos.writeObject(object);
        } catch (IOException ex) {
            System.out.println("serialization failure");
            ex.printStackTrace();
        }
    }
}
