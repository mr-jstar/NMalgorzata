/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imageProcessing;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ColorConvertOp;
import java.awt.image.ColorModel;
import java.util.ArrayList;

/**
 *
 * @author Małgorzata
 */
public class GaussianFilter implements BufferedImageOp {

    @Override
    public BufferedImage filter(BufferedImage iInput, BufferedImage oImage) {
        BufferedImage newImage = oImage == null ? new BufferedImage(iInput.getWidth(), iInput.getHeight(), iInput.getType()) : oImage;
        int[] red = new int[9];
        int[] green = new int[9];
        int[] blue = new int[9];
        int alpha;
        int[] gaussianHood = {1, 2, 1, 2, 4, 2, 1, 2, 1};

        int a, b;

        for (int i = 1; i < iInput.getWidth() - 1; i++) {
            for (int j = 1; j < iInput.getHeight() - 1; j++) {
                //wpisuje pixele z 3x3
                int k = 0;
                for (a = i - 1; a <= i + 1; a++) {
                    for (b = j - 1; b <= j + 1; b++) {
                        red[k] = new Color(iInput.getRGB(a, b)).getRed();
                        green[k] = new Color(iInput.getRGB(a, b)).getGreen();
                        blue[k] = new Color(iInput.getRGB(a, b)).getBlue();
                        k++;
                    }
                }
                //kolegcja całego RGB
                ArrayList<int[]> kolRGB = new ArrayList<int[]>();
                kolRGB.add(red);
                kolRGB.add(green);
                kolRGB.add(blue);
                // zmiana wartości ponożonych przez maske filtru Gaussa
                for (k = 0; k < 9; k++) {
                    kolRGB.get(0)[k] = kolRGB.get(0)[k] * gaussianHood[k];
                    kolRGB.get(1)[k] = kolRGB.get(1)[k] * gaussianHood[k];
                    kolRGB.get(2)[k] = kolRGB.get(2)[k] * gaussianHood[k];
                }
                //ostateczny wyniki (i,j)
                int wynikR = 0;
                int wynikG = 0;
                int wynikB = 0;
                for (k = 0; k < 9; k++) {
                    wynikR += kolRGB.get(0)[k];
                    wynikG += kolRGB.get(1)[k];
                    wynikB += kolRGB.get(2)[k];
                }
                wynikR = wynikR / 9;
                if (wynikR > 255) {
                    wynikR = 255;
                }
                wynikG = wynikG / 9;
                if (wynikG > 255) {
                    wynikG = 255;
                }
                wynikB = wynikB / 9;
                if (wynikB > 255) {
                    wynikB = 255;
                }
                //wpisannie nowych wartości do pixela
                newImage.setRGB(i, j, new Color(wynikR, wynikG, wynikB).getRGB());
            }

        }
        return newImage;
    }

    @Override
    public Rectangle2D getBounds2D(BufferedImage src) {
        return new Rectangle(0, 0, src.getWidth(), src.getHeight());
    }

    @Override
    public BufferedImage createCompatibleDestImage(BufferedImage src, ColorModel destCM) {
        if (destCM == null) {
            destCM = src.getColorModel();
        }
        return new BufferedImage(destCM, destCM.createCompatibleWritableRaster(src.getWidth(), src.getHeight()), destCM.isAlphaPremultiplied(), null);
    }

    @Override
    public Point2D getPoint2D(Point2D srcPt, Point2D dstPt) {
        dstPt.setLocation(srcPt);
        return dstPt;
    }

    @Override
    public RenderingHints getRenderingHints() {
        return null;
    }

    @Override
    public String toString() {
        return "GaussianFilter";
    }
}
