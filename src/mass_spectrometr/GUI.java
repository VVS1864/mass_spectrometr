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
import javax.swing.KeyStroke;

public class GUI {

	protected JButton button_connect;
	protected JButton button_pY;
	protected JButton button_mY;
	
	protected JButton button_pX;
	protected JButton button_mX;
	
	protected boolean is_ready = false;
	public GUI(){
		JFrame mainFrame = new JFrame("Java AWT Examples");
		mainFrame.setLayout(new BorderLayout());
	    mainFrame.setSize(800,600);
	    mainFrame.addWindowListener(new WindowAdapter() {
	         public void windowClosing(WindowEvent windowEvent){
	        	Run.arduino.close();
	            System.exit(0);
	            
	         }        
	      }); 
	    
//ActionListener  		
	    
		ActionListener connect_action_listener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!is_ready) {
					is_ready = Run.arduino.Connect();
					if (is_ready) {
						button_connect.setText("Start");
						set_status(Color.CYAN, "Arduino is ready");

					} else {
						set_status(Color.RED, "Port is busy or not found");
					}
				}
				else {
					Run.arduino.Start();
	  				if(!Run.transferring_data) {
	  					button_connect.setText("Stop");
	  					Run.transferring_data = true;
	  					set_status(Color.CYAN, "Transferring data");
	  				}
	  				else {
	  					button_connect.setText("Start");
	  					Run.transferring_data = false;
	  					set_status(Color.ORANGE, "Transferring stopped");
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
				if(Run.autoscaleY) {
					Run.autoscaleY = false;
					button_pY.setEnabled(true);
					button_mY.setEnabled(true);
				}
				else {
					Run.autoscaleY = true;
					button_pY.setEnabled(false);
					button_mY.setEnabled(false);
				}
				Run.cnvs.repaint();
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
	  			  		
//Status panel
	  	final JPanel status_panel = new JPanel();
	  	status_panel.setLayout(new BoxLayout(status_panel, BoxLayout.X_AXIS));
	  	JLabel status_info = new JLabel("Status: ");
	  	Run.status_info2 = new JLabel();
	  	
	  	status_panel.add(status_info);
	  	status_panel.add(Run.status_info2);
	  	status_panel.setAlignmentX(Component.LEFT_ALIGNMENT);
//Port panel
	    final JPanel port_panel = new JPanel();
		port_panel.setLayout(new BoxLayout(port_panel, BoxLayout.X_AXIS));
	    Run.pbox = new JComboBox<String>(Run.ports);
	    Run.pbox.setMaximumSize(new Dimension(140, 30));
	    //Run.pbox.setSelectedIndex(0);
	    button_connect = new JButton("Connect");
		button_connect.addActionListener(connect_action_listener);
		
		JButton button_l = new JButton("<");
		button_l.addActionListener(move_L);
		JButton button_r = new JButton(">");
		button_r.addActionListener(move_R);
		
		button_pY = new JButton("+");
		button_pY.addActionListener(plusY);
		button_pY.setEnabled(false);
		button_mY = new JButton("-");
		button_mY.addActionListener(minusY);
		button_mY.setEnabled(false);
		JButton button_a_scaleY = new JButton("Auto+-");
		button_a_scaleY.addActionListener(autoscaleY);
		
		
	    port_panel.add(Run.pbox);
	    port_panel.add(button_connect);
	    port_panel.add(button_l);
	    port_panel.add(button_r);
	    port_panel.add(button_pY);
	    port_panel.add(button_mY);
	    port_panel.add(button_a_scaleY);
	    port_panel.setAlignmentX(Component.LEFT_ALIGNMENT);
	    
//toolbar for canvas
	    final JPanel zoom_panel = new JPanel();
		zoom_panel.setLayout(new BoxLayout(zoom_panel, BoxLayout.Y_AXIS));
		
	    button_pX = new JButton("+");
		button_pX.addActionListener(plusX);
		
		button_mX = new JButton("--");
		button_mX.addActionListener(minusX);
		
		
		zoom_panel.add(button_pX);
		zoom_panel.add(button_mX);
	    mainFrame.add(zoom_panel, BorderLayout.WEST);
		
//Add all panels to frame
				
	    Run.cnvs = new Graph_canvas();
	    
	    KeyboardFocusManager.getCurrentKeyboardFocusManager()
	    .addKeyEventDispatcher(new KeyEventDispatcher() {
	        @Override
	        public boolean dispatchKeyEvent(KeyEvent e) {
	        	int action = e.getID();
	        	if (action == KeyEvent.KEY_PRESSED) {
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
	    
	    mainFrame.add(port_panel, BorderLayout.NORTH);
	    mainFrame.add(Run.cnvs,  BorderLayout.CENTER);
	    mainFrame.add(status_panel, BorderLayout.SOUTH);
	    
	    mainFrame.setVisible(true); 
	    
	    if(Run.ports.length==0) {
	    	set_status(Color.RED, "Port not found");
			button_connect.setEnabled(false);
		}
	}
	
	protected void set_status(Color c, String s) {
		Run.status_info2.setForeground(c);
		Run.status_info2.setText(s);
	}
	
	public void move(char left) {
		if(left == 'L') Run.x0 -= 10;
		else Run.x0 += 10;
		
		Run.cnvs.repaint();
	}
	
	/**
	 * 
	 * @param c '+' or '-'
	 * @param axis 'X' or 'Y'
	 */
	public void zoom(char c, char axis) {
		double s = (c == '+') ? Run.scale_rate : 1.0/Run.scale_rate;
		if (axis == 'Y') {
			Run.manual_Y_factor *= s;
		}
		else {
			double cn = (Run.cnvs.W/2-Run.x0);
			double o = cn*s - cn;
			Run.x0 -= (int)o;
			Run.manual_X_factor *= s;	
		}
		Run.cnvs.repaint();
	}

}


