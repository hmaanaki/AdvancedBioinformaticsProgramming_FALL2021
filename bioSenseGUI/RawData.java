package bioSenseGUI;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RawData {
	private final float[][] raw_data;
	private final double nw_absorbance;
	private final HashMap<String, Double> substrate;
	private final double sample_volume;
	private final int treatment_time;
	
	public float[][] getRawData(){
		return raw_data;
	}
	
	public double getAbsorbance(){
		return nw_absorbance;
	}
	
	public HashMap<String, Double> getSubstrate(){
		return substrate;
	}
	
	public double getSampleVolume(){
		return sample_volume;
	}
	
	public int getTreatmentTime(){
		return treatment_time;
	}
	
	
	
	public RawData(float[][] raw_data, double nw_absorbance, HashMap<String, Double> substrate, double sample_volume, int treatment_time, HashMap<String, Double> substrate2, double sample_volume2, double nw_absorbance2) {
		this.raw_data = raw_data;
		this.nw_absorbance = nw_absorbance;
		this.substrate = substrate;
		this.sample_volume = sample_volume;
		this.treatment_time = treatment_time;
		
	}
	
	public static List<Double> findMaximum(double[][] raw_data) {
		double max_resistance = 0;
		double max_res_timepoint = 0;
		for(int i = 0; i < raw_data.length; i++ ) {
			if(raw_data[0][i] > max_resistance) {
				max_resistance = raw_data[1][i];
				max_res_timepoint = raw_data[0][i];
			}
		}
		List<Double> alignment_point = new ArrayList<Double>();
		alignment_point.add(max_res_timepoint);
		alignment_point.add(max_resistance);
		return alignment_point;
	}
	
	public static double[][] normalize_resistance_data(double[][] raw_data, Double max_resistance){
		double[][] normalized_data = new double[raw_data.length][];
		
		for(int i = 0; i <raw_data.length; i++) {
			normalized_data[1][i] = raw_data[1][i] / max_resistance;
			normalized_data[0][i] = raw_data[0][i];
		}
		return normalized_data;
	}
	
	public static double[][] align_normalized_data(double[][] normalized_data, int max_res_timepoint){
		// Add code to align curves
		return normalized_data;
		
	}
	
	public static double[] determine_inhibition(double[][] normalized_data1, double[][] normalized_data2) {
		double[] inhibition_values = new double[normalized_data1.length];
		// adjust method to determine which is the curve with the largest response
		for(int i = 0; i<normalized_data1.length; i++){
			inhibition_values[i] = ((1 / normalized_data1[1][i]) - (1 / normalized_data2[1][i]))
					/(1/normalized_data1[1][i])*100;
		}	
		return inhibition_values;
	}	
}

public class BiosenseWindow{
	
}

public class CalibrationCurve{
	private final double sensitivity;
	private final String linear_equation;
	private final double limit_of_detection;
	private final double[] dynamic_range;
	private final double linearity;
	
	public CalibrationCurve(double sensitivity, String linear_equation, double limit_of_detection, double[] dynamic_range, double linearity) {
		this.sensitivity = sensitivity;
		this.linear_equation = linear_equation;
		this.limit_of_detection = limit_of_detection;
		this.dynamic_range = dynamic_range;
		this.linearity = linearity;
		
	}
	
	public double getSensitivity(){
		return sensitivity;
	}
	
	public String getLinearEquation(){
		return linear_equation;
	}
	
	public double getLOD(){
		return limit_of_detection;
	}
	
	public double[] getDynamicRange(){
		return dynamic_range;
	}
	
	public double getLinearity(){
		return linearity;
	}
}

