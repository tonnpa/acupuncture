package userinterface;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by NguyenTA on 17/8/2014.
 */
public class LocationStorage implements Serializable {
    List<Point> locations;

    public LocationStorage() {
        locations = new ArrayList<>();
    }

    public void addPoint(Point newPoint) {
        locations.add(newPoint);
    }

    public List<Point> getPoints() {
        return new ArrayList<>(locations);
    }
}
