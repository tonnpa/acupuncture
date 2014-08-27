package userinterface;

import javax.swing.*;
import java.io.File;

/**
 * Created by NguyenTA on 21/8/2014.
 */
public class APointLocator {
    private JPanel panel1;
    private JComboBox comboBox1;
    private JPanel image;

    public APointLocator() {
        Serializer serializer = new Serializer(new File("./data/locations"));
        LocationStorage locationStorage = (LocationStorage) serializer.deserialize();
        if (locationStorage == null) {
            locationStorage = new LocationStorage();
        }
        LocationCatcher locationCatcher = new LocationCatcher(locationStorage, serializer);
        ImageLoader imageLoader = new ImageLoader(locationStorage, locationCatcher);
        imageLoader.loadImageFromFile(new File("./image/female-acupuncture-model.jpg"));

        image.add(imageLoader);
    }

    public JPanel getPanel() {
        return panel1;
    }
}
