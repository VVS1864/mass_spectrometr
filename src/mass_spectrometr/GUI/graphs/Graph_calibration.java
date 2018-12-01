package mass_spectrometr.GUI.graphs;

import java.awt.BasicStroke;
import java.awt.Color;
import java.util.ArrayList;

import mass_spectrometr.Chart_analyser;
import mass_spectrometr.Run;

public class Graph_calibration extends Graph_mass{
	public boolean draw_cursor = false;
	public double cursor_x;
	public double cursor_y;
	public int snap = 7;
	public Graph_calibration(String x_measure, String y_measure, Chart_analyser analyser, double X_scale, double Y_scale, int x0) {
		super(x_measure, y_measure, analyser);
		set_scales(X_scale, Y_scale, x0);
	}
	@Override 
	void draw_current() {
		if(draw_cursor) draw_X_line();
	}
	/*
	@Override 
	void draw_data() {
// Data rendering
		
		g.setColor(Color.RED);
        g2.setStroke(new BasicStroke(1));
        
    	for(int mass = 0; mass < Run.prog.fixed_data_mass_intensity.length - 1; mass++) {
    		
    		double intensity_1 = Run.prog.fixed_data_mass_intensity[mass];
    		double intensity_2 = Run.prog.fixed_data_mass_intensity[mass+1];
        	double mass_1 = Run.prog.calc_mass(mass);
        	double mass_2 = Run.prog.calc_mass(mass+1); 
        	
        	double x1 = mass_1*X_factor + x0;
        	double y1 = H - (intensity_1*Y_factor);
        	double x2 = mass_2*X_factor + x0;
        	double y2 = H - (intensity_2*Y_factor);
        	draw_line((int)x1, (int)y1, (int)x2, (int)y2, g2);
    	}
    	if(draw_cursor) draw_X_line();
	}
	*/
	private void set_scales(double X, double Y, int x0){
		autoscaleY = false;
		manual_Y_factor = Y;
		manual_X_factor = X;
		this.x0 = x0;
	}
	
	public void draw_X_line() {
		g2.setStroke(new BasicStroke(3));
		g2.setColor(Color.BLACK);
		// g2.drawLine((int)(x1 * X_factor + x0), H, (int)(x2 * X_factor + x0), 10);
		double x1 = cursor_x*X_factor + x0;
    	double y1 = H - (cursor_y*Y_factor);
    	double w = x1 + snap;
    	double h = y1 + snap;
		//g2.drawRect((int)x1, (int)y1, (int)w, (int)h);
		g2.drawLine((int)x1-2, (int)y1-2, (int)x1+2, (int)y1+2);
		g2.drawLine((int)x1+2, (int)y1-2, (int)x1-2, (int)y1+2);
		//System.out.println("X line");
	}

}
