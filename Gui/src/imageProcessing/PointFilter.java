/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imageProcessing;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;

/**
 *
 * @author jstar
 */
public abstract class PointFilter extends AbstractBufferedImageOp {

    protected boolean canFilterIndexColorModel = false;

    public BufferedImage filter(BufferedImage src, BufferedImage dst) {
        int width = src.getWidth();
        int height = src.getHeight();
        int type = src.getType();
        WritableRaster srcRaster = src.getRaster();

        if (dst == null) {
            dst = createCompatibleDestImage(src, null);
        }
        WritableRaster dstRaster = dst.getRaster();

        setDimensions(width, height);

        int[] inPixels = new int[width];
        for (int y = 0; y < height; y++) {
            // We try to avoid calling getRGB on images as it causes them to become unmanaged, causing horrible performance problems.
            if (type == BufferedImage.TYPE_INT_ARGB) {
                srcRaster.getDataElements(0, y, width, 1, inPixels);
                for (int x = 0; x < width; x++) {
                    inPixels[x] = filterRGB(x, y, inPixels[x]);
                }
                dstRaster.setDataElements(0, y, width, 1, inPixels);
            } else {
                src.getRGB(0, y, width, 1, inPixels, 0, width);
                for (int x = 0; x < width; x++) {
                    inPixels[x] = filterRGB(x, y, inPixels[x]);
                }
                dst.setRGB(0, y, width, 1, inPixels, 0, width);
            }
        }

        return dst;
    }

    public void setDimensions(int width, int height) {
    }

    public abstract int filterRGB(int x, int y, int rgb);
}
