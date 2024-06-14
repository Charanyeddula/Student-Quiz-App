package cyeddula.project.quiz.data;

import java.util.ArrayList;
import java.util.Random;

public class SportsBank extends QuestionBank{

    private static ArrayList<Question> uniqueQuestionList=new ArrayList<Question>();
    public SportsBank()
    {
        super("sports.txt");
        //set uniqueQuestionList
        uniqueQuestionList=getQuestionList();
    }

    @Override
    public Question getRandomQuestion() {
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
