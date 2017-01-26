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
public class StretchingImage{

    
    private static ArrayList<int[]> histLUTi(BufferedImage iInput){
        //określenie zakresu
        ArrayList<int[]> LUTi = new ArrayList<int[]>();
        int rValMin=255;
        int rValMax=0;
        int gValMin=255;
        int gValMax=0;
        int bValMin=255;
        int bValMax=0;
//        for(int i=0; i<imageInput.getWidth(); i++) {
//            for(int j=0; j<imageInput.getHeight(); j++) {               
//                int rVal = new Color(imageInput.getRGB (i, j)).getRed();
//                if(rVal<rValMin) {rValMin = rVal;}
//                if(rVal>rValMax) {rValMax = rVal;}
//                int gVal = new Color(imageInput.getRGB (i, j)).getGreen();
//                if(gVal<gValMin) {gValMin = gVal;}
//                if(gVal>gValMax) {gValMax = gVal;}
//                int bVal = new Color(imageInput.getRGB (i, j)).getBlue();
//                if(bVal<bValMin) {bValMin = bVal;}
//                if(bVal>bValMax) {bValMax = bVal;}
//            }
//        }   
        
        //tablice odpowiadajace poszczególnym kolorom(o dłogości mozliwych wystepujacych odcieniach)
        int pixel = iInput.getWidth()*iInput.getHeight();
        int[] rParameter = new int[pixel];
        int[] gParameter = new int[pixel];
        int[] bParameter = new int[pixel];
         //petle for przechodza po poszczególnych elemntach tablic zawierajacych kolory nadajac im wartość 0
        for(int i=0; i<rParameter.length; i++) rParameter[i] = 0;
        for(int i=0; i<gParameter.length; i++) gParameter[i] = 0;
        for(int i=0; i<bParameter.length; i++) bParameter[i] = 0;
        
        //poprawnie przelicza
            int k = 0;
            int rVal;
            int gVal;
            int bVal;
        for (int i = 0; i <iInput.getWidth(); i++){
            for (int j =0; j < iInput.getHeight(); j++){
                
             rVal = new Color(iInput.getRGB (i, j)).getRed();
             gVal = new Color(iInput.getRGB (i, j)).getGreen();
             bVal = new Color(iInput.getRGB (i, j)).getBlue();
             int rLicz=(rVal-rValMin)*255;
             int rMian=rValMax-rValMin;
             rVal=(int)(rLicz/rMian);
             if (rVal>255){
                 rVal=255;}
             if (rVal<0){
                 rVal=0;}
             int gLicz=(gVal-gValMin)*255;
             int gMian=gValMax-gValMin;
             gVal=(int)(gLicz/gMian);
             if (gVal>255){
                 gVal=255;}
             if (gVal<0){
                 gVal=0;}
             int bLicz=(bVal-bValMin)*255;
             int bMian=bValMax-bValMin;
             bVal=(int)(bLicz/bMian);
             if (bVal>255){
                 bVal=255;}
             if (bVal<0){
                 bVal=0;}
             rParameter[k]=rVal;
             gParameter[k]=gVal;
             bParameter[k]=bVal;
             k++;
//             if (rVal==255)
            }            
        }
        LUTi.add(rParameter);
        LUTi.add(gParameter);
        LUTi.add(bParameter);
     return LUTi;
    }
    
//    private static int colorToRGB(int red, int green, int blue, int alpha) {
//        int nPixel = 0;
//        nPixel += red; 
//        nPixel = nPixel << 8;
//        nPixel += green; 
//        nPixel = nPixel << 8;
//        nPixel += blue;
//        nPixel += alpha << 8; 
//        nPixel = nPixel;
//        
//        return nPixel;
//    }
    
    
    //poprawic reszte kolorów
    public BufferedImage histogramAdd(BufferedImage iInput){
        int red;
        int green;
        int blue;
        int alpha;
        int nPixel = 0;
        
        //kolekcja wyjściowego histogramu
        ArrayList<int[]> HistOutput =histLUTi(iInput);
        BufferedImage histogramEQ = new BufferedImage( iInput.getWidth(), iInput.getHeight(), iInput.getType());
        int k=0;
        for (int i = 0; i < iInput.getWidth(); i++)
        { for (int j = 0; j < iInput.getHeight(); j++)
          {//k cały czas jest jedynka
             red=HistOutput.get(0)[k];
             green=HistOutput.get(1)[k];
             blue=HistOutput.get(2)[k];
              
              //red wychodzi cały czas 15
//              nPixel = colorToRGB(red, green, blue, 0);
//              histogramEQ.setRGB(i, j, nPixel);
              
               //wpisanie nowo utworzonego pixela w obraz
                histogramEQ.setRGB(i, j,new Color(red,green,blue).getRGB());
              //System.out.println("k "+ k +" i "+i+" j "+j+" red "+red);
              k++;
          }
        }
        System.out.println("ciekawe cy to ten nowy obraz :/");
        return histogramEQ;
    }
}
