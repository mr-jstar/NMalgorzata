/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imageProcessing;

import java.awt.image.BufferedImage;
import java.io.File;

/**
 *
 * @author Małgorzata
 */
public class improvQualityDicom {
    Interpolation inI= new Interpolation();
    GammaCorrection gmC = new GammaCorrection();
    MedianaFilter mF = new MedianaFilter();
    GaussianFilter gF= new GaussianFilter();
    StretchingImage sI= new StretchingImage();
    EqualizationImage eI = new EqualizationImage();
    
    public BufferedImage endImage(File fname){
//        BufferedImage eImageDicom = eI.histogramChange(sI.histogramAdd(gF.histogramChange(mF.histogramChange
//        (gmC.histogramChange(inI.interpolationDicom(fname))))));
BufferedImage eImageDicom = gF.histogramChange(mF.histogramChange
        (inI.interpolationDicom(fname)));
        return eImageDicom;
    }
}
