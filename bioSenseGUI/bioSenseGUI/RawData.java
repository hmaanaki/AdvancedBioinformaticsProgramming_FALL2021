package bioSenseGUI;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class RawData {
	
	private static final int NORMALIZED_DATA_SIZE = 1000;
	private final float[][] raw_data;
	private final float[][] normalized_data;
	private final double nw_absorbance;
	private final double enzyme_activity;
	private final String enzyme_type;
	private final HashMap<String, Double> substrate;
	private final double sample_volume;
	private final int treatment_time;
	
	public RawData(float[][] raw_data,float[][] normalized_data, double nw_absorbance, 
			HashMap<String, Double> substrate, double sample_volume, int treatment_time, double enzyme_activity,
			String enzyme_type) {
		
		this.raw_data = raw_data;
		this.normalized_data = normalized_data;
		this.nw_absorbance = nw_absorbance;
		this.substrate = substrate;
		this.sample_volume = sample_volume;
		this.treatment_time = treatment_time;
		this.enzyme_activity = enzyme_activity;
		this.enzyme_type = enzyme_type;
	}
	
	public float[][] getRawData(){return raw_data;}
	public float[][] getNormalizedData(){return normalized_data;}
	public double getAbsorbance(){return nw_absorbance;}
	public HashMap<String, Double> getSubstrate(){return substrate;}
	public double getSampleVolume(){return sample_volume;}
	public int getTreatmentTime(){return treatment_time;}
	public double getEnzymeActivity() {return enzyme_activity;}
	
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
		
        double[][] adjustedNormalizedData =
                Arrays.copyOfRange(normalized_data, max_res_timepoint, max_res_timepoint + NORMALIZED_DATA_SIZE);
		return normalized_data;
	}
	
	public static double[] determine_inhibition(double[][] normalized_data1, double[][] normalized_data2) {
		double[] inhibition_values = new double[normalized_data1.length];
		// add check: if positive increase in cholinesterase activity
		// if negative decrease in cholinesterase activity
		for(int i = 0; i<normalized_data1.length; i++){
			inhibition_values[i] = ((1 / normalized_data2[1][i]) - (1 / normalized_data1[1][i]))
					/(1/normalized_data1[1][i])*100;
		}	
		return inhibition_values;
	}
	
	//calibration curve object or user input calibration curve
	public static double determine_cholinesterase_activity(double[][] normalized_data, String enzyme_type, CalibrationCurve calibration_curve) {
		// determine activity from calibration curve (inputed?)
		if(enzyme_type == 'ACHE'){
			
			
		}
		else if(enzyme_type == 'BCHE'){
			
		}
		return enzyme_activity_calculated;
		
	}
	
	public double[][] GenerateOptimizationCurve(List<RawData> raw_data_objects, String optimization_user_input){
		// use raw_data.getNormalizedData()[ time-point ][] for the value as you iterate through each one
		
		
		// based on optimization_user_input 
		// :::: output [raw_data.getSubstrate e.g.][raw_data.getNormalizedData()[time-point][]
		
		return null;
	}
}

