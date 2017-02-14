/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mydicom;

import java.awt.image.BufferedImage;

/**
 *
 * @author jstar
 */
public class RTG2GrayMapper implements HUMapper {

    @Override
    public BufferedImage map(int rows, int cols, short[] rtg) {
        if (rows * cols != rtg.length) {
            return null;
        }

        BufferedImage bImg = new BufferedImage(cols, rows, BufferedImage.TYPE_USHORT_GRAY);

        int min = Short.MAX_VALUE, max = 0;

        for (int i = 0; i < rtg.length; i++) {
            int x = i % cols;
            int y = i / cols;
            int pi = rtg[i];
            if (pi < min) {
                min = pi;
            }
            if (pi > max) {
                max = pi;
            }
            pi *= 2;
            bImg.setRGB(x, y, pi);
        }
        //System.out.println("RTG2GrayMapper:  " + cols + "x" + rows + " <" + min + " : " + max + ">");
        return bImg;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof RTG2GrayMapper;
    }

    @Override
    public int hashCode() {
        return getClass().toString().hashCode();
    }
}
