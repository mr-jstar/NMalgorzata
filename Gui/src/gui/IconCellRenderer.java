/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;

class IconCellRenderer extends DefaultListCellRenderer {

    // private static final long serialVersionUID = 1L;
    private int size;
    private BufferedImage icon;
    public static int defaultIconSize = 200;

    IconCellRenderer() {//określenie wielkości obrazka
        this(defaultIconSize);
    }

    IconCellRenderer(int size) {//nadanie okreslonego rozmiaru obrazkowi 
        this.size = size;
        icon = new BufferedImage(size, size, BufferedImage.TYPE_USHORT_GRAY);
    }

    //list - The JList we're painting.
    //value - The value returned by list.getModel().getElementAt(index).
    //index - The cells index.
    //isSelected - True if the specified cell was selected.
    //cellHasFocus - True if the specified cell has the focus.
    @Override
    public Component getListCellRendererComponent(
            JList list,
            Object value,
            int index,
            boolean isSelected,
            boolean cellHasFocus) {
        Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        //tworzony jest obiekt typu component który zawiera w sobie konstruktor bazowy- super
        if (c instanceof JLabel && value instanceof BufferedImage) {//jesli w obiekcie wystepuja te 3 rzeczy to:
            JLabel l = (JLabel) c;//tworzony jest nowy obiekt jLabel z komponentu c
            l.setText("");//nie wyswietla ten jLabel nic
            BufferedImage i = (BufferedImage) value;//buffforowana jest wartość value
            l.setIcon(new ImageIcon(icon));//i ustawiany jest obraz w jLabel poprzez 
            //konstruktor IconCellRenderer o ustalonym rozmiarze i typie wyświtlenia

            Graphics2D g = icon.createGraphics();
            g.setColor(new Color(0, 0, 0, 0));
            g.clearRect(0, 0, size, size);
            g.drawImage(i, 0, 0, size, size, this);

            g.dispose();
        }
        return c;
    }
}
