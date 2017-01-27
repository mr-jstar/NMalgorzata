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
        RescaleOp operator = new RescaleOp(scale, offset, null);
        return operator.filter(input, output);
    }

    @Override
    public Rectangle2D getBounds2D(BufferedImage src) {
        return new Rectangle(0,0,src.getWidth(),src.getHeight());
    }

    @Override
    public BufferedImage createCompatibleDestImage(BufferedImage src, ColorModel destCM) {
        if( src.getColorModel().equals(destCM)) 
            return src;
        else {
            ColorConvertOp op = new ColorConvertOp(destCM.getColorSpace(),null);
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
        return "ContrastEnhancer";
    }
}
