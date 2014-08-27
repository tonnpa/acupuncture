package userinterface;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by NguyenTA on 16/8/2014.
 */
public class ImageLoader extends JComponent {

    private LocationStorage locationStorage;
    private BufferedImage img;

    public ImageLoader(LocationStorage locationStorage, MouseListener locationCatcher) {
        this.locationStorage = locationStorage;
        addMouseListener(locationCatcher);
    }

    public void loadImageFromFile(File image) {
        try {
            img = ImageIO.read(image);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void paint(Graphics g) {
        g.drawImage(img, 0, 0, null);
        List<Point> points = locationStorage.getPoints();
        g.setColor(Color.RED);
        for (Point p : points) {
            g.drawOval(p.x, p.y, 4, 4);
            g.fillOval(p.x, p.y, 4, 4);
        }
    }

    public Dimension getPreferredSize() {
        if (img == null) {
            return new Dimension(100, 100);
        } else {
            return new Dimension(img.getWidth(), img.getHeight());
        }
    }
}
