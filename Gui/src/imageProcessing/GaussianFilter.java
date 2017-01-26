/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imageProcessing;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 *
 * @author Małgorzata
 */
public class GaussianFilter {
    public BufferedImage histogramChange(BufferedImage iInput){
        int[] red = new int[9];
        int[] green = new int[9];
        int[] blue = new int[9];
        int alpha;
        int[] HoodGaussian = {1,2,1,2,4,2,1,2,1}; 
//        Color[] pixel =new Color[9];
        
//        ArrayList<int[]> hisLUT = pictureI(iInput);
        BufferedImage newHistogram = new BufferedImage(iInput.getWidth(),iInput.getHeight(),iInput.getType());
        int a,b;
        
        for (int i = 1; i<iInput.getWidth()-1;i++){
            for(int j = 1; j<iInput.getHeight()-1;j++){
                //wpisuje pixele z 3x3
                int k = 0;
                for(a = i-1 ;a<=i+1; a++){
                    for(b = j-1 ;b<=j+1;b++){
                            red[k] = new Color(iInput.getRGB(a, b)).getRed();
                            green[k] = new Color(iInput.getRGB(a, b)).getGreen();
                            blue[k] = new Color(iInput.getRGB(a, b)).getBlue();
                            k++;
                }
                }
                //kolegcja całego RGB
                ArrayList<int[]> kolRGB = new ArrayList<int[]>();
                kolRGB.add(red);
                kolRGB.add(green);
                kolRGB.add(blue);
                // zmiana wartości ponożonych przez maske filtru Gaussa
                for(k=0;k<9;k++){
                    kolRGB.get(0)[k] = kolRGB.get(0)[k] * HoodGaussian[k];
                    kolRGB.get(1)[k] = kolRGB.get(1)[k] * HoodGaussian[k];
                    kolRGB.get(2)[k] = kolRGB.get(2)[k] * HoodGaussian[k];
                }
                //ostateczny wyniki (i,j)
                int wynikR= 0;
                int wynikG = 0;
                int wynikB = 0;
                for(k=0; k<9;k++){
                  wynikR += kolRGB.get(0)[k];
                  wynikG += kolRGB.get(1)[k];
                  wynikB += kolRGB.get(2)[k];
                }
                wynikR = wynikR/9;
                if(wynikR>255){wynikR=255;}
                wynikG = wynikG/9;
                if(wynikG>255){wynikG=255;}
                wynikB = wynikB/9;
                if(wynikB>255){wynikB=255;}
                //wpisannie nowych wartości do pixela
                newHistogram.setRGB(i, j,new Color(wynikR,wynikG,wynikB).getRGB());
            }
                
        }            
    return newHistogram;              
}
}
