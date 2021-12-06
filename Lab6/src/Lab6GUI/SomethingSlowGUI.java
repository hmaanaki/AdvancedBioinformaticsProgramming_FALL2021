package Lab6GUI;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingWorker;

import lab6.SomethingSlow;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.awt.BorderLayout;

public class SomethingSlowGUI extends JFrame{
	private static final long serialVersionUID = 1L;
	private final JButton start_button = new JButton("Start");
	private final JButton cancel_button = new JButton("Cancel");
	
	private final JLabel update_window = new JLabel();
	private final JLabel user_input_window = new JLabel();
	private final JTextArea list_of_primes_window = new JTextArea();
	
	private volatile static boolean cancel_hit = false;
	private volatile boolean calculation_start = false;
	private volatile boolean wait_for_call = true;
	private static Integer thread_options;
	private static String prime_number_input;
	
	public SomethingSlowGUI() {
		super("Prime Time!");
		setSize(400,500);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(bottomLayout(), BorderLayout.SOUTH);
		getContentPane().add(centerLayout(), BorderLayout.CENTER);
		cancel_button.setEnabled(false);
		
		setVisible(true);	
	}
	
	private JPanel bottomLayout() {
		JPanel south_buttons = new JPanel();
		south_buttons.setLayout(new GridLayout(0,2));
		
		south_buttons.add(start_button);
		south_buttons.add(cancel_button);
		
		start_button.addActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent e) {	
						// initialize some variables and adjust buttons
						cancel_button.setEnabled(true);
						start_button.setEnabled(false);
						
						update_window.setText("");
						list_of_primes_window.setText("");
						
						cancel_hit = false;
						calculation_start = false;
						wait_for_call = true;
						
						// used for creating the drop-down menu with thread selections
						ArrayList<Object> thread_num_array = new ArrayList<Object>();
						for(int i =1; i<7; i++) {
							thread_num_array.add(i);
						}	
						thread_options = (Integer) JOptionPane.showInputDialog(null, "Select Number of Threads: ", "Thread Input Window", JOptionPane.PLAIN_MESSAGE, null, thread_num_array.toArray(), null);
						prime_number_input = (String)JOptionPane.showInputDialog(null, "Enter a number you wish to find the primes for: ", "User Input Window", JOptionPane.PLAIN_MESSAGE, null, null, null);
						
						user_input_window.setText("Number of Threads Slected: " + thread_options +
								" | Number Input: " + prime_number_input);
						calculation_start = true;
						// reinitialize the variables in the main function doing the calculations
						SomethingSlow.reinitialize_variables_calculation_main();
						// updates the window
						new UpdatePrimesFound().execute();
						// starts the calculations
						new DoCalculations().execute();
						return;
					}
				});
		
		cancel_button.addActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						cancel_hit = true;
					}
				});
		
		return south_buttons;
	}
	
	private JPanel centerLayout() {
		JPanel center_layout = new JPanel();
		center_layout.setLayout(new GridLayout(3,2));
		center_layout.add(user_input_window);
		center_layout.add(update_window);
		JScrollPane list_of_primes_scroll = new JScrollPane (list_of_primes_window, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		center_layout.add(list_of_primes_scroll);
		
		return center_layout;
	}
	
	// calls the main method for SomethingSlow that does the calculations
	private class DoCalculations extends SwingWorker<Void, String>{
		@Override
		protected Void doInBackground() throws Exception {
			// waits till all the values have been entered before running
			while(wait_for_call) {
				if(calculation_start) {
					wait_for_call = false;
					SomethingSlow.call_main(thread_options,Integer.valueOf(prime_number_input));
				}
			}
			return null;
		}
	}
	
	// updates the gui window during calculations
	private class UpdatePrimesFound extends SwingWorker<Void, String>{
		@Override
		protected Void doInBackground() throws Exception {
			int i = 0;
			while(SomethingSlow.calculation_completed()) {
				// if cancel is pressed prints to the gui window
				if(cancel_hit) {
					list_of_primes_window.append("Here are the primes that were found: \n");
					List<Integer> list_of_primes = SomethingSlow.get_list_of_primes();
					start_button.setEnabled(true);
					// puts the list of primes in the window
					for(Integer val: list_of_primes) {
						list_of_primes_window.append(Integer.toString(val)+ "\n");
					}
					break;
				}
				// used as a timer (accurate to the second only)
				i+=1;
				publish(String.valueOf(i));
				Thread.sleep(1000);
			}
			list_of_primes_window.append("Here are the primes that were found: \n");
			List<Integer> list_of_primes = SomethingSlow.get_list_of_primes();
			
			for(Integer val: list_of_primes) {
				list_of_primes_window.append(Integer.toString(val)+ "\n");
			}
			
			update_window.setText("Ellapsed Time: " + String.valueOf(i) + 
					" sec | Number of Primes Found: " + SomethingSlow.number_primes_found());
			
			start_button.setEnabled(true);
			cancel_button.setEnabled(false);
			
			return null;
		}
		public void process(List<String> time_ellapsed) {
			for(String s: time_ellapsed) {
				update_window.setText("Ellapsed Time: " + String.valueOf(s) + 
						" sec | Number of Primes Found: " + SomethingSlow.number_primes_found());
			}
		}
	}
	
	public static boolean cancel_pressed() {
		return cancel_hit;
	}
	
	public static void main(String Args[]) throws NumberFormatException, Exception {
		new SomethingSlowGUI();	
	}
}
