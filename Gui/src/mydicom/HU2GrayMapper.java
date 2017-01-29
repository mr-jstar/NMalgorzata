/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mydicom;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

/**
 *
 * @author jstar
 */
public class HU2GrayMapper implements HUMapper {

    final static int MX = 2 * Short.MAX_VALUE;

    @Override
    public BufferedImage map(int rows, int cols, short[] hu) {
        if (rows * cols != hu.length) {
            return null;
        }

        BufferedImage bImg = new BufferedImage(cols, rows, BufferedImage.TYPE_USHORT_GRAY);

        for (int i = 0; i < hu.length; i++) {
            int x = i % cols;
            int y = i / cols;
            double hd = (hu[i] + 1000) / 4000.0;
            short pi = (short) (MX * hd);
            bImg.setRGB(x, y, pi);
        }
        return bImg;
    }

}