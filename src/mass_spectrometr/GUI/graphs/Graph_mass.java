package mass_spectrometr.GUI.graphs;

import java.awt.BasicStroke;
import java.awt.Color;
import java.util.ArrayList;

import mass_spectrometr.Chart_analyser;
import mass_spectrometr.Run;

public class Graph_mass extends Graph_canvas{

	public Graph_mass(ArrayList<Double> x_data, ArrayList<Integer> y_data, String x_measure, String y_measure, Chart_analyser analyser) {
		super(x_data, y_data, x_measure, y_measure, analyser);
	}
	
	@Override 
	void draw_data() {
		// Current mass and intensity rendering
        
        double current_mass = Run.prog.calc_mass(Run.prog.current_B);
        //System.out.println(Run.prog.current_mass + " " + current_mass);
        int current_intensity = Run.prog.current_intensity;
        paint_current_data(current_mass, current_intensity);
        
// Data rendering
        if(x_data.size() <= 2) return;
        
        g.setColor(Color.RED);
        g2.setStroke(new BasicStroke(1));
        
    	if (current_intensity > max_y) max_y = current_intensity;
    	if (current_mass > max_x) max_x = current_mass;
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

        g.setColor(Color.BLUE);
        if(x_data.size()>2 && Run.prog.draw_graph_mass == false) {
        	analyser = new Chart_analyser(x_data, y_data);
        	paint_peak_labels(g2);
        }
        
	}
}
