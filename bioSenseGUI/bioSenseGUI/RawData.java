package bioSenseGUI;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import buildCalibrationCurve.CalibrationCurve;

public class RawData {
	
	private static final int NORMALIZED_DATA_SIZE = 700;
	private final String filename;
	private final float[][] raw_data;
	private final Double[] normalized_data;
	private final double nw_absorbance;
	private final double enzyme_activity;
	private final String enzyme_type;
	private final String substrate_type;
	private final double substrate_concentration;
	private final double sample_volume;
	private final int treatment_time;
	
	public RawData(String filename, float[][] raw_data,Double[] normalized_data, double nw_absorbance, 
			String substrate_type, double substrate_concentration, double sample_volume, int treatment_time, double enzyme_activity,
			String enzyme_type) {
		
		this.filename = filename;
		this.raw_data = raw_data;
		this.normalized_data = normalized_data;
		this.nw_absorbance = nw_absorbance;
		this.substrate_type = substrate_type;
		this.substrate_concentration = substrate_concentration;
		this.sample_volume = sample_volume;
		this.treatment_time = treatment_time;
		this.enzyme_activity = enzyme_activity;
		this.enzyme_type = enzyme_type;
	}
	
	public String getFilename() {return filename;}
	public float[][] getRawData(){return raw_data;}
	public Double[] getNormalizedData(){return normalized_data;}
	public double getAbsorbance(){return nw_absorbance;}
	public double getSubstrateConcentration() {return substrate_concentration;}
	public String getSubstrateType(){return substrate_type;}
	public double getSampleVolume(){return sample_volume;}
	public int getTreatmentTime(){return treatment_time;}
	public double getEnzymeActivity() {return enzyme_activity;}
	public String getEnzymeType() {return enzyme_type;}
	
	/**
	 * 
	 * @param f
	 * @return raw resistance data from csv file
	 * @throws IOException
	 */
	
	public static Double[] read_csv_data_file(File f) throws IOException{
		BufferedReader read_file = new BufferedReader(new FileReader(f));
		String line;
		int index = 0;
		ArrayList<Double> raw_data = new ArrayList<Double>();
		while((line = read_file.readLine()) != null) {
			String[] row_data = line.split(",", -1);
			String resistance_point = row_data[2].replaceAll("\"", "").trim();
			raw_data.add(Double.parseDouble(resistance_point));
			index += 1;
		}
		Double[] raw_data_array = (Double[]) raw_data.toArray(new Double[raw_data.size()]);
		read_file.close();
		return(raw_data_array);
	}
	
	/**
	 * 
	 * @param raw_data2
	 * @return smoothed raw data using a moving average filter
	 */
    public static Double[] moving_average_filter(Double[] raw_data2) {

        double window = 10;
        Double[] smoothed_raw_data = new Double[raw_data2.length];

        for (int i = 0; i + window <= raw_data2.length; i++) {
            //TODO Why sum is integer? Should it be double?
            double sum = 0;
            for (int j = i; j < i + window; j++) {
                sum += raw_data2[j];
            }
            //TODO Here if sum is integer and window is integer, then the result will be integer too.
            smoothed_raw_data[i] = sum / window;
        }

        return smoothed_raw_data;
    }
    
    /**
     * 
     * @param smoothed_raw_data
     * @return the maximum resistance given the smoothed raw data
     */  
	public static List<Double> find_maximum(Double[] smoothed_raw_data) {
		double max_resistance = 0;
		double max_res_timepoint = 0;
		for(int i = 0; i < smoothed_raw_data.length; i++ ) {
			if(smoothed_raw_data[i] > max_resistance) {
				max_resistance = smoothed_raw_data[i];
				max_res_timepoint = i;
			}
		}
		List<Double> alignment_point = new ArrayList<Double>();
		alignment_point.add(max_res_timepoint);
		alignment_point.add(max_resistance);
		return alignment_point;
	}
	
	/**
	 * 
	 * @param smoothed_raw_data
	 * @param max_resistance
	 * @return normalizes the smoothed raw data using the maximum resistance
	 */
	public static Double[] normalize_resistance_data(Double[] smoothed_raw_data, Double max_resistance){
		Double[] normalized_data = new Double[smoothed_raw_data.length];
		
		for(int i = 0; i <smoothed_raw_data.length; i++) {
			normalized_data[i] = smoothed_raw_data[i] / max_resistance;
		}
		return normalized_data;
	}
	
	/**
	 * 
	 * @param normalized_data
	 * @param max_res_timepoint
	 * @return returns the smoothed raw data after being aligned at the maximum
	 */
	public static Double[] align_normalized_data(Double[] normalized_data, double max_res_timepoint){
		
        Double[] adjusted_normalized_data =
                Arrays.copyOfRange(normalized_data, (int)max_res_timepoint, (int)max_res_timepoint + NORMALIZED_DATA_SIZE);
		return adjusted_normalized_data;
	}
	
	/**
	 * 
	 * @param normalized_data1
	 * @param normalized_data2
	 * @return determines the inhibition given two normalized curves
	 */
	public static double[] determine_inhibition(double[] normalized_data1, double[] normalized_data2) {
		double[] inhibition_values = new double[normalized_data1.length];
		// add check: if positive increase in cholinesterase activity
		// if negative decrease in cholinesterase activity
		for(int i = 0; i<normalized_data1.length; i++){
			inhibition_values[i] = ((1 / normalized_data2[i]) - (1 / normalized_data1[i]))
					/(1/normalized_data1[i])*100;
		}	
		return inhibition_values;
	}
	
	/**
	 * 
	 * @param normalized_data
	 * @param enzyme_type
	 * @param calibration_curve
	 * @return determines the cholinesterase activity given a calibration curve
	 */
	public static double determine_cholinesterase_activity(double[] normalized_data, String enzyme_type, CalibrationCurve calibration_curve) {
		// determine activity from calibration curve (inputed?)
		if(enzyme_type == "ACHE"){
			
			
		}
		else if(enzyme_type == "BCHE"){
			
		}
		return 0;
		
	}
	
	/**
	 * 
	 * @param raw_data_objects
	 * @param optimization_user_input
	 * @return generates an optimization curve with a log fit
	 */
	public double[][] GenerateOptimizationCurve(List<RawData> raw_data_objects, String optimization_user_input){
		// use raw_data.getNormalizedData()[ time-point ][] for the value as you iterate through each one
		
		
		// based on optimization_user_input 
		// :::: output [raw_data.getSubstrate e.g.][raw_data.getNormalizedData()[time-point][]
		
		return null;
	}
	
	public static Double[] data_processing_workflow(File f) throws IOException {
		Double[] smoothed_raw_data = read_csv_data_file(f);
		//Double [] smoothed_raw_data = moving_average_filter(raw_data);
		// [timepoint, resistance]
		List<Double> maximum_resistance = find_maximum(smoothed_raw_data);
		Double [] normalized_data = normalize_resistance_data(smoothed_raw_data, maximum_resistance.get(1));
		Double [] aligned_normalized_data = align_normalized_data(normalized_data, maximum_resistance.get(0));
		
		return aligned_normalized_data;
	}
}

