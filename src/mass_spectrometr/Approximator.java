package mass_spectrometr;

import java.util.ArrayList;

public class Approximator {
	ArrayList<Double> B = new ArrayList<Double>();
	ArrayList<Long> data_time = new ArrayList<>();
	ArrayList<Long> data_time_cuted = new ArrayList<>();
	ArrayList<Double> data_Bo = new ArrayList<Double>();
	/**
	 * Approximates B with linear Least squares method
	 * @return Array of approximated B
	 */
	public ArrayList<Double> Approximate(ArrayList<Double> B, ArrayList<Long> data_time, int N) {
		this.data_time.addAll(data_time);
		this.data_time_cuted.addAll(data_time);
		this.B.addAll(B);
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
		double n = N;
		
		for (int i=0;i<n;i++){ 
			sum_t += data_time_cuted.get(i);
			sum_t2 += data_time_cuted.get(i)*data_time_cuted.get(i);
			sum_Bt += B.get(i)*data_time_cuted.get(i);
			sum_B += B.get(i);
		}
		
		double denom = n*sum_t2 - sum_t*sum_t;
		
		double a = (n*sum_Bt - sum_t*sum_B)/denom;
		double b = (sum_B*sum_t2 - sum_t*sum_Bt)/denom;
		///double a = (n*sum_Bt - sum_t*sum_B)/(n*sum_t2 - sum_t*sum_t);
		///double b = (sum_B - a*sum_t)/n;
		
		
		calc_approx_B(a, b);
		
		return data_Bo;
	}
	
	private void recalc_time() {
		long t_first = data_time.get(0);
		
		for (int i = 0; i < data_time.size(); i++) {
			long t_old = data_time.get(i);
			long t_new = t_old-t_first;
			data_time_cuted.set(i, t_new);
		}
	}
	private void calc_approx_B(double a, double b) {
		int i;
		for(i = 0; i < B.size(); i++) {
			//b = B.get(i);
			double t = data_time_cuted.get(i);
			double B_approx = a*t + b;
			data_Bo.add(B_approx);
		}
	}
	
	private int sum_int_square(ArrayList<Integer> m) {
		int sum = 0;
		for(Integer d : m)
		    sum += d*d;
		return sum;
	}
	
	private int sum_int(ArrayList<Integer> m) {
		int sum = 0;
		for(Integer d : m)
		    sum += d;
		return sum;
	}
	
	private double sum_double(ArrayList<Double> m) {
		double sum = 0;
		for(Double d : m)
		    sum += d;
		return sum;
	}
	
	private double multi_sum(ArrayList<Double> m, ArrayList<Integer> n) {
		int i;
		double sum = 0;
		for(i = 0; i < m.size(); i++)
		    sum += m.get(i)*n.get(i);
		return sum;
	}
	
	

}
