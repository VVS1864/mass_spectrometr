package mass_spectrometr.GUI.graphs;


import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.text.DecimalFormat;
import java.util.ArrayList;

import javax.swing.JPanel;

import mass_spectrometr.Chart_analyser;
import mass_spectrometr.Peak;
import mass_spectrometr.Run;

public abstract class Graph_canvas extends JPanel {
	protected ArrayList<Double> x_data;
	protected ArrayList<Integer> y_data;
	
	private final int h_axis = 51;
	private final int w_axis = 100;
	private final int arrow_w = 10;
	protected double max_y = 100;
	protected double max_x = 0;
	
	protected double X_factor;
	protected double Y_factor;
	public  boolean autoscaleY = true;
	public  double manual_X_factor = 4.0;
	public  double manual_Y_factor = 4.0;
	public  double scale_rate = 1.3;
	
	public int x0 = 100;
	protected int  H; 
	public int  W;
	
	protected Graphics g;
	protected Graphics2D g2;
	
	protected String x_measure;
	protected String y_measure;
	
	protected Chart_analyser analyser;
	
	public Graph_canvas (ArrayList<Double> x_data, ArrayList<Integer> y_data, String x_measure, String y_measure, Chart_analyser analyser) {
		this.x_data = x_data;
		this.y_data = y_data;
		setBackground (Color.lightGray);
		this.x_measure = x_measure;
		this.y_measure = y_measure;
		this.analyser = analyser;
	}
	
	protected void paintComponent(Graphics g) {
		this.g = g;
        super.paintComponent(g);      
        this.g2 = (Graphics2D) g;
        
        draw_axis();
        
        draw_data();
	}
	
    abstract void draw_data();
	
	protected void draw_axis() {
		H = this.getHeight()-h_axis;
        W = this.getWidth()-w_axis;
        //x0 = Run.prog.x0;
        //ArrayList<Double> x_data = Run.prog.data_Bo;
        //ArrayList<Integer> y_data = Run.prog.data_intensity;
//factors
        if(autoscaleY) {
        	Y_factor = manual_Y_factor = (H-(2*h_axis))/max_y;
        }
        else {
        	Y_factor = manual_Y_factor;
        }
        X_factor = manual_X_factor;

        g.setFont(new Font(g.getFont().getFontName(), Font.PLAIN, 20)); 
       
        
// Axis Y
        g.setColor(Color.BLACK);
        g2.setStroke(new BasicStroke(3));
        g2.drawLine(w_axis, 10, w_axis, H);
        //Arrow
        g2.drawLine(w_axis-arrow_w/2, 10+arrow_w, w_axis, 10);
        g2.drawLine(w_axis+arrow_w/2, 10+arrow_w, w_axis, 10);
        g2.drawString(y_measure, w_axis - 40, 10+arrow_w);
        
// Axis X
        g.setColor(Color.BLACK);
        g2.setStroke(new BasicStroke(3));
        g2.drawLine(w_axis, H, W, H);
        g2.drawLine(W-arrow_w, H-arrow_w/2, W, H);
        g2.drawLine(W-arrow_w, H+arrow_w/2, W, H);
        g2.drawString(x_measure, W-15, H+30);
        
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
			if (current_unit == 10 || current_unit == 100) {
				unit_stroke = new BasicStroke(3);
				w = w_units * 2;
				current_unit = 0;
			} else {
				unit_stroke = new BasicStroke(1);
				w = w_units;
			}
			if (current_w > w_axis) {
				g2.setStroke(new BasicStroke(1));
				g.setColor(Color.GRAY);
				g2.drawLine(current_w, H, current_w, h_axis);

				g2.setStroke(unit_stroke);
				g.setColor(Color.BLACK);
				g2.drawString(Integer.toString(unit), current_w - 8, H + w + 30);
				g2.drawLine(current_w, H - w, current_w, H + w);
			}
			current_unit += units_rate;
			current_w += units_rate * X_factor;
			unit += units_rate;
			
        }
	}
	protected void draw_line(int x1, int y1, int x2, int y2, Graphics2D g2) {
	//	if(x1>=w_axis && x2>=w_axis && x1<=W && x2<=W && y1>=10 && y2>=10 && y1<=H && y2<=H) {
		if((x1>=w_axis && x2>=w_axis && x1<=W) && (x2<=W || y1>=10 || y2>=10 || y1<=H || y2<=H)) {
    		g2.drawLine((int)x1, (int)y1, (int)x2, (int)y2);
		}
	}
	private void draw_string(String str, int x1, int y1, Graphics2D g2) {
		if(x1>w_axis && x1<W && y1>10  && y1<H) {
			g2.drawString(str, (int)x1, (int)y1);
		}
	}
	protected void paint_peak_labels(Graphics2D g2) {
		g2.setStroke(new BasicStroke(3));
		boolean first = true;
		double prev_label_x = 0;
		Font f = g2.getFont();
		FontMetrics m = g2.getFontMetrics(f);
		for(Peak p: analyser.peaks) {
			String str = String.format("%.1f", p.x);
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
				draw_string(str, (int)label_x, (int)label_y, g2);
				prev_label_x = x + label_w;
				draw_line((int)x, (int)y+2, (int)x, (int)y+10, g2);
			}
		}
	}
	
	protected void paint_current_data(double current_x, int current_y) {
		
		g2.setColor(Color.BLUE);
		g2.setStroke(new BasicStroke(3));
		current_x = current_x * X_factor + x0;
		current_y *= Y_factor;
		draw_line((int)current_x, H, (int)current_x, 10, g2);
		g2.setStroke(new BasicStroke(10));
		g2.drawLine(w_axis/2, H - current_y, w_axis/2, H);
	}
}