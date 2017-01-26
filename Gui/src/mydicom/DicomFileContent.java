/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mydicom;

import java.awt.image.BufferedImage;
import java.util.Objects;

/**
 * Important content of a Dicom image file
 *
 * @author jstar
 */
public class DicomFileContent implements Comparable<DicomFileContent> {

    final private BufferedImage image;
    final private String name;
    final private String fileData;
    final private double sliceLocation;

    public DicomFileContent(String name, double location, BufferedImage img)
            throws Exception {
        this.name = name;
        this.sliceLocation = location;
        this.image = img;
        fileData = DicomTools.dataInf(name);
    }

    /**
     * @return the item
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
    
    @Override
    public boolean equals( Object o ) {
        return o instanceof DicomFileContent && ((DicomFileContent)o).name.equals(name);
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
    public int compareTo(DicomFileContent o) {
        if (o.sliceLocation > sliceLocation) {
            return 1;
        } else if (o.sliceLocation < sliceLocation) {
            return -1;
        } else {
            return 0;
        }
    }
}
