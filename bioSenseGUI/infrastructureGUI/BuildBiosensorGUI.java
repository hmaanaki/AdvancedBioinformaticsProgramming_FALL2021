package infrastructureGUI;

import bioSenseGUI.RawData;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Paint;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

import org.apache.commons.math3.stat.regression.SimpleRegression;
import org.jfree.chart.ChartColor;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.LogarithmicAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class BuildBiosensorGUI extends JFrame{
	
	private final static List<RawData> list_of_tests = new ArrayList<RawData>();
	
	public static final Paint[] default_paint_sequence = ChartColor.createDefaultPaintArray();
	public static Paint color = null;
	public static int color_column_coordinate = 0;
	public static int color_row_coordinate = 0;
	
	private final JButton add_row_button = new JButton("Add Row");
	private final JButton process_files_button = new JButton("Process Files");
	private final JFileChooser meta_data_chooser = new JFileChooser();
	private final JFileChooser working_directory_chooser = new JFileChooser();
	private final FileNameExtensionFilter csv_filter = new FileNameExtensionFilter("CSV file","csv");
	private static String csv_data_output_filename = "";
	
	private static String working_directory = "";
	
	private final String[] columnNames = {"Plot","Raw Data File", "NW Absorbance",
			"Enzyme Name","Enzyme Activity (U/mL)","Substrate Name","Substrate Concentration (mM)",
			"Sample Volume", "Treatment Time"};
	
	private final XYSeriesCollection normalized_data_dataset = new XYSeriesCollection();
	private final  XYSeries normalized_data = new XYSeries("Normalized Data");
	
	private final XYSeriesCollection calibration_curve_dataset = new XYSeriesCollection();
	private final XYSeries calibration_curve_points = new XYSeries("Activity");
	
	SimpleRegression calibration_curve_regression = new SimpleRegression();
	
	private final DefaultTableModel meta_data_model = new DefaultTableModel(null, columnNames);
	private JTable meta_data_table = new JTable(meta_data_model) {
		private static final long serialVersionUID = 1L;

		@Override
		public Class getColumnClass(int column) {
			switch(column) {
				case 0:
					return Boolean.class;
				default:
					return String.class;
			}
		}
		
		public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
			Component cell_component = super.prepareRenderer(renderer, row, column);
			if(column == 0) {
				cell_component.setBackground((Color) default_paint_sequence[row]);
			}
				return cell_component;
		}
	};

	public static final long serialVersionUID = 6548495454849L;
	
	// write meta data and results to csv
	public void write_results(List<RawData> list_of_tests, File output_file_name) throws IOException {
		BufferedWriter row_writer = new BufferedWriter(new FileWriter(output_file_name));
		row_writer.write("raw_data_name,nw_absorbance (A),enzyme_name,enzyme_activity (U/mL)"
				+ ",substrate_name,substrate_concentration (mM),sample_volume (µL)"
				+ ",treatment_time(min)");
		for(RawData test:list_of_tests) {
			row_writer.write("\n" + test.getFilename() +","+ test.getAbsorbance()+","+ test.getEnzymeType()+","+
					test.getEnzymeActivity()+","+ test.getSubstrateType()+","+ test.getSubstrateConcentration()+","+
					test.getSampleVolume()+","+ test.getTreatmentTime());
		}
		// writer for filename and column headers
		String data_filenames = "\n\n";
		for(RawData test:list_of_tests) {
			data_filenames += test.getFilename() + ",,";
		}
		row_writer.write(data_filenames);
		
		String data_headers = "\n";
		for(int i =0; i<list_of_tests.size(); i++) {
			data_headers += "Time,Normalized Resistance,";
		}
		row_writer.write(data_headers);
		
		// add the writer for curve data (values)
		String row_data = "\n";
		for(int j = 0; j < list_of_tests.get(0).getNormalizedData().length ; j++){
			for(RawData test:list_of_tests) {
				row_data += j + "," + test.getNormalizedData()[j] + ",";
			}
			row_writer.write(row_data);
			row_data = "\n";
		}
		row_writer.close();
	}
	
	// export jpg file containing the figures
	public void export_figures() {
	
	}
	
	public void read_factors(String f) throws IOException{
		File file = new File(f);
		BufferedReader readFile = new BufferedReader(new FileReader(file));
		String line;
		
		while((line = readFile.readLine())!= null) {
			if(line.contains("raw")) {
				continue;
			}
			String[] entry = line.split(",", -1);
			List<Object> table_entry = new ArrayList<Object>();
			table_entry.add(true);
			for(String s : entry) {
				table_entry.add(s);
			}
			System.out.println(table_entry);
			add_row_to_table(table_entry.toArray(new Object[0]));
			
		}
		readFile.close();
	}
	
	public void add_row_to_table(Object [] table_entry) {
		meta_data_model.addRow(table_entry);
	}
	
	public BuildBiosensorGUI() {
		super("BioSense GUI v0.1");
		setSize(1100,300);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(buildMenuBar(), BorderLayout.NORTH);
		getContentPane().add(buildInputTable(), BorderLayout.CENTER);
		getContentPane().add(bottomLayout(), BorderLayout.SOUTH);
		setVisible(true);	
	}
	
	public Component buildInputTable() {	
		TableColumn enzyme_column = meta_data_table.getColumnModel().getColumn(3);
		TableColumn substrate_column = meta_data_table.getColumnModel().getColumn(5);
		JComboBox<String> enzyme_name = new JComboBox<String>();
		enzyme_name.addItem("AChE");
		enzyme_name.addItem("BChE");
		JComboBox<String> substrate_name = new JComboBox<String>();
		substrate_name.addItem("ACh");
		substrate_name.addItem("BCh");
		
		enzyme_column.setCellEditor(new DefaultCellEditor(enzyme_name));
		substrate_column.setCellEditor(new DefaultCellEditor(substrate_name));
		
		return new JScrollPane(meta_data_table);
	}
	
	public class ResultsWindow {
		
		public void buildPlot() {
			getContentPane().removeAll();	
			getContentPane().setLayout(new BorderLayout());
			JPanel center_figures = new JPanel();
			setSize(1000,700);
			setLocationRelativeTo(null);
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			
			JFreeChart normalized_plot = ChartFactory.createXYLineChart("Normalized Curves","Time(s)","R/R_o", normalized_data_dataset);
			normalized_plot.removeLegend();
			ChartPanel normalized_plot_panel = new ChartPanel(normalized_plot);
			//LogarithmicAxis yAxis = new LogarithmicAxis("Y");
			//XYPlot normalized = normalized_plot.getXYPlot();
			//normalized.setRangeAxis(yAxis);
			normalized_plot_panel.setPreferredSize(new Dimension(450,400));
			
			JFreeChart calibration_curve_plot = ChartFactory.createScatterPlot("Calibration Curve", "AChE Activity (U/mL)","(R/R_o)^-1", calibration_curve_dataset);
			ChartPanel calibration_curve_plot_panel = new ChartPanel(calibration_curve_plot);
			LogarithmicAxis xAxis = new LogarithmicAxis("AChE Activity (U/mL)");
			XYPlot plot = calibration_curve_plot.getXYPlot();
			plot.setDomainAxis(xAxis);
			calibration_curve_plot_panel.setPreferredSize(new Dimension(450,400));
			
			center_figures.add(normalized_plot_panel);
			center_figures.add(calibration_curve_plot_panel);
			getContentPane().add(buildMenuBar(), BorderLayout.NORTH);
			getContentPane().add(center_figures, BorderLayout.CENTER);
			
			Component table = buildInputTable();
			table.setPreferredSize(new Dimension(1000,200));
			getContentPane().add(table,BorderLayout.SOUTH);
			
			setVisible(true);
			
		}
	}
	
	public Component buildMenuBar(){
		JMenuBar menuBar = new JMenuBar();
		JMenu file_menu = new JMenu("File");
		JMenu import_item = new JMenu("Import");
		JMenu export_item = new JMenu("Export");
		JMenuItem set_directory = new JMenuItem("Set Working Directory");
		JMenuItem exit_client = new JMenuItem("Exit");
		
		file_menu.add(import_item);
		JMenuItem meta_data_import = new JMenuItem("Import Meta Data");
		JMenuItem data_import = new JMenuItem("Import Data Files");
		import_item.add(meta_data_import);
		import_item.add(data_import);
		
		file_menu.add(export_item);
		JMenuItem normalized_data_export = new JMenuItem("Export Normalized Data (CSV)");
		JMenuItem figure_export = new JMenuItem("Export Figures (JPG)");
		export_item.add(normalized_data_export);
		export_item.add(figure_export);
		
		file_menu.add(set_directory);
		
		file_menu.add(exit_client);
	
		menuBar.add(file_menu);
		
		meta_data_import.addActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent e){
						meta_data_chooser.setFileFilter(csv_filter);
						int return_val = meta_data_chooser.showOpenDialog(getParent());
						if( return_val == JFileChooser.APPROVE_OPTION) {
							try {
								File selected_filename = meta_data_chooser.getSelectedFile();
								System.out.println("Attempting to open meta data file... " + selected_filename);
								read_factors(selected_filename.getAbsolutePath());
								System.out.println("Meta data has been loaded...");
							}
							catch (Exception IncorrectFileException){
								System.out.println("Incompatible meta data file selected...");
							}
						}
					}
		});
		
		normalized_data_export.addActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent e){
						// Window for inputting export filename
						csv_data_output_filename = (String) JOptionPane.showInputDialog(null, "Input desired output filename: ", "CSV Output Filename Window", JOptionPane.PLAIN_MESSAGE, null, null, null);
						
						File output_filename = new File(working_directory, csv_data_output_filename + ".csv");
						try {
							write_results(list_of_tests,output_filename);
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
				});
		
		set_directory.addActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						directory_setter();
					}
				});
		
		return(menuBar);
	}
	
	public void directory_setter() {
		working_directory_chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int return_val = working_directory_chooser.showOpenDialog(getParent());
		if( return_val == JFileChooser.APPROVE_OPTION) {
			try {
				File selected_directory = working_directory_chooser.getSelectedFile();
				System.out.println("Setting working directory to...  " + selected_directory);
				working_directory = selected_directory.getAbsolutePath();
			}
			catch (Exception IncorrectFileException){
				// Add some exception
			}
		}
	}
	
	public static RawData create_data_object(String filename, Double[] normalized_data, double nw_absorbance, 
			String substrate_type, double substrate_concentration, double sample_volume, int treatment_time, double enzyme_activity,
			String enzyme_type) {
		list_of_tests.add(new RawData(filename, null, normalized_data, nw_absorbance, substrate_type, substrate_concentration,
				sample_volume, treatment_time, enzyme_activity, enzyme_type));
				
		return null;
	}
	
	private JPanel bottomLayout() {
		JPanel south_buttons = new JPanel();
		south_buttons.setLayout(new GridLayout(0,2));
		south_buttons.add(add_row_button);
		south_buttons.add(process_files_button);
		
		add_row_button.addActionListener(
				new ActionListener(){
					public void actionPerformed(ActionEvent e) {
						Object[] tableRow = {true,"","","","","","","",""};
						add_row_to_table(tableRow);
					}
		});
		
		process_files_button.addActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						// Check to see if working directory has been set
						if(working_directory.length() == 0) {
							directory_setter();
						}
						
						ResultsWindow resultsWindow = new ResultsWindow();
						resultsWindow.buildPlot();
						// Need to add argument for path to data folder!
						// create RawData Object here
						for(int i = 0; i< meta_data_table.getModel().getRowCount(); i++) {
							TableModel table_entry = meta_data_table.getModel();
							String filename = (String) table_entry.getValueAt(i,1);
							XYSeries normalized_data = new XYSeries((String)table_entry.getValueAt(i, 1));
							try {
								Double[] aligned_normalized_data = RawData.data_processing_workflow(new File(working_directory, filename));
								for(int j = 0; j < aligned_normalized_data.length; j++) {
									normalized_data.add(j, aligned_normalized_data[j]);
								}
								
								// create RawData Object
								double nw_absorbance = Double.parseDouble((String) table_entry.getValueAt(i, 2));
								String substrate_type = (String) table_entry.getValueAt(i, 5);
								double substrate_concentration = Double.parseDouble((String) table_entry.getValueAt(i, 6));
								double sample_volume = Double.parseDouble((String) table_entry.getValueAt(i, 7));
								int treatment_time = Integer.parseInt((String) table_entry.getValueAt(i, 8));
								double enzyme_activity = Double.parseDouble((String) table_entry.getValueAt(i, 4));
								String enzyme_type = (String) table_entry.getValueAt(i, 3);
								
								// create the data object for the row we are on
								create_data_object(filename, aligned_normalized_data, nw_absorbance, 
										substrate_type, substrate_concentration, sample_volume,
										treatment_time, enzyme_activity, enzyme_type);
								
								normalized_data_dataset.addSeries(normalized_data);
								calibration_curve_points.add(enzyme_activity,aligned_normalized_data[300]);
								
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						}
						calibration_curve_dataset.addSeries(calibration_curve_points);
					}	
		});
		return south_buttons;
	}
	
	// return the points of the calibration curve
	public  XYSeries get_calibration_curve_points() {
		return calibration_curve_points;
	}
	
	public static void main(String[] args) {
		new BuildBiosensorGUI();
	}
}

	

