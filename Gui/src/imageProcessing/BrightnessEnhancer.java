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
import java.awt.image.LookupOp;
import java.awt.image.LookupTable;
import java.awt.image.RescaleOp;

/**
 *
 * @author jstar
 */
public class BrightnessEnhancer implements BufferedImageOp {

    private final float scale;
    private final float offset;

    public BrightnessEnhancer(float scale, float offset) {
        this.scale = scale;
        this.offset = offset;
    }

    @Override
    public BufferedImage filter(BufferedImage input, BufferedImage output) {
        BufferedImageOp operator = null;
        switch (input.getType()) {
            case BufferedImage.TYPE_USHORT_GRAY:
                operator = new RescaleOp(scale, offset, null);
                break;
            default:
                input = BufferedImageTools.convertToARGB(input);
            case BufferedImage.TYPE_INT_ARGB:  // to trzeba poprawić!
                LookupTable lookup = new LookupTable(0, 4) {
                    @Override
                    public int[] lookupPixel(int[] src, int[] dest) {
                        dest[0] = (int) (scale * src[0] + offset);
                        dest[1] = (int) (scale * src[1] + offset);
                        dest[2] = (int) (scale * src[2] + offset);
                        dest[3] = (int) (scale * src[3] + offset);
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
        return "BrightnestEnhancer";
    }
}
