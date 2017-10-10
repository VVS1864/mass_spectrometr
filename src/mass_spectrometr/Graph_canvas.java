package mass_spectrometr;


import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

public class Graph_canvas extends JPanel {
	
	private final int h_axis = 51;
	private final int w_axis = 51;
	private double max_intensity = 0;
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
        
        g.setColor(Color.BLACK);
        g2.setStroke(new BasicStroke(3));
        g2.drawLine(x0, 0, x0, H);
        
        if(Run.mass_data.size() <= 2) return;
        
        
        //factors
        if(Run.autoscaleY) {
        	Y_factor = Run.manual_Y_factor = H/max_intensity;
        }
        else {
        	Y_factor = Run.manual_Y_factor;
        }
        
        X_factor = Run.manual_X_factor;
        
        g.setColor(Color.RED);
        g2.setStroke(new BasicStroke(1));
        Double current_mass = Run.mass_data.get(Run.mass_data.size()-2);
        Double current_intensity = Run.mass_data.get(Run.mass_data.size()-1);
    	if (current_intensity > max_intensity) max_intensity = current_intensity;
    	if (current_mass > max_mass) max_mass = current_mass;
        for(int i = 2; i<Run.mass_data.size()-1; i+=2) {        	
        	Double x1 = Run.mass_data.get(i-2)*X_factor + x0;
        	Double y1 = H - (Run.mass_data.get(i-1)*Y_factor);
        	Double x2 = Run.mass_data.get(i)*X_factor + x0;
        	Double y2 = H - (Run.mass_data.get(i+1)*Y_factor);
        	g2.drawLine(x1.intValue(), y1.intValue(), x2.intValue(), y2.intValue());
        	
        }
        //g2.setStroke(new BasicStroke(10));
       // System.out.println("Mass " + x_cur/Run.X_factor);
        //x_cur = x_cur/Run.X_factor*Run.Y_factor;
        paint_current_mass(current_mass, g2);
	}
	
	private void paint_current_mass(Double mass, Graphics2D g2) {
		g2.setColor(Color.BLUE);
		g2.setStroke(new BasicStroke(3));
		mass=mass*X_factor + x0;
		g2.drawLine(mass.intValue(), H, mass.intValue(), 0);
	}
}