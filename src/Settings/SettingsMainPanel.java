package Settings;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


@SuppressWarnings("serial")
public class SettingsMainPanel extends JPanel {
    public SettingsMainPanel(){
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        BufferedImage image = null;
        try {                
    		image = ImageIO.read(ClassLoader.getSystemResource("settings/background2.jpg"));
        } catch (IOException ex) {
            System.out.println("Exception caught, exception is: " + ex.getMessage());
        }            
        super.paintComponent(g);
        g.drawImage(image, 0, 0, null); // see javadoc for more info on the parameters
    }                


}

