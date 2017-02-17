/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

/**
 *
 * @author jstar
 */
public class ImageManager implements Iterable<BufferedImageOp> {

    private BufferedImage img;
    private final static ScaleImage scaler = new ScaleImage();
    private final JLabel imagePanel;
    private final ArrayList<BufferedImageOp> filters;

    public ImageManager(JLabel panel) {
        img = null;
        imagePanel = panel;
        filters = new ArrayList<>();
    }

    public void updateImg(BufferedImage image) {
        img = image;
    }

    public void addFilter(BufferedImageOp f) {
        filters.add(f);
    }

    public void rmFilter(BufferedImageOp f) {
        filters.remove(f);
    }

    public void clearFilters() {
        filters.clear();
    }
    
    @Override
    public Iterator<BufferedImageOp> iterator() {
        return filters.iterator();
    }
    
    public int size() {
        return filters.size();
    }

    public void repaint(double scale) {
        //System.err.println("ImageManager::repaint");
        if (img == null) {
            return;
        }
        try {
            BufferedImage sc = scaler.makeImage(img, scale, scale);
            for (BufferedImageOp f : filters) {
                sc = f.filter(sc, null);
            }
            imagePanel.setIcon(new ImageIcon(sc));
        } catch (Exception ex) {
            Logger.getLogger(DicomExplorer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
