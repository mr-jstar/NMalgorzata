/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import java.awt.image.BufferedImage;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 *
 * @author jstar
 */
public class ZoomSliderListener implements ChangeListener {

    private BufferedImage obrazek;
    private final ScaleImage SCI = new ScaleImage();
    private final JLabel imagePanel;

    public ZoomSliderListener(JLabel panel) {
        obrazek = null;
        imagePanel = panel;
    }

    public void updateImg(BufferedImage img) {
        obrazek = img;
    }

    @Override
    public void stateChanged(ChangeEvent ce) {
        if (obrazek == null) {
            return;
        }
        double range = ((JSlider) ce.getSource()).getValue();
//            System.out.println("range "+range);
        double multi = (range / 100);
//            System.out.println("multi "+multi);
//            final BufferedImage obrazek = SCI.getPicture(jLabel1);
        try {
            BufferedImage sc = SCI.makeImage(obrazek, multi, multi);
            ImageIcon iconS = new ImageIcon(sc);
            imagePanel.setIcon(iconS);
        } catch (Exception ex) {
            Logger.getLogger(DicomExplorer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
