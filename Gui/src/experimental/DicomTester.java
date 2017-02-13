/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package experimental;

import java.io.File;
import java.util.Iterator;
import javax.swing.JFileChooser;
import org.dicom4j.data.elements.support.DataElement;
import org.dicom4j.io.media.DicomFile;

/**
 *
 * @author jstar
 */
public class DicomTester {

    public static void main(String[] args) {
        JFileChooser chooser = new JFileChooser(new File(""));

        if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            //String filePath = args.length > 0 ? args[0] : "/home/jstar/tmp/NMalgorzata/Gui/data/2a/1073550723.dcm";
            String filePath = file.getAbsolutePath();
            try {
                DicomFile ldcm = new DicomFile(filePath);
                ldcm.open();
                Iterator it = ldcm.getDataset().getIterator();
                while (it.hasNext()) {
                    DataElement lElement = (DataElement) it.next();
                    System.out.println(lElement.getTag().getName() + " = " + lElement.getSingleStringValue() );
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
