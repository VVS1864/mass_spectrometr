package mass_spectrometr;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.AbstractAction;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

public class GUI {
	public  JComboBox<String> portbox;
	public  Graph_canvas cnvs;

	public  JLabel status_info2;
	public  JLabel label_mass;
	public  JLabel label_intensity;

	protected JButton button_connect;
	protected JButton button_pY;
	protected JButton button_mY;
	
	protected JTextField m0_textbox;
	protected JTextField k_textbox;
	protected JTextField b0_textbox;
	protected JTextField N_textbox;
	protected boolean is_ready = false;
	protected boolean reset = false;
	public GUI(){
		JFrame mainFrame = new JFrame("Java AWT Examples");
		mainFrame.setLayout(new BorderLayout());
	    mainFrame.setSize(800,600);
	    mainFrame.addWindowListener(new WindowAdapter() {
	         public void windowClosing(WindowEvent windowEvent){
	        	 Run.prog.close();
	        	        	
	        	 mainFrame.dispose();	       
	        	 System.exit(0);
	            
	         }        
	      }); 
	    
//ActionListener  		
	    
		ActionListener connect_action_listener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!is_ready) {
					is_ready = Run.prog.arduino.Connect();
					if (is_ready) {
						button_connect.setText("Draw graph");
						set_status(Color.CYAN, "Transfering data from arduino");

					} else {
						set_status(Color.RED, "Port is busy or not found");
					}
				}
				else {
	  				if(!Run.prog.draw_graph && !reset) {
	  					button_connect.setText("Stop");
	  					Run.prog.draw_graph = true;
	  					set_status(Color.CYAN, "Transfering data from arduino, draw graph");
	  				}
	  				else if(Run.prog.draw_graph && !reset) {
	  					button_connect.setText("Reset");
	  					Run.prog.draw_graph = false;
	  					reset = true;
	  					set_status(Color.CYAN, "Transfering data from arduino");
	  				}
	  				else if(reset) {
	  					button_connect.setText("Draw graph");
	  					set_status(Color.CYAN, "Transfering data from arduino");
	  					Run.prog.draw_graph = false;
	  					reset = false;
	  					Run.prog.reset();
	  				}
	  				
				}
			}
		};
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
				if(Run.prog.autoscaleY) {
					Run.prog.autoscaleY = false;
					button_pY.setEnabled(true);
					button_mY.setEnabled(true);
				}
				else {
					Run.prog.autoscaleY = true;
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
		ActionListener update_K = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String str_m0 = m0_textbox.getText();
				double new_M0;
				String str_k = k_textbox.getText();
				double new_K;
				String str_b0 = b0_textbox.getText();
				double new_B0;
				String str_N = N_textbox.getText();
				int new_N;
				
				try {
					new_M0 = Double.parseDouble(str_m0);
					new_K = Double.parseDouble(str_k);
					new_B0 = Double.parseDouble(str_b0);
					new_N = Integer.parseInt(str_N);
				}
				catch(NumberFormatException ex) {
					k_textbox.setBackground(Color.RED);
					m0_textbox.setBackground(Color.RED);
					b0_textbox.setBackground(Color.RED);
					N_textbox.setBackground(Color.RED);
					return;
				}
				k_textbox.setBackground(Color.WHITE);
				m0_textbox.setBackground(Color.WHITE);
				b0_textbox.setBackground(Color.WHITE);
				N_textbox.setBackground(Color.WHITE);
				Run.prog.M0 = new_M0;
				Run.prog.K = new_K;
				Run.prog.B0 = new_B0;
				Run.prog.approx_N = new_N;
				
				cnvs.repaint();
			}
			
		};
	  			  		
//Status panel
	  	final JPanel status_panel = new JPanel();
	  	status_panel.setLayout(new BoxLayout(status_panel, BoxLayout.X_AXIS));
	  	JLabel status_info = new JLabel("Status: ");
	  	status_info2 = new JLabel();
	  	
	  	status_panel.add(status_info);
	  	status_panel.add(status_info2);
	  	status_panel.setAlignmentX(Component.LEFT_ALIGNMENT);
//Port panel
	    final JPanel top_panel = new JPanel();
		top_panel.setLayout(new BoxLayout(top_panel, BoxLayout.Y_AXIS));
		
		final JPanel top_panel_1 = new JPanel();
		top_panel_1.setLayout(new BoxLayout(top_panel_1, BoxLayout.X_AXIS));
		
		final JPanel top_panel_2 = new JPanel();
		top_panel_2.setLayout(new BoxLayout(top_panel_2, BoxLayout.X_AXIS));
		
	    portbox = new JComboBox<String>(Run.prog.ports);
	    portbox.setMaximumSize(new Dimension(140, 30));
	    	    	
	    button_connect = new JButton("Connect");
		button_connect.addActionListener(connect_action_listener);
		
		JButton button_l = new JButton("<");
		button_l.addActionListener(move_L);
		JButton button_r = new JButton(">");
		button_r.addActionListener(move_R);
		
		JButton button_pX = new JButton("+");
		button_pX.addActionListener(plusX);
		
		JButton button_mX = new JButton("-");
		button_mX.addActionListener(minusX);
		
	    top_panel_1.add(portbox);
	    top_panel_1.add(button_connect);
	    top_panel_1.add(button_l);
	    top_panel_1.add(button_r);
	    top_panel_1.add(button_pX);
	    top_panel_1.add(button_mX);
	    top_panel_1.setAlignmentX(Component.LEFT_ALIGNMENT);
	    
	    
		
		JLabel coefficients = new JLabel("Coefficients: ");
		JLabel M0 = new JLabel("M0: ");
		m0_textbox = new JTextField(Double.toString(Run.prog.M0));
		m0_textbox.setMaximumSize(new Dimension(50, 40));
		JLabel K = new JLabel("K: ");
		k_textbox = new JTextField(Double.toString(Run.prog.K));
		k_textbox.setMaximumSize(new Dimension(50, 40));
		JLabel B0 = new JLabel("B0: ");
		b0_textbox = new JTextField(Double.toString(Run.prog.B0));
		b0_textbox.setMaximumSize(new Dimension(50, 40));
		JLabel N = new JLabel("N: ");
		N_textbox = new JTextField(Integer.toString(Run.prog.approx_N));
		N_textbox.setMaximumSize(new Dimension(50, 40));
		JButton button_K = new JButton("Update");
		button_K.addActionListener(update_K);
		JLabel current_mass = new JLabel("Mass: ");
		label_mass = new JLabel();
		JLabel spacer = new JLabel("  ");
		JLabel current_intensity = new JLabel("Intensity: ");
		label_intensity = new JLabel();		
		
		top_panel_2.add(coefficients);
		top_panel_2.add(M0);
		top_panel_2.add(m0_textbox);
		top_panel_2.add(K);
	    top_panel_2.add(k_textbox);
	    top_panel_2.add(B0);
	    top_panel_2.add(b0_textbox);
	    top_panel_2.add(N);
	    top_panel_2.add(N_textbox);
	    top_panel_2.add(button_K);
	    top_panel_2.add(current_mass);
	    top_panel_2.add(label_mass);
	    top_panel_2.add(spacer);
	    top_panel_2.add(current_intensity);
	    top_panel_2.add(label_intensity);
	    top_panel_2.setAlignmentX(Component.LEFT_ALIGNMENT);
	    
	    top_panel.add(top_panel_1);
	    top_panel.add(top_panel_2);
	    
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
	    mainFrame.add(zoom_panel, BorderLayout.WEST);
		
//Add all panels to frame
				
	    cnvs = new Graph_canvas();
	    
	    KeyboardFocusManager.getCurrentKeyboardFocusManager()
	    .addKeyEventDispatcher(new KeyEventDispatcher() {
	        @Override
	        public boolean dispatchKeyEvent(KeyEvent e) {
	        	int action = e.getID();
	        	Component focus = mainFrame.getFocusOwner();
	        	if (focus != m0_textbox && focus != b0_textbox && focus != k_textbox && focus != N_textbox &&
	        			action == KeyEvent.KEY_PRESSED) {
	        		char c = e.getKeyChar();
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
	    
	    mainFrame.add(top_panel, BorderLayout.NORTH);
	    mainFrame.add(cnvs,  BorderLayout.CENTER);
	    mainFrame.add(status_panel, BorderLayout.SOUTH);
	    
	    mainFrame.setVisible(true); 
	    
	    if(Run.prog.ports.length==0) {
	    	set_status(Color.RED, "Port not found");
			button_connect.setEnabled(false);
			
	    }
		else {
			portbox.setSelectedIndex(0);
		    }
		
	}
	
	protected void set_status(Color c, String s) {
		status_info2.setForeground(c);
		status_info2.setText(s);
	}
	
	public void move(char left) {
		if(left == 'L') Run.prog.x0 += 10;
		else Run.prog.x0 -= 10;
		
		cnvs.repaint();
	}
	
	/**
	 * 
	 * @param c '+' or '-'
	 * @param axis 'X' or 'Y'
	 */
	public void zoom(char c, char axis) {
		double s = (c == '+') ? Run.prog.scale_rate : 1.0/Run.prog.scale_rate;
		if (axis == 'Y') {
			double new_scale = Run.prog.manual_Y_factor * s;
			if(new_scale > 0.25) Run.prog.manual_Y_factor *= s;
		}
		else {
			double new_scale = Run.prog.manual_X_factor * s;
			if(new_scale < 0.25) return;
			
			double cn = (cnvs.W/2-Run.prog.x0);
			double o = cn*s - cn;
			Run.prog.x0 -= (int)o;
			Run.prog.manual_X_factor *= s;	
		}
		cnvs.repaint();
	}

}


