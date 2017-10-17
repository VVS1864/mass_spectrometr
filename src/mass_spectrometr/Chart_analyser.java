package mass_spectrometr;

import java.util.ArrayList;

public class Chart_analyser {
	ArrayList<Double> x_data;
	ArrayList<Integer> y_data;
	int vol = 10;
	int middle_noise = 0;
	int n_noise_peak = 0;
	ArrayList<Peak> peaks = new ArrayList<Peak>();
	int summ_noise = 0;
	Chart_analyser(ArrayList<Double> x_data, ArrayList<Integer> y_data) {
		this.x_data = B_B_K(x_data, Run.K);
		this.y_data = y_data;
		analyse();
	}
	
	public static ArrayList<Double> B_B_K(ArrayList<Double> x_data, double K) {
		ArrayList<Double>new_x_data = new ArrayList<Double>();
		for (Double B: x_data) {
        	Double mass = B*B * K;
        	new_x_data.add(mass);
        } 
		return new_x_data;
	}
	/**
	 * get middle_noise and all peaks 
	 */
	public void analyse() {
		int i = 0;
		do {
			int ind = y_data.size() - i - 1;
			int y = y_data.get(ind);
			double x = x_data.get(ind);
			i++;
			if (i<10) {
				n_noise_peak++;
				summ_noise += y;
				continue;
			}
			middle_noise = summ_noise/n_noise_peak;
			if(y <= middle_noise+vol) {
				n_noise_peak++;
				summ_noise += y;
			}
			else {
				peaks.add(new Peak(x, y));
			}
		}
		while(i < x_data.size());
	}
	
	public void print() {
		for (int i = 0; i<peaks.size(); i++) {
			double x = peaks.get(i).x;
			int y = peaks.get(i).y;
			System.out.println(i+" x "+x + " y "+y);
			
		}
	}
	
}
