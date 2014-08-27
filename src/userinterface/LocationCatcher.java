package userinterface;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created by NguyenTA on 17/8/2014.
 */
public class LocationCatcher extends MouseAdapter {

    private LocationStorage locationStorage;
    private Serializer serializer;

    public LocationCatcher(LocationStorage locationStorage, Serializer serializer) {
        this.locationStorage = locationStorage;
        this.serializer = serializer;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            System.out.println("point addition");
            locationStorage.addPoint(e.getPoint());
            e.getComponent().repaint();
        }
        if (e.getButton() == MouseEvent.BUTTON3) {
            System.out.println("serialization");
            serializer.serialize(locationStorage);
        }
    }
}
