package Lab6GUI;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import lab6.SomethingSlow;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.awt.BorderLayout;

public class SomethingSlowGUI extends JFrame{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JButton start_button = new JButton("Start");
	private JButton cancel_button = new JButton("Cancel");
	
	
	public SomethingSlowGUI() {
		super("Lets Factor!");
		setSize(300,500);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(bottomLayout(), BorderLayout.SOUTH);
		
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
						ArrayList<Object> thread_num_array = new ArrayList<Object>();
						for(int i =1; i<7; i++) {
							thread_num_array.add(i);
						}	
						Integer thread_options = (Integer) JOptionPane.showInputDialog(null, "Select Number of Threads: ", "Thread Input Window", JOptionPane.PLAIN_MESSAGE, null, thread_num_array.toArray(), null);
						String prime_number_input = (String)JOptionPane.showInputDialog(null, "Enter a number you wish to find the primes for: ", "User Input Window", JOptionPane.PLAIN_MESSAGE, null, null, null);
						start_button.setEnabled(false);
						try {
							SomethingSlow.call_main(thread_options,Long.parseLong(prime_number_input));
						} catch (NumberFormatException e1) {
							e1.printStackTrace();
						} catch (Exception e1) {
							e1.printStackTrace();
						}
					}
				});
		
		return south_buttons;
	}
	
	public static void main(String Args[]) {
		new SomethingSlowGUI();
	}
}
