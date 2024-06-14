/**
 * 
 */
package cyeddula.project.quiz.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class QuizPanel extends JPanel {

	
	private SubjectPanel subjectPane;
	private QuestionPanel questionPane;
	private SummaryPanel summaryPane;
	
	private String quizSubject;
	//setter
	public void setQuizSubject(String quizSubject) {
		this.quizSubject = quizSubject;
	}

	/**
	 * Initialize values for first load
	 */
	private void initValues()
	{
		subjectPane=null;
//		questionPane=null;
		summaryPane=null;
	}
	
	/**
	 * Create quiz panel.
	 */

	public QuizPanel(String subject, String name) {
		initValues();
		
		setQuizSubject(subject);
		
		setPreferredSize(new Dimension(550,500));
		

		//Live score update panel
		JPanel liveScorePane=new JPanel();
		JLabel lblScore = new JLabel("Score : 0",SwingConstants.LEFT);
		lblScore.setFont(new Font("Chalkboard SE", Font.BOLD, 25));
		lblScore.setForeground(new Color(34, 139, 34));
//		liveScorePane.add(lblScore,JComponent.LEFT_ALIGNMENT);
		
		//Subject panel
		subjectPane=new SubjectPanel(subject);
		
		//Question panel
		
		questionPane=new QuestionPanel(quizSubject, name);
		
		
		//for score update and topic selection
		JPanel rightPane=new JPanel();
		rightPane.setLayout(new BoxLayout(rightPane, BoxLayout.Y_AXIS));
		rightPane.add(liveScorePane,Component.LEFT_ALIGNMENT);
//		rightPane.add(subjectPane);
		
		//for questions and rightpane(score,topic)
		JPanel lowerPane=new JPanel();
		lowerPane.setLayout(new BoxLayout(lowerPane, BoxLayout.X_AXIS));
		lowerPane.add(questionPane);
		lowerPane.add(rightPane);
		

		//layout

		add(lowerPane);

		/**
		 * Listener for topic selection
		 */
		subjectPane.setSubjectListener(new SubjectListener() {
			
			@Override
			public void subjectChosen(String subject) {
				// update the subject chosen for quiz, and the quiz subject
				setQuizSubject(subject);
				questionPane.setQuizSubject(subject);
			}
		});
		
		/**
		 * Listener for live score updation
		 */
		questionPane.setScoreListener(new ScoreListener() {
			
			@Override
			public void scoreUpdated(int score) {
				// update new score to label
				lblScore.setText("Score : "+score);
				
			}
		});
		
		/**
		 * Listener for ending quiz
		 */
		questionPane.setSummaryListener(new SummaryListener() {
			
			@Override
			public void quizEnded(ScoreSummary summary) {
				// pass score summary to summary pane and display summary+graph
				
				//Summary panel
				summaryPane=new SummaryPanel(summary, name);
				add(summaryPane);
				
				//hide questionPane
				lowerPane.setVisible(false);
			}
		});
		
		

	}
	
	
	
}
