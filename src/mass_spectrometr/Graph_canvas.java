package mass_spectrometr;


import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

public class Graph_canvas extends JPanel {
	
	private final int h_axis = 51;
	private double max_intensity = 0;
	//public Graphics2D g2;
	public Graph_canvas () {
		setBackground (Color.lightGray);
	}
	
	protected void paintComponent(Graphics g) {
        super.paintComponent(g);      
        Graphics2D g2 = (Graphics2D) g;
        if(Run.mass_data.size() <= 2) return;
        
        int  H = this.getHeight()-h_axis;
        //factors
        //double X_factor;
        //double Y_factor;
        if(Run.autoscale) {
        	Run.Y_factor = H/max_intensity;
        }
        
        g.setColor(Color.RED);
        
        Double current_mass = Run.mass_data.get(Run.mass_data.size()-2);
        Double current_intensity = Run.mass_data.get(Run.mass_data.size()-1);
    	if (current_intensity > max_intensity) max_intensity = current_intensity;
    	System.out.println("maxintens =" + max_intensity);
        for(int i = 2; i<Run.mass_data.size()-1; i+=2) {        	
        	Double x1 = Run.mass_data.get(i-2)*Run.X_factor + Run.x0;
        	Double y1 = H - (Run.mass_data.get(i-1)*Run.Y_factor);
        	Double x2 = Run.mass_data.get(i)*Run.X_factor + Run.x0;
        	Double y2 = H - (Run.mass_data.get(i+1)*Run.Y_factor);
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
		mass=mass*Run.X_factor + Run.x0;
		g2.drawLine(mass.intValue(), this.getHeight()-h_axis, mass.intValue(), 0);
	}
}