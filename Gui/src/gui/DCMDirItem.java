/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

/**
 * Item (dicom file) 
 * @author jstar
 */
public class DCMDirItem {
    private final Object item;
    private final String name;
    
    public DCMDirItem( Object item, String name ) {
        this.item = item;
        this.name = name;
    }

    /**
     * @return the item
     */
    public Object getItem() {
        return item;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }
}
