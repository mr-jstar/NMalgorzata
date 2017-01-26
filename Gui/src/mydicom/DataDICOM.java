/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mydicom;

import java.util.Iterator;
import org.dicom4j.data.elements.support.DataElement;
import org.dicom4j.io.media.DicomFile;

/**
 *
 * @author Małgorzata
 */
public class DataDICOM {
    
        String creationData;
        String datee;
        String modality;
        String institutionName;
        String studyDescription;
        String seriesDescription;
        String physicianOfRecord;
        String patientName;
        String bodyPartExamined;
        String sumInf;
    
    public String dataInf(String fname)throws Exception{
        DicomFile ldcm = new DicomFile(fname);// we create the	File
        ldcm.open(); // we open it (read the data)
        ldcm.getDataset();
        Iterator iterator = ldcm.getDataset().getIterator();
        while (iterator.hasNext()) {
            //System.out.println("czy otwarty fname "+fname);
            DataElement lElement = (DataElement) iterator.next();
            if(lElement.getTag().getName().equals("Instance Creation Date")){
              creationData=lElement.getSingleStringValue("empty");
              if (lElement.getValueLength()==8){
                  StringBuffer date=new StringBuffer(creationData);
                  date.insert(4, '-');
                  date.insert(7,'-');
                  date.insert(0,"Date: ");
                  datee=date.toString();         
              }
            }
            
            
//            if  (lElement.getTag().getName().equals("(0028")){
//              modality=lElement.getSingleStringValue("empty");
//              modality="Modality: "+modality;
//            }
//            
            
            if (lElement.getTag().getName().equals("Modality")){
              modality=lElement.getSingleStringValue("empty");
              modality="Modality: "+modality;
            }
            
            if (lElement.getTag().getName().equals("Institution Name")){
              institutionName=lElement.getSingleStringValue("empty");
              institutionName="Institution Name: "+institutionName;
            }
            
            if (lElement.getTag().getName().equals("Study Description")){
              studyDescription=lElement.getSingleStringValue("empty");
              studyDescription="Study Description: "+studyDescription;
            }
            
            if (lElement.getTag().getName().equals("Series Description")){
              seriesDescription=lElement.getSingleStringValue("empty");
              seriesDescription="Series Description: "+seriesDescription;
            }
            
             if (lElement.getTag().getName().equals("Physician(s) of Record")){
              physicianOfRecord=lElement.getSingleStringValue("empty");
              physicianOfRecord="Physician(s) of Record: "+physicianOfRecord;
            } 
             
             if (lElement.getTag().getName().equals("Patient's Name")){
              patientName=lElement.getSingleStringValue("empty");
              patientName="Patient's Name: "+patientName;
            }
             
             if (lElement.getTag().getName().equals("Body Part Examined")){
              bodyPartExamined=lElement.getSingleStringValue("empty");
              bodyPartExamined="Body Part Examined: "+bodyPartExamined;
            }
        }         
        String sumInf="<html>"+datee+"<br>"
                   +modality+"<br>"
                   +institutionName+"<br>"
                   +studyDescription+"<br>"
                   +seriesDescription+"<br>"
                   +physicianOfRecord+"<br>"
                   +patientName+"<br>"
                   +bodyPartExamined+"</html";        
    return sumInf;
    }    
}
