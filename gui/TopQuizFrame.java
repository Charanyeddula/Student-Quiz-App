/**
 * 
 */
package cyeddula.project.quiz.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class TopQuizFrame extends JFrame {

	//member components
	
	private JLabel lblTitle;
	
	
	private JButton btnStart;
	private JLabel lblError;
	
//	private HeaderPanel headerPane;
	private SubjectPanel subjectPane;
	
	private QuizPanel quizPane;
	
	
	private Container contentPane;
	
	private String subjectChosen;
	
	/**
	 * Initialize values for first load
	 */
	private void initValues()
	{
//		headerPane=null;
		subjectPane=null;
		quizPane=null;
		contentPane=null;
		subjectChosen=null;
		
	}

	/**
	 * Create the TopQuiz frame.
	 */

	public TopQuizFrame(String name) {
		
		super("TOP QUIZ");
		
		initValues();
		//get content pane
		contentPane=getContentPane();
		contentPane.setBackground(new Color(86, 183, 187));
		
		//FRAME properties
		//set default size and minimum size
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setSize(600,600);
		//    	setSize(screenSize.width, screenSize.height);
//    	setMinimumSize(new Dimension(800,500));
		//center the frame on screen
//		setLocationRelativeTo(null);
		contentPane.setBackground(new Color(86,183,187));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		
//		headerPane=new HeaderPanel();
		
		//set layout and add components
		
		contentPane.setLayout(new BorderLayout());
//		contentPane.add(headerPane,BorderLayout.NORTH);
		createWelcomePanel(name);
		
		
	}
	
	/**
	 * Creates welcome panel
	 */
	private void createWelcomePanel(String name)
	{
		JPanel welcomePane=new JPanel();
		welcomePane.setLayout(new GridLayout(0, 1));
//		welcomePane.setBackground(new Color(86,183,187));
//		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
				
		
		//topic selection panel
		subjectPane=new SubjectPanel();
//		subjectPane.setBackground(new Color(86,183,187));
		setVisible(true);
		subjectPane.setSubjectListener(new SubjectListener() {
			
			@Override
			public void subjectChosen(String subject) {
				// update the subject chosen for quiz
				subjectChosen=subject;
				
			}
		});
		
		lblError=new JLabel("",SwingConstants.CENTER);
		
		//add start button
		JPanel startPane=new JPanel();
		btnStart=new JButton();
		btnStart.setBorderPainted(true);
		btnStart.setContentAreaFilled(true);
//		btnStart.setBackground(new Color(86,183,187));
		btnStart.setVisible(true);
		btnStart.setIcon(new ImageIcon("./Resources/LayoutImages/startbutton.png"));
		btnStart.setToolTipText("Start Quiz");
//		btnStart.setPreferredSize(new Dimension(100, 100));

		startPane.add(btnStart,Component.CENTER_ALIGNMENT);
//		startPane.setBackground(new Color(86, 183, 187));

		/**
		 * Start button click event
		 */
		btnStart.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// Validate Subject selection
				if(subjectChosen!=null)
				{
					//ok, display QuizPanel, hide welcomePanel
					welcomePane.setVisible(true);

					//QUIZ PANEL
					quizPane=new QuizPanel(subjectChosen, name);
//					quizPane.setBackground(new Color(86,183,187));

					quizPane.setVisible(true);
					
					
					contentPane.add(quizPane,BorderLayout.CENTER);//add quizPane to contentPane
				}
				else
				{
					//else, show error message
					lblError.setText("Please choose a topic to proceed.");//add lblError to layout
					lblError.setForeground(Color.RED);
				}
				JFrame quizFrame = new JFrame("Quiz");
				quizFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				quizFrame.setSize(600, 600);
				quizFrame.add(new QuizPanel(subjectChosen, name));
				quizFrame.setVisible(true);

				dispose();

			}
		});
		
		
		
		welcomePane.add(subjectPane);
		welcomePane.add(startPane,Component.CENTER_ALIGNMENT);
		welcomePane.add(lblError);
//		welcomePane.setBackground(new Color(86,183,187));

		contentPane.add(welcomePane,BorderLayout.CENTER);//add to content pane

	}

}
