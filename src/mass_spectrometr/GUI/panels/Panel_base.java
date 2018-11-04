package mass_spectrometr.GUI.panels;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import mass_spectrometr.GUI.E_energy;
import mass_spectrometr.Run;
import mass_spectrometr.GUI.graphs.Graph_canvas;
import mass_spectrometr.GUI.graphs.Graph_mass;

public class Panel_base extends JPanel implements Panel_base_interfase{
	
	public  JLabel label_X;
	public  JLabel label_Y;

	
	protected JButton button_pY;
	protected JButton button_mY;
		
	protected final JPanel param_panel = new JPanel();
	protected final JPanel top_panel_1 = new JPanel();
	protected final JPanel top_panel_2 = new JPanel();
	public Graph_canvas cnvs;
	
	
	
	public Panel_base() {
		ActionListener move_L = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				move('L');
			}
			
		};
		ActionListener move_R = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				move('R');
			}
			
		};
		
		ActionListener plusY = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				zoom('+', 'Y');
			}
			
		};
		ActionListener minusY = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				zoom('-', 'Y');
			}
			
		};
		ActionListener autoscaleY = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(cnvs.autoscaleY) {
					cnvs.autoscaleY = false;
					button_pY.setEnabled(true);
					button_mY.setEnabled(true);
				}
				else {
					cnvs.autoscaleY = true;
					button_pY.setEnabled(false);
					button_mY.setEnabled(false);
				}
				cnvs.repaint();
			}
			
		};
		ActionListener plusX = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				zoom('+', 'X');
			}
			
		};
		ActionListener minusX = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				zoom('-', 'X');
			}
			
		};
		/*
		KeyboardFocusManager.getCurrentKeyboardFocusManager()
	    .addKeyEventDispatcher(new KeyEventDispatcher() {
	        @Override
	        public boolean dispatchKeyEvent(KeyEvent e) {
	        	int action = e.getID();
	        	Component focus = Run.prog.user_interface.mainFrame.getFocusOwner();
	        	
	        	//if (focus != m0_textbox && focus != b0_textbox && focus != k_textbox && focus != N_textbox &&
	        	//		action == KeyEvent.KEY_PRESSED) {
	        	if (!(focus instanceof JTextField) && action == KeyEvent.KEY_PRESSED) {	        		char c = e.getKeyChar();
	        		int code = e.getKeyCode();
	        		
					if (c == '+' | c == '-'){
						zoom(c, 'X');
					}
					else if(code == KeyEvent.VK_LEFT) {
						move('L');
					}
					else if(code == KeyEvent.VK_RIGHT) {
						move('R');
					}
	        	}
	          
	          return false;
	        }
	  	});
		*/
		param_panel.setLayout(new BoxLayout(param_panel, BoxLayout.Y_AXIS));
		top_panel_1.setLayout(new BoxLayout(top_panel_1, BoxLayout.X_AXIS));
		top_panel_2.setLayout(new BoxLayout(top_panel_2, BoxLayout.X_AXIS));
		
		JButton button_l = new JButton("<");
		button_l.addActionListener(move_L);
		JButton button_r = new JButton(">");
		button_r.addActionListener(move_R);

		JButton button_pX = new JButton("+");
		button_pX.addActionListener(plusX);

		JButton button_mX = new JButton("-");
		button_mX.addActionListener(minusX);
		
		setLayout(new BorderLayout());
	    
	    top_panel_1.add(button_l);
	    top_panel_1.add(button_r);
	    top_panel_1.add(button_pX);
	    top_panel_1.add(button_mX);
	    top_panel_1.setAlignmentX(Component.LEFT_ALIGNMENT);

	    param_panel.add(top_panel_1);
	    param_panel.add(top_panel_2);
	    
	  //toolbar for canvas
	    final JPanel zoom_panel = new JPanel();
		zoom_panel.setLayout(new BoxLayout(zoom_panel, BoxLayout.Y_AXIS));

		button_pY = new JButton("+");
		button_pY.addActionListener(plusY);
		button_pY.setEnabled(false);
		button_mY = new JButton("--");
		button_mY.addActionListener(minusY);
		button_mY.setEnabled(false);
		JButton button_a_scaleY = new JButton("A");
		button_a_scaleY.addActionListener(autoscaleY);

		zoom_panel.add(button_pY);
		zoom_panel.add(button_mY);
		zoom_panel.add(button_a_scaleY);
		
	    //cnvs = new Graph_mass(Run.prog.data_Bo, Run.prog.data_intensity);
	    
	    
	    add(param_panel, BorderLayout.NORTH);
	    
	    add(zoom_panel, BorderLayout.WEST);
	}
	@Override
	public void move(char left) {
		if(left == 'L') cnvs.x0 += 10;
		else cnvs.x0 -= 10;
		
		cnvs.repaint();
	}
	
	/**
	 * 
	 * @param c '+' or '-'
	 * @param axis 'X' or 'Y'
	 */
	@Override
	public void zoom(char c, char axis) {
		double s = (c == '+') ? cnvs.scale_rate : 1.0/cnvs.scale_rate;
		if (axis == 'Y') {
			double new_scale = cnvs.manual_Y_factor * s;
			if(new_scale > 0.25 || new_scale > cnvs.manual_Y_factor) cnvs.manual_Y_factor *= s;
			System.out.println(cnvs.manual_Y_factor);
		}
		else {
			double new_scale = cnvs.manual_X_factor * s;
			if(new_scale < 0.25) return;
			
			double cn = (cnvs.W/2-cnvs.x0);
			double o = cn*s - cn;
			cnvs.x0 -= (int)o;
			cnvs.manual_X_factor *= s;	
		}
		cnvs.repaint();
	}
}
