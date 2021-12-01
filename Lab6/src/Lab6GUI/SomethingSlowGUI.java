package Lab6GUI;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.BorderLayout;

public class SomethingSlowGUI extends JFrame{
	
	private JButton start_button = new JButton("Start");
	private JButton cancel_button = new JButton("Cancel");
	
	
	public SomethingSlowGUI() {
		super("Lets Factor!");
		setLocationRelativeTo(null);
		setSize(500,500);
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
						JPanel user_input_window = new JPanel();
						
						JComboBox<Integer> num_threads = new JComboBox<Integer>();
						for(int i =0; i<7; i++) {
							num_threads.addItem(i);
						}
						Object[] thread_number = new Object[] {};
						JOptionPane thread_options = new JOptionPane("Select Number of Threads: ", JOptionPane.QUESTION_MESSAGE, JOptionPane.DEFAULT_OPTION, null, thread_number, null);
						thread_options.add(num_threads);
						String prime_number_input = (String)JOptionPane.showInputDialog(null, "Enter a number you wish to find the primes for: ", "User Input Window", JOptionPane.PLAIN_MESSAGE, null, null, null);
						thread_options.add(thread_options);
					}
				});
		
		return south_buttons;
	}
	
	public static void main(String Args[]) {
		new SomethingSlowGUI();
	}
}
