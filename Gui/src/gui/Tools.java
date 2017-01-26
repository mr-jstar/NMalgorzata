/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import org.dicom4j.data.elements.support.DataElement;
import org.dicom4j.io.media.DicomFile;
import java.util.logging.Logger;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;

/**
 *
 * @author Małgorzata
 */
public class Tools {

    public static BufferedImage openDicomFile(File fname) throws Exception {
        BufferedImage bImg = null;
        int rows = 0;//tworzenie smiennej wiersz
        int cols = 0;
        int slope = 0;
        int intercept = 0;

        //W celu zapewnienia ze plik otwierany to plik Dicom!!!
        String fa = fname.getName();
        String end = ".dcm";
        if (fa.endsWith(end)) {
            //musze pominiac otwieranie innych formatów niż dicom
            DicomFile ldcm = new DicomFile(fname);// tworzenie obiektu do pliku dicom
            ldcm.open();// otwieramy pliku dicom
            ldcm.getDataset();// pobieranie danych zawarte w pliku Dicom
            Iterator iterator = ldcm.getDataset().getIterator();//obiekt typu iteracja

            while (iterator.hasNext()) {//dopuki iteracja zachodzi to...
                // we get the current Element (DataElement is the base class for all elements)
                DataElement lElement = (DataElement) iterator.next();//tworzony jest
                //obiekt typu dane elementu "=" do którego przypisane sa kolejne wartości iterowane które sa typu "dnych tego elementu"

                //te dwie wartości sa potrzebne do poprawnego odczytu obrazu(transformacja danych)
                //wykorzystywane sa w tym wzorze: hu = pixel_value * slope + intercept
                if (lElement.getTag().getName().equals("Rescale Slope")) {
                    String sl = lElement.getSingleStringValue();
                    slope = Integer.valueOf(sl);
                }
                if (lElement.getTag().getName().equals("Rescale Intercept")) {
                    String in = lElement.getSingleStringValue();
                    intercept = Integer.valueOf(in);
                }

                if (lElement.getTag().getName().equals("Rows")) {//jezeli obiekt lElement
                    //znajdzie (Tag) cel którym jest nazwa równa nazwie "Rows")to:
                    rows = lElement.getSingleIntegerValue();//do rows przypisane zostanie 
                    //wartość która znajdowała sie przy "Rows" typu Iniger
                }
                if (lElement.getTag().getName().equals("Columns")) {//jezeli obiekt lElement
                    //znajdzie (Tag) cel którym jest nazwa równa nazwie "Colums")to:
                    cols = lElement.getSingleIntegerValue();//do cols przypisane zostanie 
                    //wartość która znajdowała sie przy "Columns" typu Iniger
                }
                if (lElement.getTag().getName().equals("Pixel Data")) {//jezeli obiekt lElement
                    //znajdzie (Tag) cel którym jest nazwa równa nazwie "Pixel Data")to:...
                    short[] ldata = lElement.getShortValues();//tworzona jest tablica 
                    //o liczbie elementów podbranych z wartości Pixel Data
                    if (rows * cols != ldata.length) {//jesli liczba kolumn pomnożona 
                        //prze liczbe wierszy bedzie różna od długości obiektu ldata to:
//                    System.err.println("Zły obrazek ");//wyrzucony zostanie błąd
                        break; // zakonczenie tej instrukcji if i przejscie dalej
                    }
                    bImg = new BufferedImage(cols, rows, BufferedImage.TYPE_USHORT_GRAY);//-odcienie szarości 
                    //sa od 0 do jakiejs wartości "0"-czarny dalej robi sie jaśniej
                    //tworzony jest obiekt typu bufferImage którego parametry to kolumny wiersze oraz 
                    //typ tworzonego obrazu(typ uShort czyli short tylko ze znakami dodatnimi)
                    Graphics2D cg = bImg.createGraphics();// obiekt typu grafika 2D "=" prypisany 
                    //do obiektu bubfforowanie obrazu i tworzenie go

                    for (int i = 0; i < ldata.length; i++) {//az do długości zapisanej w Pixel Data
                        int x = i % cols;//tworznie wierszy
                        int y = i / cols;//tworznie kolumn
                        short hounsfield = ldata[i];//przypisanie wartości pojedynczych pixeli

                        //sadze ze t jest potrzebne tylko trzeba pobrac wartości 
                        hounsfield = (short) (hounsfield * slope + intercept);// hu = pixel_value * slope + intercept

                        // o liczbie elementów takich jak w Pixel Data
                        double hd = (hounsfield + 1000) / 4000.;//+1000 bo skala hounsfielda zaczyna sie od -1000 do 4000                   
                        bImg.setRGB(x, y, (short) (2 * Short.MAX_VALUE * hd));//"(short) (2 * Short.MAX_VALUE * hd)"- zastąpiono
                        //tylko jednym odcienie zamiast 3 kolorów 
                    }
//                ImageIO.write(bImg, "png", new File(fname.replace("dcm", "png")));// to do zapisywania pliku
                }
            }
        }
        return bImg;
    }

    // Wyświetla strukturę Dicoma do standardowego wyjścia
    public static String reportDCM(String fname) throws Exception {
        DicomFile ldcm = new DicomFile(fname);// we create the	File
        ldcm.open(); // we open it (read the data)

        Iterator iterator = ldcm.getDataset().getIterator();
        while (iterator.hasNext()) {
            // we get the current Element (DataElement is the base class for all elements)
            DataElement lElement = (DataElement) iterator.next();
            System.out.print(lElement.getTag().getName() + "(" + lElement.getClass().getSimpleName() + ") -> ");
            if (lElement.isAvailableAsString()) {//sprawdza czy wartość elementu dostepna jest jako string
                System.out.print(" { ");
                String[] ldata = lElement.getStringValues();
                for (int j = 0; j < ldata.length; j++) {
                    System.out.print(" [" + j + "]=" + ldata[j]);
                }
                System.out.println(" }");
            } else {
                try {
                    short[] ldata = lElement.getShortValues();
                    System.out.println(ldata.length + " ints");
                } catch (Exception e) {
                    System.out.println("not as short nor as String");
                }
            }
        }
        return fname;
    }

//ładuje obrazy do DefaultListModel model a ponadto zawiera kolecjie łaczaca 
//odpowiedni element z listy z nazwa pliku z którego pochodzi
//potrzebne to było w celu wyświetlenia inf o miniaturach
    public static ArrayList<DCMDirItem> readDicomDir(File directory, DefaultListModel model) throws IOException {

        File[] imageFiles = directory.listFiles(new FilenameFilter() {//obiekt typu fileNameFilter zawiera metode
            @Override
            public boolean accept(File file, String name) {//test określajacy jakie powininna zawierać lista pliki(powinien to byc plik o jakies nazwie)
                //file- określa folder w którym znajduje sie wybrany plik
                //określa gdzie dany plik sie znajduje (ścierzke dostepu)
                //name- określa dokładną naze wybranego pliku
                return true;//jesli beda te argumenty to wyrzuci prawde 
            }
        });
       
        model.removeAllElements();
        for (int i = 0; i < imageFiles.length; i++) {

            BufferedImage im;
            try {
                im = openDicomFile(imageFiles[i]);
                model.addElement(im);//dodanie do bierzacych odczytywanych obrazów               
            } catch (Exception ex) {
                Logger.getLogger(Tools.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        ArrayList<DCMDirItem> list = new ArrayList<>();

        for (int i = 0; i < imageFiles.length; i++) {
            list.add( new DCMDirItem(model.elementAt(i), imageFiles[i].getName()));
       }

        return list;
    }
}


