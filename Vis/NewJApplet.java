
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.JApplet;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;

public class NewJApplet extends JApplet {

    private JSlider sb = new JSlider(JSlider.HORIZONTAL, 0, 30, 10);
    int chapter;
    String file1 = "C:\\Users\\Sarah Dänel\\Documents\\NetBeansProjects\\Networks2\\src\\java\\LotF_Annotated.tsv";
    String file2 = "C:\\Users\\Sarah Dänel\\Documents\\NetBeansProjects\\Networks2\\src\\java\\LotFChars.txt";
    MainClass mainClass;

    public void init() {
        JLabel sliderLabel = new JLabel("Chapter", JLabel.CENTER);
        sliderLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        sb.setValue(0);
        sb.setPaintTicks(true);
        sb.setMajorTickSpacing(5);
        sb.setMinorTickSpacing(1);
        sb.setSnapToTicks(true);
        sb.setPaintTrack(false);
        sb.setPaintLabels(true);
        Font font = new Font("Helvetica", Font.BOLD, 16);
        sb.setFont(font);
        //add(sliderLabel);
        add(sb);
    }
    
    public void stateChanged(ChangeEvent e){
    JSlider source = (JSlider)e.getSource();
    if(!source.getValueIsAdjusting()){
    chapter = (int)source.getValue();
    mainClass = new MainClass(chapter, file1, file2);
   
    } else {
    chapter = 1;
    }
    }
    
    
    public static void main(String[] args) {
        run(new NewJApplet(), 1000, 500);
    }

    public static void run(JApplet applet, int width, int height) {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(applet);
        frame.setSize(width, height);
        applet.init();
        applet.start();
        frame.setVisible(true);
    }
}
//Create the label.
/**
 * *
 * JSlider chapters = new JSlider(JSlider.HORIZONTAL, min, max);
 * chapters.setChangeListener(listener); chapters.setMajorTickSpacing(10);
 * chapters.setPaintTicks(true); add(chapters);
 */
