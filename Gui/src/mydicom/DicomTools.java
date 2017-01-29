/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mydicom;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import org.dicom4j.data.elements.support.DataElement;
import org.dicom4j.io.media.DicomFile;

/**
 *
 * @author Małgorzata
 */
public class DicomTools {

    public static String dataInf(File file) throws Exception {
        String fileName = "File: " + file.getName();
        String creationDate = "";
        String datee = "";
        String modality = "";
        String institutionName = "";
        String studyDescription = "";
        String seriesDescription = "";
        String physicianOfRecord = "";
        String patientName = "";
        String bodyPartExamined = "";
        String sumInf = "";
        String slicePosition = "unknown";
        DicomFile ldcm = new DicomFile(file);
        ldcm.open();
        ldcm.getDataset();
        Iterator iterator = ldcm.getDataset().getIterator();
        while (iterator.hasNext()) {
            DataElement lElement = (DataElement) iterator.next();
            if (lElement.getTag().getName().equals("Instance Creation Date")) {
                creationDate = lElement.getSingleStringValue("empty");
                if (lElement.getValueLength() == 8) {
                    StringBuilder date = new StringBuilder(creationDate);
                    date.insert(4, '-');
                    date.insert(7, '-');
                    date.insert(0, "Date: ");
                    datee = date.toString();
                }
            }

            if (lElement.getTag().getName().equals("Modality")) {
                modality = lElement.getSingleStringValue("empty");
                modality = "Modality: " + modality;
            }

            if (lElement.getTag().getName().equals("Institution Name")) {
                institutionName = lElement.getSingleStringValue("empty");
                institutionName = "Institution Name: " + institutionName;
            }

            if (lElement.getTag().getName().equals("Study Description")) {
                studyDescription = lElement.getSingleStringValue("empty");
                studyDescription = "Study Description: " + studyDescription;
            }

            if (lElement.getTag().getName().equals("Series Description")) {
                seriesDescription = lElement.getSingleStringValue("empty");
                seriesDescription = "Series Description: " + seriesDescription;
            }

            if (lElement.getTag().getName().equals("Physician(s) of Record")) {
                physicianOfRecord = lElement.getSingleStringValue("empty");
                physicianOfRecord = "Physician(s) of Record: " + physicianOfRecord;
            }

            if (lElement.getTag().getName().equals("Patient's Name")) {
                patientName = lElement.getSingleStringValue("empty");
                patientName = "Patient's Name: " + patientName;
            }

            if (lElement.getTag().getName().equals("Body Part Examined")) {
                bodyPartExamined = lElement.getSingleStringValue("empty");
                bodyPartExamined = "Body Part Examined: " + bodyPartExamined;
            }

            if (lElement.getTag().getName().equals("Slice Location")) {
                slicePosition = "Slice Location: " + lElement.getSingleStringValue("unknown");
            }
        }
        sumInf = "<html>"
                + fileName + "<br>"
                + datee + "<br>"
                + modality + "<br>"
                + institutionName + "<br>"
                + studyDescription + "<br>"
                + seriesDescription + "<br>"
                + physicianOfRecord + "<br>"
                + patientName + "<br>"
                + bodyPartExamined + "<br>"
                + slicePosition + "<br>"
                + "</html";
        return sumInf;
    }

    // Pobiera pliki z obrazami z katalogu i pakuje je do ListModel
    // oraz tworzy mapę obraz -> nazwa pliku
    public static void readDicomDir(File directory, DefaultListModel<DicomFileContent> model, HUMapper mapper) throws IOException {
        System.err.println("Opening " + directory.getAbsolutePath());
        File[] imageFiles = directory.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File file, String name) {
                return true; // można jeszcze sprawdzać, czy rozszerzenie do dcm, DCM itd, ale chyba lepiej nie
            }
        });
        System.err.println(imageFiles.length + " files found");
        processFileList(imageFiles, model, mapper);
        System.err.println(model.getSize() + " images read from " + directory);
    }

    public static void processFileList(File[] imageFiles, DefaultListModel<DicomFileContent> model, HUMapper mapper) throws IOException {
        ArrayList<DicomFileContent> list = new ArrayList<>();
        for (File file : imageFiles) {
            //System.err.print("Trying " + file.getName());
            try {
                DicomFileContent im = openDicomFile(file,mapper);
                if (im != null) {
                    list.add(im);
                    //System.err.println(" -> ok! : ");
                } else {
                    //System.err.println(" -> NO IMAGE");
                }
            } catch (Exception ex) {
                Logger.getLogger(DicomTools.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        Collections.sort(list);
        model.removeAllElements();
        for (DicomFileContent fc : list) {
            //System.err.println(fc.getName() + " at " + fc.getLocation() + " : " + fc.getWidth() + "x" + fc.getHeight() + " pixels");
            model.addElement(fc);
        }
    }

    public static DicomFileContent openDicomFile(File file, HUMapper mapper) throws Exception {
        BufferedImage bImg = null;
        int rows = 0; //tworzenie smiennej wiersz
        int cols = 0;
        float slope = 0;
        float intercept = 0;
        double location = 0;
        short hmin = 1000, hmax = -1000;
        String fname = file.getName();
        String end = ".dcm";
        short[] ldata= null;
        if (fname.endsWith(end)) {
            DicomFile ldcm = new DicomFile(file);
            ldcm.open();
            ldcm.getDataset();
            Iterator iterator = ldcm.getDataset().getIterator();
            while (iterator.hasNext()) {
                DataElement lElement = (DataElement) iterator.next();
                String tagName = lElement.getTag().getName();
                //System.err.println( tagName );
                if (tagName.equals("Rescale Slope")) {
                    String sl = lElement.getSingleStringValue();
                    slope = Float.valueOf(sl);
                }
                if (tagName.equals("Rescale Intercept")) {
                    String in = lElement.getSingleStringValue();
                    intercept = Float.valueOf(in);
                }
                if (tagName.equals("Rows")) {
                    rows = lElement.getSingleIntegerValue();
                }
                if (tagName.equals("Columns")) {
                    cols = lElement.getSingleIntegerValue();
                }
                if (tagName.equals("Slice Location")) {
                    location = Double.parseDouble(lElement.getSingleStringValue());
                }
                if (tagName.equals("Pixel Data")) {
                    ldata = lElement.getShortValues();
                    if (rows * cols != ldata.length) {
                        break;
                    }
                    for (int i = 0; i < ldata.length; i++) {
                        int x = i % cols;
                        int y = i / cols;
                        ldata[i] = (short) (ldata[i] * slope + intercept);
                    }
                    //bImg = (new HU2GrayMapper()).map(rows, cols, ldata);
                    bImg = mapper.map(rows, cols, ldata);
                }
            }
        }
        return bImg == null ? null : new DicomFileContent(file, location, ldata, bImg);
    }

    // Wyświetla strukturę Dicoma do standardowego wyjścia
    public static String reportDCM(String fname) throws Exception {
        DicomFile ldcm = new DicomFile(fname);
        ldcm.open();
        Iterator iterator = ldcm.getDataset().getIterator();
        while (iterator.hasNext()) {
            DataElement lElement = (DataElement) iterator.next();
            System.out.print(lElement.getTag().getName() + "(" + lElement.getClass().getSimpleName() + ") -> ");
            if (lElement.isAvailableAsString()) {
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
}
