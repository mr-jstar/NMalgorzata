/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imageProcessing;

import java.awt.image.BufferedImage;

/**
 *
 * @author jstar
 */
public abstract class TransferFilter extends PointFilter {

    protected int[] rTable, gTable, bTable;
    protected boolean initialized = false;

    public TransferFilter() {
        canFilterIndexColorModel = true;
    }

    @Override
    public int filterRGB(int x, int y, int rgb) {
        int a = rgb & 0xff000000;
        int r = (rgb >> 16) & 0xff;
        int g = (rgb >> 8) & 0xff;
        int b = rgb & 0xff;
        r = rTable[r];
        g = gTable[g];
        b = bTable[b];
        return a | (r << 16) | (g << 8) | b;
    }

    @Override
    public BufferedImage filter(BufferedImage src, BufferedImage dst) {
        if (!initialized) {
            initialize();
        }
        return super.filter(src, dst);
    }

    protected void initialize() {
        rTable = gTable = bTable = makeTable();
        initialized = true;
    }

    protected int[] makeTable() {
        int[] table = new int[256];
        for (int i = 0; i < 256; i++) {
            table[i] = clamp((int) (255 * transferFunction(i / 255.0f)));
            //System.out.print( " " + table[i] + " ");
        }
        //System.out.println("");
        return table;
    }

    protected float transferFunction(float v) {
        return 0;
    }

    public int[] getLUT() {
        if (!initialized) {
            initialize();
        }
        int[] lut = new int[256];
        for (int i = 0; i < 256; i++) {
            lut[i] = filterRGB(0, 0, (i << 24) | (i << 16) | (i << 8) | i);
        }
        return lut;
    }

    private static int clamp(int c) {
        if (c < 0) {
            return 0;
        }
        if (c > 255) {
            return 255;
        }
        return c;
    }

}
