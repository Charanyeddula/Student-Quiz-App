/**
 * 
 */
package cyeddula.project.quiz.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public abstract class QuestionBank {
	

	public QuestionBank(String fileName)
	{		
		//load data from file and set questionList
		getAllQuestions(fileName);
	}
	
	//data members
	private ArrayList<Question> questionList=new ArrayList<Question>();
	public final String FILE_PATH="./Resources/Data/";//folder path where input files are located.
	public final String IMAGE_PATH="./Resources/Images/";//folder path where image files are located.
	
	private int questionsAttempted=0;
	private int correctAnswers=0;
	
	

	//getters and setters
	public ArrayList<Question> getQuestionList() {
		return questionList;
	}

	public void setQuestionList(ArrayList<Question> questionList) {
		this.questionList = questionList;
	}
	
	
	public int getQuestionsAttempted() {
		return questionsAttempted;
	}

	public void incrementQuestionsAttempted() {
		++this.questionsAttempted;
	}

	public int getCorrectAnswers() {
		return correctAnswers;
	}

	public void incrementCorrectAnswers() {
		++this.correctAnswers;
	}
	
	
	
	//methods
	public void getAllQuestions(String fileName)
	{
		ArrayList<Question> questions=new ArrayList<Question>();
		BufferedReader reader;
		Question newQuestion;
		int i=0;
		
		//Check if file exists in the given path. Print error message if the file is missing.
		File file=new File(FILE_PATH+fileName);
		if(file.exists() && !file.isDirectory())
		{
			try 
			{
				//read data from file line by line
				reader=new BufferedReader(new FileReader(file));
				String line;
				while((line=reader.readLine())!=null)
				{
					//validate format of each line
					//records with invalid format are discarded
					//Expected format of each record:
					//<questiontype>question<F>file name(if question is image type)</F><O>colon separated options</O><A>answer</A>
					++i;
					newQuestion=new Question();
					newQuestion.setQuestionID(i);
					
					//identify question type
					QuestionType qType=getQuestionType(line.substring(0, 4));
					
					//set question text
					if(qType== QuestionType.MULTIPLECHOICE)
					{
						newQuestion.setQuestionType(QuestionType.MULTIPLECHOICE);
						newQuestion.setQuestionText(line.substring(4, line.indexOf("<O>")));
					}
					if(qType==QuestionType.IMAGEQUESTION)
					{
						newQuestion.setQuestionType(QuestionType.IMAGEQUESTION);
						newQuestion.setQuestionText(line.substring(4, line.indexOf("<F>")));
						newQuestion.setQuestionImage(IMAGE_PATH+line.substring(line.indexOf("<F>")+3,line.indexOf("</F>")));//set image path
						//System.out.println("imagepath="+newQuestion.getQuestionImage());
					}
					if(qType==QuestionType.IMAGEANSWER)
					{
						newQuestion.setQuestionType(QuestionType.IMAGEANSWER);
						newQuestion.setQuestionText(line.substring(4, line.indexOf("<O>")));
					}
					
					
					//split the string enclosed in <O></O> and set options
					String optString=line.substring(line.indexOf("<O>")+3, line.indexOf("</O>"));
					List<String> options=new ArrayList<String>();
					for(String o : optString.split(":"))
					{
						options.add(o);
					}
					newQuestion.setOptions(options);
					newQuestion.setAnswer(line.substring(line.indexOf("<A>")+3, line.indexOf("</A>")));
					questions.add(newQuestion);
					
						
				}
				
				
			}
			catch (IOException e) 
			{
				System.out.println("Exception:"+e.getMessage());
			}
			catch(Exception e)
			{
				System.out.println("Exception:"+e.getMessage());
			}
			finally{
				//set questionList to new list of questions
				setQuestionList(questions);
				
			}
	}
	else
		System.out.println("Error: File does not exist. Please provide a valid file path and file name.");

	}

	public abstract Question getRandomQuestion();

	public double getPercentageScore()
	{
		if(getQuestionsAttempted()==0)
			return 0;
		
		int total=getQuestionsAttempted()*5;
		double score=getCorrectAnswers()*5.0;
		double percent=(score/total)*100.0;
		
		//System.out.println(score+"/"+total);
		return percent;
	}

	private QuestionType getQuestionType(String questionType)
	{
		//System.out.println(questionType);
		if(questionType.equals("<MC>"))
			return QuestionType.MULTIPLECHOICE;
		else if(questionType.equals("<QI>"))
			return QuestionType.IMAGEQUESTION;
		else if(questionType.equals("<AI>"))
			return QuestionType.IMAGEANSWER;
		
		return null;
	}


}
