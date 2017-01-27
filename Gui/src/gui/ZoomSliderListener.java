/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 *
 * @author jstar
 */
public class ZoomSliderListener implements ChangeListener {

    private final ImageManager manager;
    private double scale;

    public ZoomSliderListener(ImageManager manager) {
        this.manager = manager;
        this.scale = 1;
    }
    
    public double getCurrentScale() {
        return scale;
    }

    @Override
    public void stateChanged(ChangeEvent ce) {
        double value = ((JSlider) ce.getSource()).getValue();
        //System.out.println("value "+value);
        scale = value / 100;
        manager.repaint(scale);
    }
}
