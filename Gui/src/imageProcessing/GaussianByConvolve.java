/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imageProcessing;

import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;

/**
 *
 * @author jstar
 */
public class GaussianByConvolve extends AbstractBufferedImageOp {

    @Override
    public BufferedImage filter(BufferedImage input, BufferedImage output) {
        float oneF = 1f / 15f;
        Kernel k = new Kernel(3, 3, new float[]{oneF, 2 * oneF, oneF, 2 * oneF, 4 * oneF, 2 * oneF, oneF, 2 * oneF, oneF});
        BufferedImageOp operator = new ConvolveOp(k);
        output = operator.filter(input, null);
        return output;
    }

    @Override
    public String toString() {
        return "Gaussian Blur";
    }
}
