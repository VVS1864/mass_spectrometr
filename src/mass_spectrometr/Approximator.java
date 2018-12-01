package mass_spectrometr;

import java.util.ArrayList;

public class Approximator {
	private ArrayList<Double> B;
	private ArrayList<Long> data_time;
	private ArrayList<Long> data_time_cuted;
	private ArrayList<Double> data_Bo;
	private int n;
	/**
	 * Approximates B with linear Least squares method
	 * @return Array of approximated B
	 */
	public Approximator(ArrayList<Double> B, ArrayList<Long> data_time, int N) {
		this.n = N;
		this.B = new ArrayList<Double>();
		this.data_time_cuted = new ArrayList<Long>();
		this.data_time = new ArrayList<Long>();
		this.data_Bo = new ArrayList<Double>();
		
		//System.out.println("size " + data_Bo.size());
		
		this.data_time.addAll(data_time);
		this.data_time_cuted.addAll(data_time);
		this.B.addAll(B);
		this.data_Bo.addAll(B);
		/*
		double sum_B = sum_double(B);
		int sum_t = sum_int(data_time);
		double sum_Bt = multi_sum(B, data_time);
		int sum_t2 = sum_int_square(data_time);
		*/
	
		
		recalc_time();
		double sum_t = 0;
		double sum_t2 = 0;
		double sum_Bt = 0;
		double sum_B = 0;
		
		for (int i=0;i<n;i++){ 
			sum_t += data_time_cuted.get(i);
			sum_t2 += Math.pow(data_time_cuted.get(i), 2);
			//sum_t2 += data_time_cuted.get(i)*data_time_cuted.get(i);
			sum_Bt += B.get(i)*data_time_cuted.get(i);
			sum_B += B.get(i);
		}
		
		double denom = n*sum_t2 - Math.pow(sum_t, 2);
		//double denom = n*sum_t2 - sum_t*sum_t;
		
		double a = (n*sum_Bt - sum_t*sum_B)/denom;
		double b = (sum_B*sum_t2 - sum_t*sum_Bt)/denom;
		//System.out.println(a + " " + b + " " + denom);
		///double a = (n*sum_Bt - sum_t*sum_B)/(n*sum_t2 - sum_t*sum_t);
		///double b = (sum_B - a*sum_t)/n;
		
		
		calc_approx_B(a, b);
		
		
	}
	
	private void recalc_time() {
		long t_first = data_time.get(0);
		
		for (int i = 0; i < n; i++) {
			long t_old = data_time.get(i);
			long t_new = t_old-t_first;
			data_time_cuted.set(i, t_new);
		}
	}
	private void calc_approx_B(double a, double b) {
		int i;
		for(i = 0; i < n; i++) {
			//b = B.get(i);
			long
			t = data_time_cuted.get(i);
			double B_approx = a*t + b;
			data_Bo.set(i, B_approx);
		}
	}	
	
	public ArrayList<Double> get_approx(){
		return data_Bo;
	}

}
