/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imageProcessing;

import mydicom.DicomFileContent;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import mydicom.DicomTools;

/**
 *
 * @author Małgorzata
 */
public class Interpolation {

    BufferedImage pictureDicom;

    public BufferedImage interpolationDicom(File fname) {
        BufferedImage newImage;
        try {
            DicomFileContent fc = DicomTools.openDicomFile(fname);
            pictureDicom = fc == null ? null : fc.getImage();
            double height = 1.25;
            double width = 1.25;
            int h = (int) (pictureDicom.getHeight() * height);
            int w = (int) (pictureDicom.getWidth() * width);
            newImage = new BufferedImage(w, h,
                    BufferedImage.TYPE_USHORT_GRAY);
            Graphics2D g = newImage.createGraphics();
            try {
                g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                        RenderingHints.VALUE_INTERPOLATION_BICUBIC);//4x4
                g.setBackground(new Color(0, 0, 0));
                g.clearRect(0, 0, w, h);
                g.drawImage(pictureDicom, 0, 0, w, h, null);
            } finally {
                g.dispose();
            }
            return newImage;
        } catch (Exception ex) {
            Logger.getLogger(Interpolation.class.getName()).log(Level.SEVERE, null, ex);
        }
        return pictureDicom;
    }
}
