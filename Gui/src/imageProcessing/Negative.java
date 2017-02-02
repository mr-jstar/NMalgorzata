/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imageProcessing;

import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.LookupOp;
import java.awt.image.LookupTable;
import java.awt.image.RescaleOp;

/**
 *
 * @author jstar
 */
public class Negative extends AbstractBufferedImageOp {

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
                operator = new LookupOp(lookup, null);
                break;
        }

        return operator.filter(input, output);
    }

    @Override
    public String toString() {
        return "Negative";
    }
}
