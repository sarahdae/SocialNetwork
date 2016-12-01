
import java.awt.Component;

import javax.swing.JApplet;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSlider;

public class NewJApplet extends JApplet {

    private JSlider sb = new JSlider(JSlider.HORIZONTAL, 0, 30, 10);

    public void init() {
        JLabel sliderLabel = new JLabel("Chapter", JLabel.CENTER);
        sliderLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(sliderLabel);
        sb.setValue(0);
        sb.setPaintTicks(true);
        sb.setMajorTickSpacing(5);
        sb.setMinorTickSpacing(1);
        sb.setSnapToTicks(true);
        sb.setPaintTrack(false);
        sb.setPaintLabels(true);

        add(sb);
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
