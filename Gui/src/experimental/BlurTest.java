/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package experimental;

/**
 *
 * @author jstar
 */
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class BlurTest extends JPanel {

  public void paint(Graphics g) {
    BufferedImage img = createImage();
    
    float ninth = 1.0f / 9.0f;

    //float[] blurKernel = { ninth, ninth, ninth, ninth, ninth, ninth, ninth, ninth, ninth };
    float[] blurKernel = { 0, 0, 0, 0, 1, 0, 0, 0, 0 };

    BufferedImageOp blurOp = new ConvolveOp(new Kernel(3, 3, blurKernel));

    BufferedImage clone = blurOp.filter(img, null);
    
    g.drawImage(clone, 20,20,this);
  }
  private BufferedImage createImage(){
    BufferedImage bufferedImage = new BufferedImage(200,200,BufferedImage.TYPE_INT_ARGB);
    Graphics g = bufferedImage.getGraphics();
    g.drawString("www.java2s.com", 20,20);
    
    return bufferedImage;
  }

  public static void main(String[] args) {
    JFrame frame = new JFrame();
    frame.getContentPane().add(new BlurTest());

    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setSize(200, 200);
    frame.setVisible(true);
  }
  
  
}
