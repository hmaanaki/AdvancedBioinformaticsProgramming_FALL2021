package buildCalibrationCurve;

import java.util.List;
import bioSenseGUI.RawData;

public class CalibrationCurve {
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
	
	public double getSensitivity(){return sensitivity;}
	// Determine what data structure to put this in
	public String getLinearEquation(){return linear_equation;}	
	public double getLOD(){return limit_of_detection;}	
	public double[] getDynamicRange(){return dynamic_range;}	
	public double getLinearity(){return linearity;}
	
	public double generate_linear_equation(List<RawData> raw_data, String calibration_parameter) {
		
		// Linear fit the data based on calibration_parameter (enzyme activity or inhibition)
		
		// Determine the linear equation
		
		// call all the methods below to characterize the calibration curve
		
		
		new CalibrationCurve(determine_sensitivity(raw_data), linear_equation, 
				determine_limit_of_detection(raw_data), determine_dynamic_range(raw_data), 
				determine_linearity(raw_data));
	}
	
	public double characterize_calibration_curve(CalibrationCurve linear_equation) {
		// determine sensitivity double
		
		// determine limit of detection double
		
		//determine dynamic range double[]
		
		//determine linearity double
		
	}
}
