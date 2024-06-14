/**
 * 
 */
package cyeddula.project.quiz.data;

import java.util.ArrayList;
import java.util.Random;


public class ScienceBank extends QuestionBank {
	
	public ScienceBank()
	{
		super("science.txt");
		//a uniqueQuestionList is maintained separately for each topic bank
		//to avoid question repetitions in the case of topic switching back and forth
		uniqueQuestionList=getQuestionList();
	}
	
	//uniqueQuestionList is used to avoid repetition of same question for a same subject
	private static ArrayList<Question> uniqueQuestionList=new ArrayList<Question>();
	

	@Override
	public Question getRandomQuestion() {
		//Get a random question corresponding to subject chosen.
		//No two questions chosen for the same subject should be the same.
		//choose a random index of the uniqueQuestionList
		
		if(uniqueQuestionList.size()==0)//attempted all questions in selected topic
			return null;
		
		
		int randomIndex=new Random().nextInt(uniqueQuestionList.size());
		Question newQuestion=uniqueQuestionList.get(randomIndex);
		//remove the selected question from uniqueQuestionList to avoid future repetitions
		uniqueQuestionList.remove(randomIndex);
		
		//return the question at random index chosen
		return newQuestion;
	}

}
