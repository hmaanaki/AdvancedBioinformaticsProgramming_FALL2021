package infrastructureGUI;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
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
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;

public class BuildBiosensorGUI extends JFrame{
	
	private final JButton add_row_button = new JButton("Add Row");
	private final JButton process_files_button = new JButton("Process Files");
	private final JFileChooser meta_data_chooser = new JFileChooser();
	private final FileNameExtensionFilter csv_filter = new FileNameExtensionFilter("CSV file","csv");
	
	private final String[] columnNames = {"Plot","Raw Data File", "NW Absorbance",
			"Enzyme Name","Enzyme Activity (U/mL)","Substrate Name","Substrate Concentration (mM)",
			"Sample Volume", "Treatment Time"};
	
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
	};
	
	public static final long serialVersionUID = 6548495454849L;
	
	public void read_factors(String f) throws IOException{
		File file = new File(f);
		BufferedReader readFile = new BufferedReader(new FileReader(file));
		String line;
		
		while((line = readFile.readLine())!= null) {
			if(line.contains("raw")) {
				continue;
			}
			String[] entry = line.split(",", -1);
			List<Object> table_entry = new ArrayList();
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
			
			DefaultCategoryDataset normalized_data = new DefaultCategoryDataset();
			JFreeChart normalized_plot = ChartFactory.createLineChart("Normalized Curves","Time(s)","R/R_o", normalized_data);
			ChartPanel normalized_plot_panel = new ChartPanel(normalized_plot);
			normalized_plot_panel.setPreferredSize(new Dimension(450,400));
			
			DefaultCategoryDataset optimization_data = new DefaultCategoryDataset();
			JFreeChart optimization_plot = ChartFactory.createLineChart("Optimization Curve", "AChE Activity (U/mL)","(R/R_o)^-1", optimization_data);
			ChartPanel optimization_plot_panel = new ChartPanel(optimization_plot);
			optimization_plot_panel.setPreferredSize(new Dimension(450,400));
			
			center_figures.add(normalized_plot_panel);
			center_figures.add(optimization_plot_panel);
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
		JMenuItem exit_client = new JMenuItem("Exit");
		
		file_menu.add(import_item);
		JMenuItem meta_data_import = new JMenuItem("Import Meta Data");
		JMenuItem data_import = new JMenuItem("Import Data Files");
		import_item.add(meta_data_import);
		import_item.add(data_import);
		
		file_menu.add(export_item);
		JMenuItem meta_data_export = new JMenuItem("Export Meta Data (CSV)");
		JMenuItem figure_export = new JMenuItem("Export Figures (JPG)");
		JMenuItem normalized_data_export = new JMenuItem("Export Normalized Data (CSV)");
		export_item.add(meta_data_export);
		export_item.add(figure_export);
		export_item.add(normalized_data_export);
		
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
		
		return(menuBar);
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
				new ActionListener(){
					public void actionPerformed(ActionEvent e) {
						ResultsWindow resultsWindow = new ResultsWindow();
						resultsWindow.buildPlot();
					}
		});
		
		return south_buttons;
		
	}

	
	public static void main(String[] args) {
		new BuildBiosensorGUI();
	}
}

	

