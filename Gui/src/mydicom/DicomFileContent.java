/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mydicom;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.Serializable;
import java.util.AbstractMap;
import java.util.Objects;

/**
 * Important content of a Dicom image file
 *
 * @author jstar
 */
public class DicomFileContent implements Comparable<DicomFileContent>, Serializable {
    
    private static final long serialVersionUID = 1L;

    private BufferedImage image;
    final private String name;
    final private String fileData;
    final private double sliceLocation;
    private short[] hu;
    private short hmin = 5000, hmax= -5000;

    public DicomFileContent(File file, double location, short[] hu, BufferedImage img)
            throws Exception {
        this.name = file.getName();
        this.sliceLocation = location;
        this.image = img;
        fileData = DicomTools.dataInf(file);
        this.hu = hu;
        for (short l : hu) {
            if (l < hmin) {
                hmin = l;
            }
            if (l > hmax) {
                hmax = l;
            }
        }
    }
    
    public void updateImage(HUMapper mapper) {
        image = mapper.map(image.getWidth(), image.getHeight(), hu);
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
