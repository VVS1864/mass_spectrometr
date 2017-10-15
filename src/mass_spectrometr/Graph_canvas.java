package mass_spectrometr;


import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;

import javax.swing.JPanel;

public class Graph_canvas extends JPanel {
	
	private final int h_axis = 51;
	private final int w_axis = 51;
	private final int arrow_w = 10;
	private double max_intensity = 100;
	private double max_mass = 0;
	private double X_factor;
	private double Y_factor;
	private int x0;
	public int  H; 
	public int  W;
	//public Graphics2D g2;
	public Graph_canvas () {
		setBackground (Color.lightGray);
	}
	
	protected void paintComponent(Graphics g) {
        super.paintComponent(g);      
        Graphics2D g2 = (Graphics2D) g;
        H = this.getHeight()-h_axis;
        W = this.getWidth()-w_axis;
        x0 = Run.x0;
        ArrayList<Double> x_data = Run.mass_data;
        ArrayList<Integer> y_data = Run.intensity_data;
//factors
        if(Run.autoscaleY) {
        	Y_factor = Run.manual_Y_factor = (H-h_axis)/max_intensity;
        }
        else {
        	Y_factor = Run.manual_Y_factor;
        }
        X_factor = Run.manual_X_factor;

        g.setFont(new Font(g.getFont().getFontName(), Font.PLAIN, 20)); 
// Axis Y
        g.setColor(Color.BLACK);
        g2.setStroke(new BasicStroke(3));
        g2.drawLine(w_axis, 10, w_axis, H);
        //Arrow
        g2.drawLine(w_axis-arrow_w/2, 10+arrow_w, w_axis, 10);
        g2.drawLine(w_axis+arrow_w/2, 10+arrow_w, w_axis, 10);
        g2.drawString("int", w_axis - 40, 10+arrow_w);
        
// Axis X
        g.setColor(Color.BLACK);
        g2.setStroke(new BasicStroke(3));
        g2.drawLine(w_axis, H, W, H);
        g2.drawLine(W-arrow_w, H-arrow_w/2, W, H);
        g2.drawLine(W-arrow_w, H+arrow_w/2, W, H);
        g2.drawString("M", W-15, H+30);
        
        g.setFont(new Font(g.getFont().getFontName(), Font.PLAIN, 12));
        
// Units 
        BasicStroke unit_stroke; 
        int w_units = 5;
        int units_rate;
// Units Y
        int current_h = H;
        if(Y_factor > 18) units_rate = 1;
        else if(Y_factor > 4.5) units_rate = 5;
        else if(Y_factor > 3.5) units_rate = 10;
        else if(Y_factor > 2.2) units_rate = 20;
        else if(Y_factor > 1.2) units_rate = 50;
        else units_rate = 100;
       
        int current_unit = 0;
        int unit = 0;
        
        while(current_h > h_axis+units_rate*Y_factor) {
        	
        	current_unit += units_rate;
        	current_h -= units_rate*Y_factor;
        	unit += units_rate;
        	
        	int w;
        	if(current_unit == 10 || current_unit == 100) {
        		unit_stroke = new BasicStroke(3);
        		w = w_units * 2; 
        		current_unit = 0;
        	}
        	else {
        		unit_stroke = new BasicStroke(1);
        		w = w_units;
        		
        	}
        	g2.setStroke(new BasicStroke(1));
        	g.setColor(Color.GRAY);
        	g2.drawLine(w_axis, current_h, W, current_h);
        	
        	g2.setStroke(unit_stroke);
        	g.setColor(Color.BLACK);
        	g2.drawString(Integer.toString(unit), w_axis - w - 30, current_h);
        	g2.drawLine(w_axis - w, current_h, w_axis + w, current_h);
        	
        	
        }

// Units X
        int current_w = x0;
        units_rate = 20;
        current_unit = 0;
        unit = 0;
       
        if(X_factor > 30) units_rate = 1;
        else if(X_factor > 6.5) units_rate = 5;
        else if(X_factor > 3.5) units_rate = 10;
        else if(X_factor > 2) units_rate = 20;
        else if(X_factor > 1.2) units_rate = 50;
        else units_rate = 100;
        
        while(current_w < W-units_rate*X_factor) {
        	
        	
        	int w;
        	if(current_unit == 10 || current_unit == 100) {
        		unit_stroke = new BasicStroke(3);
        		w = w_units * 2; 
        		current_unit = 0;
        	}
        	else {
        		unit_stroke = new BasicStroke(1);
        		w = w_units;
        	}
        	g2.setStroke(new BasicStroke(1));
        	g.setColor(Color.GRAY);
        	g2.drawLine(current_w, H, current_w, h_axis);
        	
        	g2.setStroke(unit_stroke);
        	g.setColor(Color.BLACK);
        	g2.drawString(Integer.toString(unit), current_w-8, H + w + 30);
        	g2.drawLine(current_w, H-w, current_w, H+w);
        	
        	current_unit += units_rate;
        	current_w += units_rate*X_factor;
        	unit += units_rate;
        }
// Data rendering
        
        if(x_data.size() <= 2) return;
        
        g.setColor(Color.RED);
        g2.setStroke(new BasicStroke(1));
        double current_mass = x_data.get(x_data.size()-1);
        int current_intensity = y_data.get(y_data.size()-1);
    	if (current_intensity > max_intensity) max_intensity = current_intensity;
    	if (current_mass > max_mass) max_mass = current_mass;
        for(int i = 1; i<x_data.size()-1; i++) {        	
        	double x1 = x_data.get(i-1)*X_factor + x0;
        	double y1 = H - (y_data.get(i-1)*Y_factor);
        	double x2 = x_data.get(i)*X_factor + x0;
        	double y2 = H - (y_data.get(i)*Y_factor);
        	g2.drawLine((int)x1, (int)y1, (int)x2, (int)y2);
        	
        }
       
        paint_current_mass(current_mass, g2);
        if(!Run.transferring_data && Run.analyser!=null) {
        paint_peak_labels(g2);
        }
	}
	private void paint_peak_labels(Graphics2D g2) {
		boolean first = true;
		double prev_label_x = 0;// = Run.analyser.peaks.get(0).x*X_factor + label_w;
		Font f = g2.getFont();
		FontMetrics m = g2.getFontMetrics(f);
		for(Peak p: Run.analyser.peaks) {
			String str = Double.toString(p.x);
			double x = p.x*X_factor + x0;
			double y = H - p.y*Y_factor;
			int label_w = m.stringWidth(str)/2;
			
			double label_x = x - label_w;
			
			if (first) {
				prev_label_x = x - label_w;
				first = false;
			}
			if (label_x >= prev_label_x) {
				double label_y = y;
				g2.drawString(str, (int)label_x, (int)label_y);
				prev_label_x = x + label_w;
				g2.drawLine((int)x, (int)y+2, (int)x, (int)y+10);
			}
		}
	}
	
	private void paint_current_mass(double mass, Graphics2D g2) {
		g2.setColor(Color.BLUE);
		g2.setStroke(new BasicStroke(3));
		mass = mass*X_factor + x0;
		g2.drawLine((int)mass, H, (int)mass, 0);
	}
}