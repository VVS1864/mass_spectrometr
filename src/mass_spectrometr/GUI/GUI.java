package mass_spectrometr.GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.AbstractAction;
import javax.swing.BoxLayout;
import javax.swing.FocusManager;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

import mass_spectrometr.Run;
import mass_spectrometr.GUI.graphs.Graph_canvas;
import mass_spectrometr.GUI.graphs.Graph_mass;
import mass_spectrometr.GUI.panels.Panel_base;
import mass_spectrometr.GUI.panels.Panel_mass;

public class GUI {
	public JFrame mainFrame;
	public Panel_mass mass_panel;
	public E_energy e_energy_frame;
	
	protected JButton button_connect;
	protected JButton button_reset;
	
	public  JComboBox<String> portbox;
	
	
	public  JLabel status_info2;
	
	protected boolean is_ready = false;
	protected boolean reset = false;

	
	public GUI(){
		mainFrame = new JFrame("USSR Mass spectrometr software");
		mainFrame.setLayout(new BorderLayout());
	    mainFrame.setSize(1000,600);
	    mainFrame.setMinimumSize(new Dimension(640, 480));
	    mainFrame.addWindowListener(new WindowAdapter() {
	
	    	public void windowClosing(WindowEvent windowEvent){
	        	 Run.prog.close();
	        	        	
	        	 mainFrame.dispose();	       
	        	 System.exit(0);
	            
	         }        
	      }); 
// Electron energy window
	    e_energy_frame = new E_energy(mainFrame);
	    
//ActionListener  		
	    
	    ActionListener reset_action_listener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Run.prog.reset();
			}
		};
			
			
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
	  				if(!Run.prog.draw_graph_mass && !Run.prog.draw_graph_en_el) {
	  					button_connect.setText("Stop");
	  					Run.prog.draw_graph_mass = true;
	  					set_status(Color.CYAN, "Transfering data from arduino, draw graph");
	  				}
	  				else if(Run.prog.draw_graph_mass) {
	  					button_connect.setText("Draw graph");
	  					Run.prog.draw_graph_mass = false;
	  					set_status(Color.CYAN, "Transfering data from arduino");
	  				}
				}
			}
		};
		
		ActionListener show_hide_E = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				e_energy_frame.switch_visible();
			}
		};
		
		ActionListener calibration = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(!Run.prog.draw_graph_mass) {
					double[] scales = mass_panel.cnvs.get_scales();
					new Calibration_frame(mainFrame, scales[0], scales[1]);
				}
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
				
		final JPanel top_panel_0 = new JPanel();
		top_panel_0.setLayout(new BoxLayout(top_panel_0, BoxLayout.X_AXIS));
				
	    portbox = new JComboBox<String>(Run.prog.ports);
	    portbox.setMaximumSize(new Dimension(140, 30));
	    	    	
	    button_connect = new JButton("Connect");
		button_connect.addActionListener(connect_action_listener);
		
		button_reset = new JButton("Reset");
		button_reset.addActionListener(reset_action_listener);
		
		JButton button_E_energy = new JButton("E energy");
		button_E_energy.addActionListener(show_hide_E);
		
		JButton button_calibration = new JButton("Calibration");
		button_calibration.addActionListener(calibration);
		
	    top_panel_0.add(portbox);
	    top_panel_0.add(button_connect);
	    top_panel_0.add(button_reset);
	    top_panel_0.add(button_E_energy);
	    top_panel_0.add(button_calibration);
	    top_panel_0.setAlignmentX(Component.LEFT_ALIGNMENT);
	    
	    mass_panel = new Panel_mass();
		
		
	    
	    top_panel.add(top_panel_0);
	    
	   

//Add all panels to frame

	    mainFrame.add(top_panel, BorderLayout.NORTH);
	    mainFrame.add(mass_panel,  BorderLayout.CENTER);
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
	
	public void repaint_cnvs() {
		mass_panel.cnvs.repaint();
		e_energy_frame.energy_panel.cnvs.repaint();
	}
	
	public Component get_focus_owner() {
		Window active = FocusManager.getCurrentManager().getActiveWindow();
		Component focus = active.getFocusOwner();
		return focus;
	}

}


