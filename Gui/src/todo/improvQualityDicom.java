/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package todo;

import imageProcessing.GaussianFilter;
import imageProcessing.HistogramEqualizationFilter;
import todo.StretchingImage;
import todo.MedianaFilter;
import todo.Interpolation;
import todo.GammaCorrection;
import java.awt.image.BufferedImage;
import java.io.File;
import mydicom.DicomFileContent;
import mydicom.DicomTools;

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
    HistogramEqualizationFilter eI = new HistogramEqualizationFilter();
    
}
