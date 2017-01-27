/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package todo;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.Arrays;

/**
 *
 * @author Małgorzata
 */
class MedianaFilter{
    public BufferedImage histogramChange(BufferedImage iInput){
        int[] red = new int[9];
        int[] green = new int[9];
        int[] blue = new int[9];
        int alpha;
        Color[] pixel =new Color[9];
        
//        ArrayList<int[]> hisLUT = pictureI(iInput);
        BufferedImage newHistogram = new BufferedImage(iInput.getWidth(),iInput.getHeight(),iInput.getType());
        
        for (int i = 1; i<iInput.getWidth()-1;i++){
            for(int j = 1; j<iInput.getHeight()-1;j++){
                pixel[0] = new Color(iInput.getRGB(i-1, j-1));
                pixel[1] = new Color(iInput.getRGB(i-1, j));
                pixel[2] = new Color(iInput.getRGB(i-1, j+1));
                pixel[3] = new Color(iInput.getRGB(i, j-1));
                pixel[4] = new Color(iInput.getRGB(i, j));
                pixel[5] = new Color(iInput.getRGB(i, j+1));       
                pixel[6] = new Color(iInput.getRGB(i+1, j-1));
                pixel[7] = new Color(iInput.getRGB(i+1, j));       
                pixel[8] = new Color(iInput.getRGB(i+1, j+1));
                for(int a = 0; a<9; a++){
                    red[a] = pixel[a].getRed();
                    green[a] = pixel[a].getGreen();
                    blue[a] = pixel[a].getBlue();
                }
                Arrays.sort(red);
                Arrays.sort(green);
                Arrays.sort(blue);
                
                //wpisanie nowo utworzonego pixela w obraz
                newHistogram.setRGB(i, j,new Color(red[4],green[4],blue[4]).getRGB());
            }
        }return newHistogram;
    }
    
}
