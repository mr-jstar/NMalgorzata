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

    private static final long serialVersionUID = 1L;

    private BufferedImage image;
    private String name;
    private String fileData;
    private double sliceLocation;
    private short[] hu;
    private short hmin = 5000, hmax = -5000;
    
    public DicomFileContent(ObjectInput in) throws IOException, ClassNotFoundException { 
        readExternal(in);
    }

    public DicomFileContent(File file, double location, short[] hu, BufferedImage img)
            throws Exception {
        this.name = file.getName();
        this.sliceLocation = location;
        this.image = img;
        fileData = DicomTools.dataInf(file);
        this.hu = hu;
        calcHUminAndmax();
    }

    private void calcHUminAndmax() {
        for (short l : hu) {
            if (l < hmin) {
                hmin = l;
            }
            if (l > hmax) {
                hmax = l;
            }
        }
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeObject(name);
        out.writeObject(fileData);
        out.writeObject(sliceLocation);
        out.writeObject(hu);
        out.writeObject(image.getWidth());
        out.writeObject(image.getHeight());
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        name = (String) in.readObject();
        fileData = (String) in.readObject();
        sliceLocation = (Double) in.readObject();
        hu = (short[]) in.readObject();
        int width = (int) in.readObject();
        int height = (int) in.readObject();
        image = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
        calcHUminAndmax();
    }

    public void updateImage(HUMapper mapper) {
        if( image != null ) {
            image = mapper.map(image.getWidth(), image.getHeight(), hu);
        } else {
            throw new NullPointerException("DicomFileContent::updateImage: image is null, but can not be!");
        }
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

    /**
     * @return the width
     */
    public double getWidth() {
        return image.getWidth();
    }

    /**
     * @return the height
     */
    public double getHeight() {
        return image.getWidth();
    }

    public AbstractMap.SimpleImmutableEntry<Short, Short> getHURange() {
        return new AbstractMap.SimpleImmutableEntry<>(hmin, hmax);
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
