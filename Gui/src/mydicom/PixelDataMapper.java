/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mydicom;

import java.awt.image.BufferedImage;

/**
 * 
 * @author jstar
 */
public interface PixelDataMapper {
    public BufferedImage map( int rows, int cols, short [] hu, int windowCenter, int windowWidth);
}
