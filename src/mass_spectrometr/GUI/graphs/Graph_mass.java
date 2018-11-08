package mass_spectrometr.GUI.graphs;

import java.awt.BasicStroke;
import java.awt.Color;
import java.util.ArrayList;

import mass_spectrometr.Chart_analyser;
import mass_spectrometr.Run;

public class Graph_mass extends Graph_canvas{

	public Graph_mass(String x_measure, String y_measure, Chart_analyser analyser) {
		super(x_measure, y_measure, analyser);
	}
	
	@Override 
	void draw_data() {
		// Current mass and intensity rendering
        
        double current_mass = Run.prog.calc_mass(Run.prog.current_B);
        
        int current_intensity = Run.prog.current_intensity;
        paint_current_data(current_mass, current_intensity);
        
// Data rendering
      
        
        g.setColor(Color.RED);
        g2.setStroke(new BasicStroke(1));
        
    	if (current_intensity > max_y) max_y = current_intensity;
    	if (current_mass > max_x) max_x = current_mass;

    	for(int mass = 0; mass < Run.prog.fixed_data_mass_intensity.length - 1; mass++) {
    		
    		double intensity_1 = Run.prog.fixed_data_mass_intensity[mass];
    		double intensity_2 = Run.prog.fixed_data_mass_intensity[mass+1];
        	double mass_1 = Run.prog.calc_mass(mass);
        	double mass_2 = Run.prog.calc_mass(mass+1); 
        	
        	double x1 = mass_1*X_factor + x0;
        	double y1 = H - (intensity_1*Y_factor);
        	double x2 = mass_2*X_factor + x0;
        	double y2 = H - (intensity_2*Y_factor);
        	draw_line((int)Math.round(x1), (int)Math.round(y1), (int)Math.round(x2), (int)Math.round(y2), g2);
    	}
    	
        g.setColor(Color.BLUE);
        /*
        if(x_data.size()>2 && Run.prog.draw_graph_mass == false) {
        	analyser = new Chart_analyser(x_data, y_data);
        	paint_peak_labels(g2);
        }
        */
        
	}
	
	
}
