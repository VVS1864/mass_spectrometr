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
	void draw_current() {
// Current mass and intensity rendering
        
        double current_mass = Run.prog.calc_mass(Run.prog.current_B);  
        int current_intensity = Run.prog.current_intensity;
        paint_current_data(current_mass, current_intensity);
        
        if (current_intensity > max_y) max_y = current_intensity;
    	if (current_mass > max_x) max_x = current_mass;
	}
	
	
	@Override 
	void draw_data() {
// Data rendering
        g.setColor(Color.RED);
        g2.setStroke(new BasicStroke(1));

        for(int mass_first = 0; mass_first < Run.prog.fixed_data_mass_intensity.length; mass_first++) {
    		boolean make_line = false;
    		double mass_1 = 0;
    		double mass_2 = 0;
    		double intensity_1 = 0;
    		double intensity_2 = 0;
    		
    		intensity_1 = Run.prog.fixed_data_mass_intensity[mass_first];
    		if(intensity_1 == 0) continue;

    		mass_1 = Run.prog.calc_mass(mass_first);
    		for(int mass_second = mass_first+1; mass_second < Run.prog.fixed_data_mass_intensity.length; mass_second++) {
    			intensity_2 = Run.prog.fixed_data_mass_intensity[mass_second];
    			if(intensity_2 == 0) continue;
    			mass_2 = Run.prog.calc_mass(mass_second);
    			make_line = true;
    			break;
    		}
    		if (make_line) {
    			
    			make_line = false;
    			double x1 = mass_1*X_factor + x0;
            	double y1 = H - (intensity_1*Y_factor);
            	double x2 = mass_2*X_factor + x0;
            	double y2 = H - (intensity_2*Y_factor);
            	draw_line((int)Math.round(x1), (int)Math.round(y1), (int)Math.round(x2), (int)Math.round(y2), g2);
            	//draw_point((int)Math.round(x1), (int)Math.round(y1), g2);
    		}
        	
        	
        	
        	
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
