/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mydicom;

import java.awt.Color;
import java.awt.image.BufferedImage;

/**
 *
 * @author jstar
 */
public class HU2RGBMapperByJstar implements HUMapper {

    // jstar wymyślał
    final static short[] HS = {-1001, 0, 100, 200, 1000, 4000};
    final static short[] RS = {0, 127, 127, 127, 168, 255};
    final static short[] GS = {0, 127, 127, 168, 50, 255};
    final static short[] BS = {0, 127, 168, 168, 194, 255};

    @Override
    public BufferedImage map(int rows, int cols, short[] hu) {
        if (rows * cols != hu.length) {
            return null;
        }

        BufferedImage bImg = new BufferedImage(cols, rows, BufferedImage.TYPE_INT_ARGB);
        int red = 0, green = 0, blue = 0;

        for (int i = 0; i < hu.length; i++) {
            int x = i % cols;
            int y = i / cols;
            int j = 0;
            while (j < HS.length && HS[j] < hu[i]) {
                j++;
            }
            if (j == HS.length) {
                red = RS[HS.length - 1];
                green = GS[HS.length - 1];
                blue = BS[HS.length - 1];
            } else {
                float delta = ((float) hu[i] - HS[j - 1]) / (HS[j] - HS[j - 1]);
                red = (int) (RS[j - 1] + (RS[j] - RS[j - 1]) * delta);
                green = (int) (GS[j - 1] + (GS[j] - GS[j - 1]) * delta);
                blue = (int) (BS[j - 1] + (BS[j] - BS[j - 1]) * delta);
            }
            int color = (new Color(red, green, blue)).getRGB();
            //if( j > 1 ) System.err.println(hu[i] + " -> " + j + "  -> " + red + "," + green + "," + blue + "->" +color);
            bImg.setRGB(x, y, color);
        }
        return bImg;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof HU2RGBMapperByJstar;
    }

    @Override
    public int hashCode() {
        return getClass().toString().hashCode();
    }

}
