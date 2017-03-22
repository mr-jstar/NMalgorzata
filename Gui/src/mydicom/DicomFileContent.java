/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mydicom;

import java.awt.image.BufferedImage;
import java.io.Externalizable;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.AbstractMap;
import java.util.Objects;

/**
 * Important content of a Dicom image file
 *
 * @author jstar
 */
public class DicomFileContent implements Comparable<DicomFileContent>, Externalizable {

    private static final long serialVersionUID = 1L;//powiązane z serializacja
    
    public static final int UNKNOWN = 0;
    public static final int CR = 1;
    public static final int CT = 2;

    private BufferedImage image;
    private String name;
    private String fileData;
    private double sliceLocation;
    private int modality;
    private short[] pixel_data;
    private int rows, cols;
    private short data_min = Short.MAX_VALUE, data_max = Short.MIN_VALUE;
//    private float rescaleSlope;
//    private float rescaleIntercept;
    private int windowCenter;
    private int windowWidth;
    
    public DicomFileContent(ObjectInput in) throws IOException, ClassNotFoundException { 
        readExternal(in);
    }

    public DicomFileContent(File file, double location, int modality, short[] pixel_data, BufferedImage img, int windowCenter, int windowWidth)//, float slope,float intercept)
            throws Exception {
        this.name = file.getName();
        this.sliceLocation = location;
        this.modality = modality;
        this.image = img;
////        this.rescaleSlope = slope;
////        this.rescaleIntercept = intercept;
        this.cols = img.getWidth();
        this.rows = img.getHeight();
        this.windowCenter = windowCenter;
        this.windowWidth = windowWidth;
        fileData = DicomTools.dataInf(file);
        this.pixel_data = pixel_data;
        calcDataMinAndMax();
    }
    
// szukanie minimalnej i max wartosci pixeli
    private void calcDataMinAndMax() {
        for (short l : pixel_data) {
            if (l < data_min) {
                data_min = l;
            }
            if (l > data_max) {
                data_max = l;
            }
        }
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeObject(name);
        out.writeObject(fileData);
        out.writeObject(sliceLocation);
        out.writeObject(modality);
        out.writeObject(pixel_data);
//        out.writeObject(rescaleSlope);
//        out.writeObject(rescaleIntercept);
        out.writeObject(windowCenter);
        out.writeObject(windowWidth);
        out.writeObject(cols); 
        out.writeObject(rows); 
//        poprawna serializacja window
        //System.out.println( "Saved " + image.getWidth() + "x" + image.getHeight());
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        name = (String) in.readObject();
        fileData = (String) in.readObject();
        sliceLocation = (Double) in.readObject();
        modality = (Integer) in.readObject();
        pixel_data = (short[]) in.readObject();
//        rescaleSlope=(float) in.readObject();
//        rescaleIntercept = (float) in.readObject();
        windowCenter =(int) in.readObject();
        windowWidth = (int) in.readObject();
        //deserializacja poprawne wartosci window
        cols = (int) in.readObject();
        rows = (int) in.readObject();
        //System.out.println( "Read " + cols + "x" + rows);
        image = new BufferedImage(cols, rows, BufferedImage.TYPE_BYTE_GRAY);
        //System.out.println( "Created " + image.getWidth() + "x" + image.getHeight());
        calcDataMinAndMax();
    }

    public void updateImage(PixelDataMapper mapper) {
        if( image != null ) {
            image = mapper.map(rows, cols, pixel_data, windowCenter, windowWidth);//,rescaleSlope,rescaleIntercept);
            //odpowiednie windows
        } else {
            throw new NullPointerException("DicomFileContent::updateImage: image is null, but can not be!");
        }
    }

    /**
     * @return the image
     */
    public BufferedImage getImage() {
        return image;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the data
     */
    public String getData() {
        return fileData;
    }

    /**
     * @return the location
     */
    public double getLocation() {
        return sliceLocation;
    }
    
    /**
     * @return the modality
     */
    public int getModality() {
        return modality;
    }

    /**
     * @return the width
     */
    public double getWidth() {
        return cols;
    }

    /**
     * @return the height
     */
    public double getHeight() {
        return rows;
    }
    
//    /**
//     * 
//     * @return the Rescale Slope
//     */
//    public float getrescaleSlope(){
//        return rescaleSlope;
//    }
//    
//    /**
//     * 
//     * @return the Rescale Intercept
//     */
//    public float getrescaleIntercept(){
//        return rescaleIntercept;
//    }
    
    /**
     * 
     * @return the Window Center
     */
    public int getwindowCenter(){
        return windowCenter;
    }
    
    /**
     * 
     * @return the Window Width 
     */
    public int getwindowWidth(){
        return windowWidth;
    }
    
    public short getPixelData( int x, int y ) {
        if( x >= 0 && x < cols && y >= 0 && y <= rows )
            return pixel_data[ y*cols + x ];
        else
            return 0;
    }

    public AbstractMap.SimpleImmutableEntry<Short, Short> getHURange() {//<klucz,wartosc>
        return new AbstractMap.SimpleImmutableEntry<>(data_min, data_max);//Tworzy wpis reprezentującą mapowanie z określonego klucza do określonej wartości.
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof DicomFileContent && ((DicomFileContent) o).name.equals(name);
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 23 * hash + Objects.hashCode(this.name);
        return hash;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public int compareTo(DicomFileContent o) {//porównywanie obrazków na podstawie sliceLocation
        if (o.sliceLocation > sliceLocation) {
            return 1;
        } else if (o.sliceLocation < sliceLocation) {
            return -1;
        } else {
            return 0;
        }
    }

}
