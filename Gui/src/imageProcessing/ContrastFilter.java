/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imageProcessing;

/**
 *
 * @author jstar
 */
public class ContrastFilter extends TransferFilter {

    private float brightness = 1.0f;
    private float contrast = 1.0f;
    
    public ContrastFilter( float brightness, float contrast ) {
        this.brightness = brightness;
        this.contrast = contrast;
        initialized = false;
        //System.out.println( "b=" + brightness + " c=" + contrast + "  tf in <" + transferFunction(0f) + " - " + transferFunction(1f)+ ">");
    }

    @Override
    protected float transferFunction(float f) {
        f = (f * brightness - 0.5f) * contrast + 0.5f;
        //f = brightness * f + contrast;
        return f;
    }

    /**
     * Set the filter brightness.
     *
     * @param brightness the brightness in the range 0 to 1
     * @min-value 0
     * @max-value 1
     * @see #getBrightness
     */
    public void setBrightness(float brightness) {
        this.brightness = brightness;
        initialized = false;
    }

    /**
     * Get the filter brightness.
     *
     * @return the brightness in the range 0 to 1
     * @see #setBrightness
     */
    public float getBrightness() {
        return brightness;
    }

    /**
     * Set the filter contrast.
     *
     * @param contrast the contrast in the range 0 to 1
     * @min-value 0
     * @max-value 1
     * @see #getContrast
     */
    public void setContrast(float contrast) {
        this.contrast = contrast;
        initialized = false;
    }

    /**
     * Get the filter contrast.
     *
     * @return the contrast in the range 0 to 1
     * @see #setContrast
     */
    public float getContrast() {
        return contrast;
    }

    @Override
    public String toString() {
        return "ContrastFilter";
    }

}
