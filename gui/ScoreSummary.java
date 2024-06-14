/**
 * 
 */
package cyeddula.project.quiz.gui;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class ScoreSummary {
	
	//data members
	private int totalQuestions;
	private int correctAnswers;
	private Map<String,Double> statistics=new HashMap<String,Double>();
	
	public final int SCORE_VALUE=5;//each question carries 5 marks


	public String getName() {
		String name = "";
		try{
			FileInputStream io = new FileInputStream("Name.txt");
			ObjectInputStream ois = new ObjectInputStream(io);
			name = (String) ois.readObject();
		} catch (Exception e) {
			name = "Unknown";
		}
		return name;
	}


	public void saveScore(HashMap<String,Integer> statisticss)
	{
		String name = getName();
		int score = getTotalScore();

		try{
			//writing names to file
			File file = new File("Historyfile.txt");
			FileWriter writer = new FileWriter(file,true);
			writer.write(name+" "+statisticss.get("ENTERTAINMENT")+statisticss.get("SCIENCE")+" "+statisticss.get("GEOGRAPHY")+" "+statisticss.get("SPORTS")+"\n");
			writer.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	//getters and setters
	public int getTotalQuestions() {
		return totalQuestions;
	}
	public void setTotalQuestions(int totalQuestions) {
		this.totalQuestions = totalQuestions;
	}
	public int getCorrectAnswers() {
		return correctAnswers;
	}
	public void setCorrectAnswers(int correctAnswers) {
		this.correctAnswers = correctAnswers;
	}
	public Map<String, Double> getStatistics() {
		return statistics;
	}
	public void setStatistics(Map<String, Double> statistics) {
		this.statistics = statistics;
	}
	public int getTotalScore()
	{
		return correctAnswers*SCORE_VALUE;
	}

}
