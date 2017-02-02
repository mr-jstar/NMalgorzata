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
public class Laplace extends AbstractBufferedImageOp {

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
                input = convertToARGB(input);
            case BufferedImage.TYPE_INT_ARGB: 
                operator = new RGBLaplace();
                output = operator.filter(input, output);
                break;
        }
        return output;
    }

    @Override
    public String toString() {
        return "Laplace Edge Sharpener";
    }
}
