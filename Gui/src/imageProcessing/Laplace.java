/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imageProcessing;

import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ColorConvertOp;
import java.awt.image.ColorModel;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.awt.image.LookupOp;
import java.awt.image.LookupTable;
import java.awt.image.RescaleOp;

/**
 *
 * @author jstar
 */
public class Laplace implements BufferedImageOp {

    @Override
    public BufferedImage filter(BufferedImage input, BufferedImage output) {

        switch (input.getType()) {
            case BufferedImage.TYPE_USHORT_GRAY:
                //Kernel k = new Kernel(3, 3, new float[]{0f, 0f, 0f, -1f, 1f, 0f, 0f, 0f, 0f}); // Horizontal
                //Kernel k = new Kernel(3, 3, new float[]{-1f, -1f, 1f, -1f, -2f, 1f, 1f, 1f, 1f}); // SE Gradient
                //Kernel k = new Kernel(3, 3, new float[]{0f, -1f, 1f,  -1f, 1f,  1f, -1f, 0f, 1f}); // Emboss
                Kernel k = new Kernel(3, 3, new float[]{0f, -1f, 0f, -1f, 4f, -1f, 0f, -1f, 0f}); // Laplace
                BufferedImageOp operator = new ConvolveOp(k);
                output = operator.filter(input, null);
                break;
            default:
                input = BufferedImageTools.convertToARGB(input);
            case BufferedImage.TYPE_INT_ARGB:  // to trzeba poprawić!
                operator = new RGBLaplace();
                output = operator.filter(input, output);
                break;
        }
        return output;
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
        return "Laplace Edge Sharpener";
    }
}
