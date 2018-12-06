package mass_spectrometr;

public class Arduino_state_controller extends Thread{
	private boolean is_runing = false;
	@Override
	public void run() {
		
		while(true) {
			if(!Run.prog.arduino.is_running()) {
				Run.prog.arduino.reconnect();
			}
				
			
			try{
				sleep(2000);
			}
			catch(InterruptedException e) {}
		}
	}
	@Override
	public void start() {
		if(!is_runing) {
			is_runing = true;
			super.start();
		}
	}
}
