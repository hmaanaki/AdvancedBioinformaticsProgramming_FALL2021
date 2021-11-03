package Lab5;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.Random;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;

public class Lab5 extends JFrame{
	// a place for user to type
	// a place that shows right/wrong answers
	// a place for time left (count down from 30 sec) (end at 0)
	// start and cancel buttons
	private static final long serialVersionUID = 6548494654849L;
	
	public static String[] SHORT_NAMES = 
		{ "A","R", "N", "D", "C", "Q", "E", 
		"G",  "H", "I", "L", "K", "M", "F", 
		"P", "S", "T", "W", "Y", "V" };

	public static String[] FULL_NAMES = 
		{
		"alanine","arginine", "asparagine", 
		"aspartic acid", "cysteine",
		"glutamine",  "glutamic acid",
		"glycine" ,"histidine","isoleucine",
		"leucine",  "lysine", "methionine", 
		"phenylalanine", "proline", 
		"serine","threonine","tryptophan", 
		"tyrosine", "valine"};
	
	private static JLabel quiz_question = new JLabel();
	private JLabel timer_label = new JLabel();
	//private JTextField answer_check = new JTextField();
	private JTextField user_input = new JTextField();
	private JLabel question_feedback = new JLabel();
	private JButton start_button = new JButton("Start");
	private JButton cancel_button = new JButton("Cancel");
	private JButton submit_button = new JButton("Submit");
	
	private Random random = new Random();
	private int correct_answers = 0;
	private int incorrect_answers = 0;
	public static int val;
	public static boolean quiz_started;
	public static boolean end_of_quiz;
	public static boolean program_cancel;
	public static int i = 0;
	
	private class StartActionListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			start_button.setEnabled(false);
			program_cancel = false;
			quiz_started = true;
			end_of_quiz = false;
			new TheClock().execute();
			int n = random.nextInt(20);
			val = n;
			quiz_question.setText("What is the single letter code for " + FULL_NAMES[val] + "?");
		}
	}

	public Lab5() {
		super("Amino Acid Quiz v0.0.0.1");
		setLocationRelativeTo(null);
		setSize(800,500);
		quiz_question.setFont(new Font("Times", Font.PLAIN,15));
		timer_label.setFont(new Font("Times", Font.PLAIN,15));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(bottomLayout(), BorderLayout.SOUTH);
		start_button.addActionListener(new StartActionListener());
		getContentPane().add(centerLayout(),BorderLayout.CENTER);
		getContentPane().add(topLayout(),BorderLayout.NORTH);
		setVisible(true);
	}

	private void updateQuestion() {
		int n = random.nextInt(20);
		val= n;
		quiz_question.setText("What is the single letter code for " + FULL_NAMES[val] + "?");
		validate();
	}

	private class TheClock extends SwingWorker<Void, String>{			
			@Override
			protected Void doInBackground() throws Exception {
				i = 0;
				while(i<30) {
					if(program_cancel) {
						return null;
					}
					i++;
					publish(String.valueOf(30 - i));
					Thread.sleep(1000);
				}
				question_feedback.setText("Correct: " + correct_answers + "   |   Incorrect: " + incorrect_answers);
				publish("Time's Up!");
				i = 0;
				correct_answers = 0;
				incorrect_answers = 0;
				end_of_quiz = true;
				return null;		
			}
			
			public void process(List<String> remaining_time){
				timer_label.setText("Time Remaining: " + String.valueOf(remaining_time) + " | Correct: " + correct_answers + " | Incorrect: " + incorrect_answers);
			}
		};
	
	private JPanel bottomLayout() {
		JPanel south_buttons = new JPanel();
		south_buttons.setLayout(new GridLayout(0,2));
		south_buttons.add(start_button);
		south_buttons.add(cancel_button);
		cancel_button.addActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						start_button.setEnabled(true);
						quiz_started = false;
						program_cancel = true;
						correct_answers = 0;
						incorrect_answers = 0;
						user_input.setText("");
						timer_label.setText("");
						quiz_question.setText("");
						question_feedback.setText("");
					}
				});
		
		return south_buttons;
	}
	private JPanel centerLayout() {
		JPanel center_layout = new JPanel();
		center_layout.setLayout(new GridLayout(3,2,0,50));
		center_layout.add(user_input);
		//user_input.setPreferredSize(new Dimension(200,10));
		user_input.setHorizontalAlignment(SwingConstants.CENTER);
		user_input.setFont(new Font("Times", Font.PLAIN,30));
		center_layout.add(question_feedback);
		question_feedback.setHorizontalAlignment(SwingConstants.CENTER);
		question_feedback.setFont(new Font("Times",Font.PLAIN,20));
		center_layout.add(submit_button);
		submit_button.setMnemonic(KeyEvent.VK_H);
		submit_button.addActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						if(!quiz_started || end_of_quiz) {
							return;
						}
						if(user_input.getText().toUpperCase().equals(SHORT_NAMES[val])) {
							correct_answers+=1;
							question_feedback.setText("That's correct!");
						}
						else {
							incorrect_answers +=1;
							question_feedback.setText("That's incorrect! The correct answer is: " + SHORT_NAMES[val]);
						}
						user_input.setText("");
						updateQuestion();
						
					}
				});
		
		return center_layout;
	}
	private JPanel topLayout() {
		JPanel top_layout = new JPanel();
		top_layout.setLayout(new GridLayout(0,2));
		top_layout.add(quiz_question);
		top_layout.add(timer_label);
		return top_layout;
	}
	
	public static void main(String[] args){
		new Lab5();
		
	}
	
	
}
