/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package todo;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import mydicom.DicomFileContent;
import mydicom.DicomTools;

/**
 *
 * @author Małgorzata
 */
class GammaCorrection{//1
    //mozliwe ze trzeba storzyc tblice luti=255*(i/255)^(1/y)
double y =0.5;//nie wiem ile jeszcze tu nastawić (1/2.2)

public BufferedImage histogramChange(BufferedImage iInput){
        int red;
        int green;
        int blue;
        int alpha;
        int newPixel=0;
        
//        ArrayList<int[]> hisLUT = pictureI(iInput);
        BufferedImage newHistogram = new BufferedImage(iInput.getWidth(),iInput.getHeight(),iInput.getType());
        
        for (int i = 0; i<iInput.getWidth();i++){
            for(int j = 0; j<iInput.getHeight();j++){
                
                //pobranie wszystkich wartości RGB każdego z pixeli
                alpha = new Color(iInput.getRGB(i, j)).getAlpha();
                red = new Color(iInput.getRGB(i, j)).getRed();
                green = new Color(iInput.getRGB(i, j)).getGreen();
                blue = new Color(iInput.getRGB(i,j)).getBlue();
//                System.out.println("red 1 "+red);
                 //przypisanie nowych wartości na podstawie kolekcji histogramLut
                red = (int)(Math.pow(red, y));
                if (red>255){red=255;}
                if (red<0){red=0;}
                green = (int)(Math.pow(green, y));
                if (green>255){green=255;}
                if (green<0){green=0;}
                blue = (int)(Math.pow(blue, y));
                if (blue>255){blue=255;}
                if (blue<0){blue=0;}
//                System.out.println("red 2 "+red);
                
                //tworzenie nowego pixela
//                newPixel = colorToRGB(alpha, red, green, blue);
                
                //wpisanie nowo utworzonego pixela w obraz
//                newHistogram.setRGB(i, j, newPixel);
 //wpisanie nowo utworzonego pixela w obraz
                newHistogram.setRGB(i, j,new Color(red,green,blue).getRGB());
            }
        }return newHistogram;
}

    public BufferedImage interpolationDicom(File fname, Interpolation interpolation) {
        BufferedImage newImage;
        try {
            DicomFileContent fc = DicomTools.openDicomFile(fname);
            interpolation.pictureDicom = fc == null ? null : fc.getImage();
            double height = 1.25;
            double width = 1.25;
            int h = (int) (interpolation.pictureDicom.getHeight() * height);
            int w = (int) (interpolation.pictureDicom.getWidth() * width);
            newImage = new BufferedImage(w, h, BufferedImage.TYPE_USHORT_GRAY);
            Graphics2D g = newImage.createGraphics();
            try {
                g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
                g.setBackground(new Color(0, 0, 0));
                g.clearRect(0, 0, w, h);
                g.drawImage(interpolation.pictureDicom, 0, 0, w, h, null);
            } finally {
                g.dispose();
            }
            return newImage;
        } catch (Exception ex) {
            Logger.getLogger(Interpolation.class.getName()).log(Level.SEVERE, null, ex);
        }
        return interpolation.pictureDicom;
    }

    public BufferedImage endImage(File fname) {
        try {
            DicomFileContent fc = DicomTools.openDicomFile(fname);
            return fc.getImage();
        } catch (Exception e) {
            return null;
        }
    }
}

//musze sie zastanowic czy ta klasa jest w ogóle potrzeba w tym miejscu bo alpha jest stała cały czas
//private static int colorToRGB(int red, int green, int blue, int alpha) {
//        int nPixel = 0;
//        nPixel += alpha << 8; 
//        nPixel = nPixel;
//        nPixel += red; 
//        nPixel = nPixel << 8;
//        nPixel += green; 
//        nPixel = nPixel << 8;
//        nPixel += blue;
//        return nPixel;
// 
//    }
//}
