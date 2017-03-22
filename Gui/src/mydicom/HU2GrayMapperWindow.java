/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mydicom;

import java.awt.Color;
import java.awt.image.BufferedImage;

/**
 *
 * @author Małgorzata
 */
public class HU2GrayMapperWindow implements PixelDataMapper{
    

    final static int MX = 2 * Short.MAX_VALUE;
    private final static int ymin =0;
    private final static int ymax =255;
    
    
    @Override
    public BufferedImage map(int rows, int cols, short[] hu,int windowCenter, int windowWidth) {
        if (rows * cols != hu.length) {
            return null;
        }
        BufferedImage bImg = new BufferedImage(cols, rows, BufferedImage.TYPE_USHORT_GRAY);
        int pi=0;
        final int WCenter = windowCenter +1024;
        final int WWidth = windowWidth+1024;
        for (int i = 0; i < hu.length; i++) {
            int x = i % cols;
            int y = i / cols;
            
            int a= hu[i]+1024;
            if (a<=WCenter-0.5-(((WWidth)-1)/2))
                {pi = ymin;}
            else if (a>WCenter-0.5+(((WWidth)-1)/2))
                {pi= ymax;}
            else 
            {
                pi = (int)((((a)-((WCenter)-0.5))/(((WWidth))-1)+0.5)*255);
            }
            bImg.setRGB(x, y, new Color(pi, pi, pi).getRGB());
        }
        
        
        return bImg;
    }
    @Override
    public boolean equals(Object o) {
        return o instanceof HU2GrayMapperWindow;
    }

    @Override
    public int hashCode() {
        return getClass().toString().hashCode();
    }

    //coś moze byc nie tak taka sama nazwa metody 
//    @Override
//    public BufferedImage map(float slope, float intercept) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
    
}
