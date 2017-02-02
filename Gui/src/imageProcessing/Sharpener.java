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

/**
 *
 * @author jstar
 */
public class Sharpener implements BufferedImageOp {

    @Override
    public BufferedImage filter(BufferedImage input, BufferedImage output) {
        Kernel k = new Kernel(3, 3, new float[]{-1f, -1f, -1f, -1f, 9f, -1f, -1f, -1f, -1f});
        BufferedImageOp operator = new ConvolveOp(k);
        output = operator.filter(input, null);
        return output;
    }

    @Override
    public Rectangle2D getBounds2D(BufferedImage src) {
        return new Rectangle(0, 0, src.getWidth(), src.getHeight());
    }

    @Override
    public BufferedImage createCompatibleDestImage(BufferedImage src, ColorModel destCM) {
        if (src.getColorModel().equals(destCM)) {
            return src;
        } else {
            ColorConvertOp op = new ColorConvertOp(destCM.getColorSpace(), null);
            return op.filter(src, null);
        }
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
        return "Sharpener";
    }
}
