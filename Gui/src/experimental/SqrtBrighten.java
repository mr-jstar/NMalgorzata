/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package experimental;

import experimental.MyLookUp;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ByteLookupTable;
import java.awt.image.ColorConvertOp;
import java.awt.image.ColorModel;
import java.awt.image.LookupTable;

/**
 *
 * @author jstar
 */
public class SqrtBrighten implements BufferedImageOp {
    
    private static final byte[] data = new byte[256];

    static {
        for (int i = 0; i < 256; i++) {
            data[i] = (byte) (Math.sqrt((float) i / 255) * 255);
        }
    }

    @Override
    public BufferedImage filter(BufferedImage input, BufferedImage output) {
        LookupTable table = new ByteLookupTable(0, data);
        MyLookUp op = new MyLookUp(table, null);
        return op.filter(input, output);
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
        return "Sqrt Brighten";
    }
}
