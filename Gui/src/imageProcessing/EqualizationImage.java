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
/**
 * Wyrównywanie histogramu(trzecie przekształcenie)
 * @author Małgorzata
 */
public class EqualizationImage{
 
    /**
     * program sprawdz ile razy dana liczba sie powtarza w obrazie i do każdej 
     * wartości koloru przypisuje ilość wystepowania
     * 
     * @param pictureInput obraz wejsciowy
     * @return histInput histogram obrazu wejściowego
     */
    
    public static BufferedImage imageInput;
    private static ArrayList<int[]> histogramCompute(BufferedImage iInput)
//BufferedImage pictureInput-obraz po kolejnych przekształceniach(nie wiem jeszcze jaka kolejność
    {
       //tablice odpowiadajace poszczególnym kolorom(o dłogości mozliwych wystepujacych odcieniach)
        int[] rParameter = new int[256];
        int[] gParameter = new int[256];
        int[] bParameter = new int[256]; 
        
        //nadanie każdemu elementowi tablicy zerowej wartości
        for(int i=0; i<rParameter.length; i++) rParameter[i] = 0;
        for(int i=0; i<gParameter.length; i++) gParameter[i] = 0;
        for(int i=0; i<bParameter.length; i++) bParameter[i] = 0;
        
        // petla sprawdza każdy element tablicy i zliczca powtarzalność wartości 
        // przypisujac do odpowiedniego elementu 
        for(int i=0; i<iInput.getWidth(); i++) {
            for(int j=0; j<iInput.getHeight(); j++) {
                int red = new Color(iInput.getRGB (i, j)).getRed();
                int green = new Color(iInput.getRGB (i, j)).getGreen();
                int blue = new Color(iInput.getRGB (i, j)).getBlue();
                rParameter[red]++; gParameter[green]++; bParameter[blue]++;
            }
    }
    //wpisanie tablic do kolekcji po zliczeniu powtarzalności
    ArrayList<int[]> histInput = new ArrayList<int[]>();
    histInput.add(rParameter);
    histInput.add(gParameter);
    histInput.add(bParameter);
    return histInput;
    }
    
    /**
     * metoda zajmuje sie sumowanie kolejnych powtórzej i mnożenie ich przez stałą
     * 
     * @param pictureInput obraz wejściowy
     * @return histInput histogram obrazu wejściowego
     * */
    private static ArrayList<int[]> histogramLut(BufferedImage iInput)
    {
        //inicjalizacja kolekcji tablic (do każdej wartości RGB przypisana jest ilość powtórzeń)
        ArrayList<int[]> pictureHistInput = histogramCompute(iInput);
        
        //inicjalizacja kolekcji zawierajaca tablice kolorów każdego pixela wyjściowego
        ArrayList<int[]> tablesLUT = new ArrayList<int[]>();
        
        //tworzenie tablic poszczególnych kolorów i nadanie im długości 256 elementów
        int[] rParameter = new int[256];
        int[] gParameter = new int[256];
        int[] bParameter = new int[256];
        
        //petle for przechodza po poszczególnych elemntach tablic zawierajacych kolory nadajac im wartość 0
        for(int i=0; i<rParameter.length; i++) rParameter[i] = 0;
        for(int i=0; i<gParameter.length; i++) gParameter[i] = 0;
        for(int i=0; i<bParameter.length; i++) bParameter[i] = 0;
        
        long rSum = 0;
        long gSum = 0;
        long bSum = 0;
        
        //Stały współczynnik scali
        float scaleConstant = (float)(255.0/(iInput.getWidth()*iInput.getHeight()));
        
        // ...ValScale -wartości po skalowaniu przez stałą
        for(int i = 0; i < rParameter.length; i++)
        {
            rSum = rSum + pictureHistInput.get(0)[i];//dodanie wejsciowej wartości danego koloru z poszczególnych pixeli
            int rValScale = (int)(rSum * scaleConstant);
            if(rValScale > 255)
            {
                rParameter[i] = 255;
            }
            else rParameter[i] = rValScale;         
            
            gSum = gSum + pictureHistInput.get(1)[i];
            int gValScale = (int)(gSum * scaleConstant);
            if(gValScale > 255)
            {
                gParameter[i] = 255;
            }
            else gParameter[i] = gValScale;
            
            bSum = bSum + pictureHistInput.get(2)[i];//dodanie wejsciowej wartości danego koloru z poszczególnych pixeli
            int bValScale = (int)(bSum * scaleConstant);
            if(bValScale > 255)
            {
                bParameter[i] = 255;
            }
            else bParameter[i] = bValScale;
        }
        
        tablesLUT.add(rParameter);
        tablesLUT.add(gParameter);
        tablesLUT.add(bParameter);
        
        return tablesLUT;
    }
    
    /**
     * zwraca kolor w postaci jednej liczby 
     * 
     * @param red
     * @param green
     * @param blue
     * @param alpha
     * @return nPixel nowa wartość pixela
     */
//    private static int colorToRGB(int red, int green, int blue, int alpha) {
//        int nPixel = 0;
//        nPixel += alpha << 8; 
//        nPixel = nPixel;
//        nPixel += red; 
//        nPixel = nPixel << 8;
//        nPixel += green; 
//        nPixel = nPixel << 8;
//        nPixel += blue;
//        return nPixel;
// 
//    }
    
    /**
     * Przypisanie nowych watości pixeli
     * 
     * @param pictureInput obraz wejściowy
     * @return newHistogram nowy histogram obrazu
     */
    public BufferedImage histogramChange(BufferedImage iInput){
        int red;
        int green;
        int blue;
        int alpha;
        int newPixel=0;
        
        ArrayList<int[]> hisLUT = histogramLut(iInput);
        BufferedImage newHistogram = new BufferedImage(iInput.getWidth(),iInput.getHeight(),iInput.getType());
        
        for (int i = 0; i<iInput.getWidth();i++){
            for(int j = 0; j<iInput.getHeight();j++){
                
                //pobranie wszystkich wartości RGB każdego z pixeli
                alpha = new Color(iInput.getRGB(i, j)).getAlpha();
                red = new Color(iInput.getRGB(i, j)).getRed();
                green = new Color(iInput.getRGB(i, j)).getGreen();
                blue = new Color(iInput.getRGB(i,j)).getBlue();
                
                //przypisanie nowych wartości na podstawie kolekcji histogramLut
                red = hisLUT.get(0)[red];
                green = hisLUT.get(1)[green];
                blue = hisLUT.get(2)[blue];
                
                //tworzenie nowego pixela
//                newPixel = colorToRGB(alpha, red, green, blue);
                
                //wpisanie nowo utworzonego pixela w obraz
//                newHistogram.setRGB(i, j, newPixel);
                 //wpisanie nowo utworzonego pixela w obraz
                newHistogram.setRGB(i, j,new Color(red,green,blue).getRGB());
            }
        }
        return newHistogram;
    }
}
