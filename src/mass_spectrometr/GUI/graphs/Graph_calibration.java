package mass_spectrometr.GUI.graphs;

import java.awt.BasicStroke;
import java.awt.Color;
import java.util.ArrayList;

import mass_spectrometr.Chart_analyser;
import mass_spectrometr.Run;

public class Graph_calibration extends Graph_canvas{
	public Graph_calibration(ArrayList<Double> x_data, ArrayList<Integer> y_data, String x_measure, String y_measure, Chart_analyser analyser, double X_scale, double Y_scale) {
		super(x_data, y_data, x_measure, y_measure, analyser);
		set_scales(X_scale, Y_scale);
	}
	
	@Override 
	void draw_data() {
// Data rendering
        if(x_data.size() <= 2) return;
        
        g.setColor(Color.RED);
        g2.setStroke(new BasicStroke(1));
        
    	if (x_data.size() == 0 || y_data.size() == 0) return;
        for(int i = 1; i<x_data.size()-1; i++) {    
        	double mass_1 = Run.prog.calc_mass(x_data.get(i-1));
        	double mass_2 = Run.prog.calc_mass(x_data.get(i)); 
        	double x1 = mass_1*X_factor + x0;
        	double y1 = H - (y_data.get(i-1)*Y_factor);
        	double x2 = mass_2*X_factor + x0;
        	double y2 = H - (y_data.get(i)*Y_factor);
        	
        	draw_line((int)x1, (int)y1, (int)x2, (int)y2, g2);
        	
        }
	}
	
	private void set_scales(double X, double Y){
		autoscaleY = false;
		manual_Y_factor = Y;
		manual_X_factor = X;
	}

}
