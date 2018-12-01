package mass_spectrometr.GUI.graphs;

import java.awt.BasicStroke;
import java.awt.Color;
import java.util.ArrayList;

import mass_spectrometr.Chart_analyser;
import mass_spectrometr.Run;

public class Graph_energy extends Graph_canvas {

	public Graph_energy(String x_measure, String y_measure,
			Chart_analyser analyser, int unit_0_x) {
		super(x_measure, y_measure, analyser);
		manual_X_factor = 35.0;
		this.unit_0_X = unit_0_x;
		x0 = 120;
	}

	@Override
	void draw_current() {
		
		// Current energy and intensity rendering
		double current_ = Run.prog.current_en_el_float;
		int current_intensity = Run.prog.current_intensity;
	
		paint_current_data(current_, current_intensity);
		if (current_intensity > max_y)
			max_y = current_intensity;
		if (current_ > max_x)
			max_x = current_;
		
	}
	
	@Override
	void draw_data() {

		g.setColor(Color.RED);
		g2.setStroke(new BasicStroke(1));
		
		// Data rendering 
		for(int energy = 0; energy < Run.prog.fixed_data_en_el_intensity.length - 1; energy++) {
			double intensity_1 = Run.prog.fixed_data_en_el_intensity[energy][0];
			double intensity_2 = Run.prog.fixed_data_en_el_intensity[energy+1][0];
			
			
			if(Run.prog.fixed_data_en_el_intensity[energy][1] != 1 ) {
				intensity_1 = Run.prog.fixed_data_en_el_intensity[energy][0]/(Run.prog.fixed_data_en_el_intensity[energy][1]-1);
			}
			if(Run.prog.fixed_data_en_el_intensity[energy+1][1] != 1) {
				intensity_2 = Run.prog.fixed_data_en_el_intensity[energy+1][0]/(Run.prog.fixed_data_en_el_intensity[energy+1][1]-1);
			}
			
			double en_el_1_float = Run.prog.calc_float_en_el(energy);
			double en_el_2_float = Run.prog.calc_float_en_el(energy+1);
			
			double x1 = (en_el_1_float - unit_0_X) * X_factor + x0;
			double y1 = H - (intensity_1 * Y_factor);
			double x2 = (en_el_2_float - unit_0_X) * X_factor + x0;
			double y2 = H - (intensity_2 * Y_factor);
			draw_line((int)Math.round(x1), (int)Math.round(y1), (int)Math.round(x2), (int)Math.round(y2), g2);
			
		}
	}
	
	void draw_data_xz() {
		g.setColor(Color.BLUE);
		/*
		 * if(x_data.size()>2 && Run.prog.draw_graph_en_el == false) { analyser = new
		 * Chart_analyser(x_data, y_data); paint_peak_labels(g2); }
		 */
	}

	

}
