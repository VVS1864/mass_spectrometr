package mass_spectrometr;


import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

public class Graph_canvas extends JPanel {
	
	//public Graphics2D g2;
	public Graph_canvas () {
		setBackground (Color.lightGray);
		//setSize(800, 600);
		//g2 = (Graphics2D) g;
	}
	
	protected void paintComponent(Graphics g) {
        super.paintComponent(g);      
        Graphics2D g2 = (Graphics2D) g;
        if(Run.mass_data.size() <= 2) return;
        
        g.setColor(Color.RED);
        Double x_cur=0.0;
        for(int i = 2; i<Run.mass_data.size()-1; i+=2) {
        	Double x1 = Run.mass_data.get(i-2)*Run.X_factor;
        	Double y1 = Run.mass_data.get(i-1)*Run.Y_factor;
        	Double x2 = x_cur = Run.mass_data.get(i)*Run.X_factor;
        	Double y2 = Run.mass_data.get(i+1)*Run.Y_factor;
        	g2.drawLine(x1.intValue(), y1.intValue(), x2.intValue(), y2.intValue());
        }
        g2.setStroke(new BasicStroke(10));
        x_cur = x_cur/Run.X_factor*Run.Y_factor;
        
        g.drawLine(this.getWidth()-10, 0, this.getWidth()-10, x_cur.intValue());
	}
}