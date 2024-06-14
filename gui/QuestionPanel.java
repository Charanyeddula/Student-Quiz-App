package cyeddula.project.quiz.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

import cyeddula.project.quiz.data.*;

public class  QuestionPanel extends JPanel {

	private JLabel lblNum;
	private JTextArea lblQuestion;
	private JButton btnSubmit;
	private JButton btnNext;
	private JButton btnEnd;
	private JRadioButton rdoSelected;
	//private JLabel lblError;
	private JPanel innerQPane;
	private JPanel buttonPane;
//	private JPanel timerPane;
	
	private Timer timer;
	private JLabel lblTimeProgress;
	private JLabel lblQuestionImage;
	
	
	private ActionListener radioBtnEventHandler;
	private ScoreListener scoreListener;//to pass score from questionPanel to quizPanel
	private SummaryListener summaryListener;//to pass score summary to summaryPanel at the end of quiz

	private static int score=0;//total score
	private static int attempted=0;//total questions attempted
	private static int correctAnswers=0;//no. of questions answered correctly
	private int timeTick=10;//for timer testing
	private boolean isSubmitted=false;
	
	private final String LAYOUTIMAGEPATH="./Resources/LayoutImages/";
	
	private Question newQuestion;
	protected QuestionBank qb;
	
	//to get questions for each topic
	private static GeographyBank geographyBank=new GeographyBank();
	private static ScienceBank scienceBank=new ScienceBank();
	private static EntertainmentBank entertainmentBank=new EntertainmentBank();
	private static SportsBank sportsBank=new SportsBank();
	
	private String quizSubject;
	
	/**
	 * Initialize values for first load
	 */
	private void initValues()
	{
		geographyBank=null;
		scienceBank=null;
		entertainmentBank=null;
		sportsBank=null;
		newQuestion=null;
		qb=null;
		attempted=0;
		score=0;
		correctAnswers=0;
		isSubmitted=false;
		
	}
	/**
	 * Create the panel.
	 */
	public QuestionPanel(String quizSubject, String name) {
		
		initValues();
		
		setPreferredSize(new Dimension(800, 700));
		
		//get question bank for the selected topic
		setQuizSubject(quizSubject);
		quizStart();
		
		setAlignmentY(JComponent.CENTER_ALIGNMENT);

		//Timer panel
		innerQPane=new JPanel();
		createQuestionAndOptions(name);
		add(innerQPane, JComponent.CENTER_ALIGNMENT);
		createButtons(name);


//		startAnimation();//start timer on first load
		
		
	}
	
	//setters
	public void setScoreListener(ScoreListener scoreListener) {
		this.scoreListener = scoreListener;
	}
	public void setSummaryListener(SummaryListener summaryListener) {
		this.summaryListener = summaryListener;
	}
	public void setQuizSubject(String quizSubject) {
		this.quizSubject = quizSubject;
	}

	//methods
	/**
	 * Create all questions and options
	 */
	private void createQuestionAndOptions(String name)
	{
		//get a random question from question bank
		newQuestion=qb.getRandomQuestion();
		if(newQuestion==null)
		{
			isSubmitted=false;
			//attempted all questions available in this topic
			//lblError.setText("You have attempted all the questions in this topic.");
			JOptionPane.showMessageDialog(null, 
					"Quiz Completed. Thank you",
					"QuizInfo",
				    JOptionPane.INFORMATION_MESSAGE);
			
			endQuiz(name);
			
		}
		else{
		
		innerQPane.setLayout(new BoxLayout(innerQPane, BoxLayout.Y_AXIS));
		innerQPane.setSize(750, 400);
		
		
		//populate data to controls
		JPanel numPane=new JPanel();
		lblNum=new JLabel("<html>Question "+(attempted)+" of 10<br/><br/></html>");
		lblNum.setFont(new Font("Chalkboard SE", Font.BOLD, 20));
		numPane.add(lblNum,JComponent.LEFT_ALIGNMENT);
		
		JPanel qTextPane=new JPanel();
		qTextPane.setAlignmentX(Component.CENTER_ALIGNMENT);
		lblQuestion=new JTextArea();
		lblQuestion.setLineWrap(true);
		lblQuestion.setWrapStyleWord(true);
		lblQuestion.setFont(new Font("Chalkboard SE", Font.BOLD, 15));
		lblQuestion.setText(newQuestion.getQuestionText());//question
		lblQuestion.setEditable(false);
		lblQuestion.setOpaque(false);
		lblQuestion.setSize(750, 100);
		qTextPane.add(lblQuestion);
		
		
		innerQPane.add(numPane);
		innerQPane.add(qTextPane);
		
		if(newQuestion.getQuestionType()== QuestionType.IMAGEQUESTION)
		{
			//set image for question
			JPanel qImgPane=new JPanel();
			qImgPane.setAlignmentX(Component.CENTER_ALIGNMENT);
			ImageIcon qIcon=new ImageIcon(newQuestion.getQuestionImage());
			lblQuestionImage=new JLabel(qIcon,SwingConstants.CENTER);
			lblQuestionImage.setPreferredSize(new Dimension(250, 250));
			
			qImgPane.add(lblQuestionImage);
			innerQPane.add(qImgPane,Component.CENTER_ALIGNMENT);
			
		}
		
		//options
		ButtonGroup optionGroup=new ButtonGroup();//to enable single selection.
		radioBtnEventHandler = new RadioButtonEventHandler();
		//dynamically generate radio buttons for answer options
		JPanel optionPane=new JPanel();
		optionPane.setLayout(new BoxLayout(optionPane, BoxLayout.Y_AXIS));
		
		optionPane.setAlignmentX(Component.CENTER_ALIGNMENT);
		for(String option :newQuestion.getOptions())
		{
			JRadioButton newOption=new JRadioButton(option);
			newOption.setActionCommand(option);//to check answer on submission
			newOption.addActionListener(radioBtnEventHandler);
			optionGroup.add(newOption);//add to button group
			newOption.setOpaque(true);
			
			optionPane.add(newOption);
			
		}
		innerQPane.add(optionPane,Component.LEFT_ALIGNMENT);//add to layout
		}
		
	}
	
	/**
	 * Create Submit,Next,End buttons
	 */
	private void createButtons(String name)
	{
		buttonPane=new JPanel();
		buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.Y_AXIS));
		
		//button to submit answer
		ImageIcon submitIcon=new ImageIcon(LAYOUTIMAGEPATH+"SubmitBlueButton.png");
		btnSubmit=new JButton("Submit");
		
		btnSubmit.setForeground(new Color(255, 255, 255));
		btnSubmit.setOpaque(true);
		btnSubmit.setFont(new Font("Chalkboard SE", Font.BOLD, 20));
		btnSubmit.setBackground(new Color(86, 183, 187));

		btnSubmit.setToolTipText("Submit");
		/**
		 * Submit button click
		 */
		btnSubmit.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				isSubmitted=true;
				//Check answer
				if(rdoSelected!=null)
				{
					//stop timer if an answer is selected
//					stopAnimation();

					btnSubmit.setVisible(false);
					btnNext.setVisible(true);
					
					
					if(rdoSelected.getActionCommand().equals(newQuestion.getAnswer()))
					{
						//correct, highlight in green, update Score
						rdoSelected.setSize(rdoSelected.getParent().getWidth(),rdoSelected.getHeight());//resizing to set bg color to full length of container
						score+=5;//increase score
						correctAnswers++;//increment correct answer counter
						qb.incrementCorrectAnswers();

						
						//invoke scoreUpdated to update new score to label
						scoreListener.scoreUpdated(score);
					}
					else
					{
						//wrong, highlight in red, update Score
						rdoSelected.setSize(rdoSelected.getParent().getWidth(),rdoSelected.getHeight());//resizing to set bg color to full length of container
//						rdoSelected.setBackground(Color.RED);
					}
					
					//if 10 questions attempted, end quiz.
					if(attempted==10)
					{
						//attempted--;
						JOptionPane.showMessageDialog(null, "Quiz completed! You can check your scores now.","Quiz completed",JOptionPane.INFORMATION_MESSAGE);
						//show summary pane
						endQuiz(name);
					}
				}
				else
				{
					isSubmitted=false;//is true only when an answer is selected and submitted
					//print message to select an answer
					JOptionPane.showMessageDialog(null, "To proceed, please select an answer.",
							"QuizAlert",
							JOptionPane.ERROR_MESSAGE);
					
				}
			}
		});
		
		
		//button to go to next question
		btnNext=new JButton("Next");
		
		btnNext.setForeground(new Color(255, 255, 255));
		btnNext.setOpaque(true);
		btnNext.setFont(new Font("Chalkboard SE", Font.BOLD, 20));
		btnNext.setBackground(new Color(86, 183, 187));
		
		btnNext.setVisible(false);
		/**
		 * Next button click event
		 */
		btnNext.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				isSubmitted=false;
				
				//if 10 questions attempted, end quiz.
				if(attempted==10)
				{
					
					JOptionPane.showMessageDialog(null, "Quiz completed! You can check you scores now.","Quiz completed",JOptionPane.INFORMATION_MESSAGE);
					//show summary pane
					endQuiz(name);
				}
				else{
					
					attempted++;//increment attempted counter
					setQuestionBank();//sets the question bank again as it can change depending on new topic

				innerQPane.removeAll();
				rdoSelected=null;
				createQuestionAndOptions(name);
				
				//show submit button
				btnSubmit.setVisible(true);
				//hide next button
				btnNext.setVisible(false);
				
				
				//Start timer for next question
				//using a separate thread
	             Thread worker = new Thread() {
	            	  
	                  public void run()
	                  {
	                	  // Show time ticking in timer pane


	                	  SwingUtilities.invokeLater( new Runnable(){
	                    
	                		  public void run()
	                		  {
	                			 // System.out.println("inside run");
//	                			  startAnimation();


	                		  }
	                	  });
	                  }
	             };
	   
	             worker.start();//start timer as a separate thread
				}
				
			}
		});
		
		
		buttonPane.add(btnSubmit);
		buttonPane.add(btnNext);
		
		
		JPanel endButtonPane=new JPanel();
		endButtonPane.setLayout(new BoxLayout(endButtonPane,BoxLayout.X_AXIS));
		
		btnEnd=new JButton("End Quiz");
		
		btnEnd.setForeground(new Color(255, 255, 255));
		btnEnd.setOpaque(true);
		btnEnd.setFont(new Font("Chalkboard SE", Font.BOLD, 20));
		btnEnd.setBackground(new Color(86, 183, 187));
		
		/**
		 * End button click event
		 */
		btnEnd.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// End quiz and display summary pane
				if(isSubmitted)
					endQuiz(name);
				else
				{
					JOptionPane.showMessageDialog(null, "Please submit an answer for this question before you end quiz.",
							"QuizAlert",
							JOptionPane.ERROR_MESSAGE);
					
				}
				
			}
		});
		endButtonPane.add(btnEnd);
		
		add(buttonPane,JComponent.CENTER_ALIGNMENT);
		add(endButtonPane,JComponent.RIGHT_ALIGNMENT);
		
	}

	class RadioButtonEventHandler implements ActionListener
	   {  
		//Saves the button that was selected.
	      public void actionPerformed(ActionEvent event){  
	    	  
	    	  rdoSelected=(JRadioButton)event.getSource();//to check answer on submit

	      }
	   }
	
	
	/**
	 * Set question bank for the selected topic
	 * 
	 */
	private void setQuestionBank()
	{
		//System.out.println("setting question bank for"+quizSubject);
		switch (quizSubject) {
		case "Geography":
			qb=geographyBank;
			geographyBank.incrementQuestionsAttempted();
			break;
		case "Science":
			qb=scienceBank;
			scienceBank.incrementQuestionsAttempted();
			break;
		case "Entertainment":
			qb=entertainmentBank;
			entertainmentBank.incrementQuestionsAttempted();
			break;
			case "Sports":
				qb=sportsBank;
				sportsBank.incrementQuestionsAttempted();
				//System.out.println("sports="+sportsBank.getQuestionsAttempted());
				break;
		default:
			//questionPane.qb=new GeographyBank();
			break;
		}
	}
	
	/**
	 * Set up variables and initialize question bank for first time load
	 */
	private void quizStart()
	{
		if(attempted==0)//first time loading quiz
		{
			attempted++;//increment counter for first time load
			geographyBank=new GeographyBank();
			scienceBank=new ScienceBank();
			entertainmentBank=new EntertainmentBank();
			sportsBank=new SportsBank();
			
			setQuestionBank();
		}
	}

	/**
	 * set style for timer label
	 * @param remaining
	 */

	//Timer option yet to be implemented.
	private void setTimerLabel(int remaining)
	{
		lblTimeProgress.setForeground(new Color(46, 139, 87));
		lblTimeProgress.setFont(new Font("Century Schoolbook", Font.BOLD, 35));
		lblTimeProgress.setText(String.valueOf(remaining));
	}

	ActionListener timerListener = new ActionListener() {

		// Define an action listener to respond to events
        // from the timer.  When an event is received, the
        // color of the display is changed.

        public void actionPerformed(ActionEvent evt) {

           setTimerLabel(timeTick);

           if(timeTick<5)
           {
        	   lblTimeProgress.setForeground(Color.RED);

           }

           if(timeTick==0)//stop animation when time expires
           {
        	   stopAnimation();
//        	   lblTimeProgress.setText("Time out!");//Show time out message
//        	   lblTimeProgress.setFont(new Font("Century Schoolbook", Font.BOLD, 50));

        	   isSubmitted=true;//when time out - similar to wrong answer submitted case.
        	   btnSubmit.setVisible(false);//hide submit button when time out.
   		  	   btnNext.setVisible(true);//show next button to proceed to next question.

           }

           timeTick--;
        }
     };

    /**
     * Start timer animation
     */
	void startAnimation() {
        if (timer == null) {
           timer = new Timer(1000, timerListener);
           timer.start();  // Make the time start running.
        }
    }

	/**
	 * Stop timer animation
	 */
    void stopAnimation() {

    	timeTick=10;//reset timer

       if (timer != null) {
          timer.stop();
		  timer = null;
       }
    }
    
    /**
     * End quiz, calculate results and show summary pane
     */
    private void endQuiz(String name)
    {
    	//calculate total percentage score of each subject
    	Map<String,Double> percentScore=new HashMap<String,Double>();
    	if(geographyBank.getQuestionsAttempted()>0)
    	{
    		percentScore.put(Subjects.GEOGRAPHY.toString(), geographyBank.getPercentageScore());
    	}
    	if(scienceBank.getQuestionsAttempted()>0)
    	{
    		percentScore.put(Subjects.SCIENCE.toString(),scienceBank.getPercentageScore());
    	}
    	if(entertainmentBank.getQuestionsAttempted()>0)
    	{
    		percentScore.put(Subjects.ENTERTAINMENT.toString(),entertainmentBank.getPercentageScore());
    	}
		if(sportsBank.getQuestionsAttempted()>0)
		{
			percentScore.put(Subjects.SPORTS.toString(),sportsBank.getPercentageScore());
		}
    	
    	//System.out.println(percentScore);
    	//show summary pane
    	ScoreSummary scoreSummary=new ScoreSummary();
    	scoreSummary.setTotalQuestions(attempted);
    	scoreSummary.setCorrectAnswers(correctAnswers);
    	scoreSummary.setStatistics(percentScore);
    	//invoke listener method
    	summaryListener.quizEnded(scoreSummary);

    }
    
	

}


