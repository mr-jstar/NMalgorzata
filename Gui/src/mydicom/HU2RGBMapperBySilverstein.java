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
public class HU2RGBMapperBySilverstein implements PixelDataMapper {

    // Silverstein, Parsad, Tsirline, Journal of Biomed. Informatics, 41 (2008) 927-935
    final static short[] HS = {-1001, -500, -80,  60, 1000, 4000};   // -1000, -600 to -400, -100 to -60, 40 to 80, 400 to 1000
    final static short[] RS = {    0,  194, 194, 110,  255, 102};   // 0,194,194,102 to 153,255
    final static short[] GS = {    0,  105, 166,   0,  255, 178};   // 0,105,166,0,255
    final static short[] BS = {    0,   82, 115,   0,  255, 255};   // 0,82,115,0,255

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
            if (j == 0) {
                red = RS[0];
                green = GS[0];
                blue = BS[0];
            } else if (j == HS.length) {
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
        return o instanceof HU2RGBMapperBySilverstein;
    }

    @Override
    public int hashCode() {
        return getClass().toString().hashCode();
    }

}
