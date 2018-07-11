package mass_spectrometr.GUI;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class Volt_engine extends JPanel{
	private JTextField num_field;
	private JSlider slider;
	static final int MIN = -4;
	static final int MAX = 20;
	static final int INIT = 0;
	
	public Volt_engine() {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		JLabel label = new JLabel("Electron energy: ");
		slider = new JSlider(JSlider.HORIZONTAL,
                MIN, MAX, INIT);
		
		slider.setPaintTicks(true);
		slider.setPaintLabels(true);
		slider.setMajorTickSpacing(4);
		slider.setMinorTickSpacing(1);
		
		JPanel p = new JPanel();
		p.setLayout(new BoxLayout(p, BoxLayout.X_AXIS));
		num_field = new JTextField();
		
		slider.setMaximumSize(new Dimension(300, 100));
		num_field.setMaximumSize(new Dimension(50, 100));
		p.add(label);
		p.add(num_field);
		add(p);
		add(slider);
		
		ChangeListener listener = new ChangeListener(){
			public void stateChanged(ChangeEvent event){
		
				JSlider slider = (JSlider)event.getSource();
				int value = slider.getValue();
				num_field.setText(Integer.toString(value));
			}
		};
		
		slider.addChangeListener(listener);
	}
	
	public void set_value(int value) {
		if(value <= MAX && value >= MIN) {
			num_field.setText(Integer.toString(value));
			slider.setValue(value);
		}
	}
	
	public int get_value() {
		return slider.getValue();
	}
}
