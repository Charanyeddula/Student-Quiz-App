/**
 * 
 */
package cyeddula.project.quiz.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class SummaryPanel extends JPanel{
	
	//member controls
	private JLabel lblTotalScore;
	private JLabel lblAttempted;
	private JLabel lblCorrect;
	private JLabel lblThank;
	private JLabel lblInfo;
	
	private JButton btnPlayAgain;
	private JPanel playAgainPane;
	
	//score statistics collection
	private Map<String,Double> statistics=new HashMap<String,Double>();//<SUBJECT,SCORE_PERCENT>
	
	
	//Constructor
	/**
	 * Creates score summary panel
	 * @param summary
	 */
	public SummaryPanel(ScoreSummary summary, String name)
	{
		statistics=summary.getStatistics();
		
		
		//setPreferredSize(new Dimension(700,700));
		setLayout(new BorderLayout());
		
		lblThank=new JLabel(name + ", Thank You for taking the test.",SwingConstants.CENTER);
		lblTotalScore=new JLabel("Your total score:"+summary.getTotalScore());
		lblAttempted=new JLabel("Total questions attempted:"+summary.getTotalQuestions());
		lblCorrect=new JLabel("Total correct answers:"+summary.getCorrectAnswers());
		lblInfo=new JLabel();
		
		lblThank.setFont(new Font("Calibri", Font.BOLD, 25));
		lblThank.setForeground(new Color(0, 0, 139));
		lblTotalScore.setFont(new Font("Calibri", Font.BOLD, 20));
		lblTotalScore.setForeground(new Color(0, 128, 0));
		lblAttempted.setFont(new Font("Chalkboard se", Font.BOLD, 20));
		lblAttempted.setForeground(new Color(0, 128, 0));
		lblCorrect.setFont(new Font("Calibri", Font.BOLD, 20));
		lblCorrect.setForeground(new Color(0, 128, 0));
		lblInfo.setFont(new Font("Calibri", Font.BOLD, 15));
		lblInfo.setForeground(new Color(0, 0, 139));
		
		//set info text
		//bar chart is created only if the user has a non-zero score
		if(summary.getTotalScore()==0)
		{
//			lblInfo.setText("<html>Your score is too low.<br/></html>");
			lblInfo.setForeground(Color.RED);
		}
		else{
			lblInfo.setText("<html>View your performance in <u>each of the topics you scored</u> below.</html>");
			lblInfo.setForeground(new Color(0, 0, 139));
		}
		
		//create score pane
		
		JPanel finalScorePane = new JPanel();
		finalScorePane.setLayout(new BoxLayout(finalScorePane, BoxLayout.Y_AXIS));
		finalScorePane.setPreferredSize(new Dimension(500,200));
		
		
		finalScorePane.add(lblThank);
		finalScorePane.add(lblTotalScore);
		finalScorePane.add(lblAttempted);
		finalScorePane.add(lblCorrect);
		
		finalScorePane.add(lblInfo);
		
		//graph pane
		JPanel graphPane=new JPanel();
		graphPane.setLayout(new BorderLayout());
		graphPane.setAlignmentX(CENTER_ALIGNMENT);
		graphPane.setPreferredSize(new Dimension(600,350));
		
		
		
		BarChart chart = new BarChart("Your Performance Chart");
		
		chart.setAlignmentX(CENTER_ALIGNMENT);
		//color for bar is chosen as a random color from a preferred list. Repeating colors are considered ok.
		Color[] colors={Color.darkGray,Color.MAGENTA,Color.orange,Color.blue,Color.pink,Color.yellow};
		for(Entry<String, Double> topicScore:statistics.entrySet())
		{
			Random rand = new Random();
			int i = rand.nextInt(6);
			chart.addBar(colors[i], topicScore.getKey()+":"+String.format("%.2f", topicScore.getValue()));//format used is subject:percentScore
			
		}
		
		graphPane.add(chart,BorderLayout.CENTER);//add chart to pane
		
		//add to layout
		
		add(finalScorePane,BorderLayout.NORTH);
		add(graphPane,BorderLayout.CENTER);
		
		
		//play again option
		playAgainPane=new JPanel();
		playAgainPane.setAlignmentX(Component.CENTER_ALIGNMENT);
		btnPlayAgain=new JButton("Replay");
		btnPlayAgain.setIcon(new ImageIcon("./Resources/LayoutImages/routeReplay.png"));
		btnPlayAgain.setVerticalTextPosition(SwingConstants.BOTTOM);
		btnPlayAgain.setHorizontalTextPosition(SwingConstants.CENTER);
		btnPlayAgain.setBorderPainted(false);
		btnPlayAgain.setContentAreaFilled(false);
		btnPlayAgain.setFont(new Font("Calibri", Font.BOLD, 15));
		btnPlayAgain.setForeground(new Color(0, 0, 139));
		btnPlayAgain.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// start over again
				if (e.getSource() == btnPlayAgain) {

				    Frame[] fr=JFrame.getFrames();
				    for(Frame f:fr)
				    {
				    	f.dispose();
				    }
				    new TopQuizFrame(name);
				}
				
			}
		});
		playAgainPane.add(btnPlayAgain);
		//show Try again button
    	playAgainPane.setVisible(true);
    	add(playAgainPane,BorderLayout.SOUTH);//add to layout
    	
		
	}
	

}


class BarChart extends JPanel
{
	BarChart(String title)
	{
		this.title=title;
	}
	private String title;
	private Map<Color, String> bars =new LinkedHashMap<Color, String>();
	
	/**
	 * Add new bar to chart
	 * @param color color to display bar
	 * @param value size of bar
	 */
	public void addBar(Color color, String value)
	{
		bars.put(color, value);
		repaint();
	}
	
	/**
	 * Override paintComponent to draw the performance chart
	 */
	@Override
	protected void paintComponent(Graphics g)
	{
		try{
		//System.out.println("bars="+bars);
		// determine longest bar
		
		double max = Double.MIN_VALUE;
		for (String value : bars.values())//value is of format: Subject:scorePercent
		{
			double score=Double.parseDouble(value.split(":")[1]);
			max = Math.max(max, score);
		}
		
		// paint bars
		
		int maxWidth=600;
		int maxHeight=300;
		
		//title and subject label styles
		Font titleFont = new Font("Calibri", Font.BOLD, 20);
	    FontMetrics titleFontMetrics = g.getFontMetrics(titleFont);
	    Font labelFont = new Font("Calibri", Font.BOLD, 15);
	    FontMetrics labelFontMetrics = g.getFontMetrics(labelFont);

	    int titleWidth = titleFontMetrics.stringWidth(title);
	    int q = titleFontMetrics.getAscent();
	    int p = (maxWidth - titleWidth) / 2;
	    g.setFont(titleFont);
	    //g.drawString(title, p, q);//draw title

		
		int width = (maxWidth / bars.size()) - 2;
		int x = 1;
		for (Color color : bars.keySet())
		{
			String subject=bars.get(color).split(":")[0];
			
			double value = Double.parseDouble(bars.get(color).split(":")[1]);
			
			//to handle 0 score condition
			int height=0;
			if(value==0)
				height=1;
			else
				height = (int)((maxHeight-q-20) * ((double)value / max));
			
			
			g.setColor(color);
			g.fillRect(x, maxHeight - height, width, height);
			g.setColor(Color.black);
			
			//System.out.println(maxHeight+" - "+height);
			//System.out.println("x="+x+" y="+(maxHeight - height)+" height="+height+" width="+width);
			
			g.drawRect(x, maxHeight - height, width, height);//draw bar
			
			g.setFont(labelFont);
		    g.drawString(subject+" - "+(value)+"%", x, maxHeight - height);//draw label
		      
		    x += (width + 5);//adjust x position
		}
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
		}
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(bars.size() * 10 + 2, 50);
	}

	
}
