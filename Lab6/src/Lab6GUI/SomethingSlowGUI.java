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
	private final JTextArea list_of_primes_window = new JTextArea();
	
	public SomethingSlowGUI() {
		super("Prime Time!");
		setSize(400,500);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(bottomLayout(), BorderLayout.SOUTH);
		getContentPane().add(centerLayout(), BorderLayout.CENTER);
		
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
						update_window.setText("");
						list_of_primes_window.setText("");
						
						ArrayList<Object> thread_num_array = new ArrayList<Object>();
						for(int i =1; i<7; i++) {
							thread_num_array.add(i);
						}	
						Integer thread_options = (Integer) JOptionPane.showInputDialog(null, "Select Number of Threads: ", "Thread Input Window", JOptionPane.PLAIN_MESSAGE, null, thread_num_array.toArray(), null);
						String prime_number_input = (String)JOptionPane.showInputDialog(null, "Enter a number you wish to find the primes for: ", "User Input Window", JOptionPane.PLAIN_MESSAGE, null, null, null);
						start_button.setEnabled(false);
						try {
							new UpdatePrimesFound().execute();
							SomethingSlow.call_main(thread_options,Integer.valueOf(prime_number_input));
						} catch (NumberFormatException e1) {
							e1.printStackTrace();
						} catch (Exception e1) {
							e1.printStackTrace();
						}
					}
				});
		
		return south_buttons;
	}
	
	private JPanel centerLayout() {
		JPanel center_layout = new JPanel();
		center_layout.setLayout(new GridLayout(2,2));
		center_layout.add(update_window);
		JScrollPane list_of_primes_scroll = new JScrollPane (list_of_primes_window, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		center_layout.add(list_of_primes_scroll);
		
		return center_layout;
	}
	
	private class UpdatePrimesFound extends SwingWorker<Void, String>{
		@Override
		protected Void doInBackground() throws Exception {
			int i = 0;
			while(SomethingSlow.calculation_completed()) {
				System.out.println("Hello");
				i+=1;
				publish(String.valueOf(i));
				Thread.sleep(1000);
			}
			list_of_primes_window.append("Here are the primes that were found: \n");
			List<Integer> list_of_primes = SomethingSlow.get_list_of_primes();
			for(Integer val: list_of_primes) {
				list_of_primes_window.append(Integer.toString(val)+ "\n");
			}
			start_button.setEnabled(true);
			return null;
		}
		public void process(List<String> time_ellapsed) {
			for(String s: time_ellapsed) {
				update_window.setText("Ellapsed Time: " + String.valueOf(s) + 
						" sec | Number of Primes Found: " + SomethingSlow.number_primes_found());
			}
		}
	}
	public static void main(String Args[]) {
		new SomethingSlowGUI();
	}
}
