package mass_spectrometr.GUI.graphs;

import java.awt.BasicStroke;
import java.awt.Color;
import java.util.ArrayList;

import mass_spectrometr.Chart_analyser;
import mass_spectrometr.Run;

public class Graph_energy extends Graph_canvas {

	public Graph_energy(String x_measure, String y_measure, Chart_analyser analyser, int unit_0_x) {
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
		for (int energy_first = 0; energy_first < Run.prog.fixed_data_en_el_intensity.length - 1; energy_first++) {
			boolean make_line = false;
			int energy_1_int = 0;
			int energy_2_int = 0;
			double energy_1_double = 0;
			double energy_2_double = 0;
			double intensity_1 = 0;
			double intensity_2 = 0;

			intensity_1 = Run.prog.fixed_data_en_el_intensity[energy_first][0];
			if (intensity_1 == 0)
				continue;
			
			energy_1_int = energy_first;
			energy_1_double = Run.prog.calc_float_en_el(energy_first);

			for (int energy_second = energy_first
					+ 1; energy_second < Run.prog.fixed_data_en_el_intensity.length; energy_second++) {

				intensity_2 = Run.prog.fixed_data_en_el_intensity[energy_second][0];
				if (intensity_2 == 0)
					continue;

				energy_2_int = energy_second;
				energy_2_double = Run.prog.calc_float_en_el(energy_second);
				make_line = true;
				break;
			}
			if (make_line) {
				make_line = false;

				if (Run.prog.fixed_data_en_el_intensity[energy_1_int][1] != 1) {
					intensity_1 = Run.prog.fixed_data_en_el_intensity[energy_1_int][0]
							/ (Run.prog.fixed_data_en_el_intensity[energy_1_int][1] - 1);
				}
				if (Run.prog.fixed_data_en_el_intensity[energy_2_int][1] != 1) {
					intensity_2 = Run.prog.fixed_data_en_el_intensity[energy_2_int][0]
							/ (Run.prog.fixed_data_en_el_intensity[energy_2_int][1] - 1);
				}

				double x1 = (energy_1_double - unit_0_X) * X_factor + x0;
				double y1 = H - (intensity_1 * Y_factor);
				double x2 = (energy_2_double - unit_0_X) * X_factor + x0;
				double y2 = H - (intensity_2 * Y_factor);
				draw_line((int) Math.round(x1), (int) Math.round(y1), (int) Math.round(x2), (int) Math.round(y2), g2);
			}
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
