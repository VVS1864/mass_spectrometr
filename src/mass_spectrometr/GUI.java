package mass_spectrometr;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class GUI {

	protected JButton button_connect;
	protected boolean is_ready = false;
	public GUI(){
		JFrame mainFrame = new JFrame("Java AWT Examples");
		mainFrame.setLayout(new BorderLayout());
	    mainFrame.setSize(800,600);
	    mainFrame.addWindowListener(new WindowAdapter() {
	         public void windowClosing(WindowEvent windowEvent){
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
						set_status(Color.RED, "Port not found");
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
		
	    port_panel.add(Run.pbox);
	    port_panel.add(button_connect);
	    port_panel.setAlignmentX(Component.LEFT_ALIGNMENT);
		
//Add all panels to frame
				
	    Run.cnvs = new Graph_canvas();
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

}


