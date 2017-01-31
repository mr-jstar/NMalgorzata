/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imageProcessing;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ColorConvertOp;
import java.awt.image.ColorModel;
import java.awt.image.LookupOp;
import java.awt.image.LookupTable;
import java.awt.image.RescaleOp;

/**
 *
 * @author jstar
 */
public class Negative implements BufferedImageOp {

    private static BufferedImage convertToARGB(BufferedImage image) {
        BufferedImage newImage = new BufferedImage(
                image.getWidth(), image.getHeight(),
                BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = newImage.createGraphics();
        g.drawImage(image, 0, 0, null);
        g.dispose();
        return newImage;
    }

    @Override
    public BufferedImage filter(BufferedImage input, BufferedImage output) {
        BufferedImageOp operator = null;
        switch (input.getType()) {
            case BufferedImage.TYPE_USHORT_GRAY:
                operator = new RescaleOp(-1f, (float) (1 << 16 - 1), null);
                break;
            default:
                input = convertToARGB(input);
            case BufferedImage.TYPE_INT_ARGB:
                LookupTable lookup = new LookupTable(0, 4) {
                    @Override
                    public int[] lookupPixel(int[] src, int[] dest) {
                        dest[0] = (int) (255 - src[0]);
                        dest[1] = (int) (255 - src[1]);
                        dest[2] = (int) (255 - src[2]);
                        return dest;
                    }
                };
                operator = new LookupOp(lookup, new RenderingHints(null));
                break;
        }
        return operator.filter(input, output);
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
        return "Negative";
    }
}
