package cyeddula.project.quiz.gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;

public class ScorePanel extends JPanel {

    private JLabel scoreLabel;
    private int score;

    private JLabel ErrorLabel;
    private ArrayList<String[]> DictScore;

    public ScorePanel() {
        scoreLabel = null;
        score = 0;    }

//    public void setScore(int score) {
//        this.score = score;
//        scoreLabel.setText("Score: " + score);
//    }

    public ScorePanel(String Name) {
        loadScores(Name);

        setPreferredSize(new Dimension(600, 600));

        System.out.println(DictScore);
        JPanel ScorePanel = new JPanel();

        scoreLabel = new JLabel("Score: " + score);
        scoreLabel.setFont(new Font("Century Schoolbook", Font.BOLD, 25));
        JTable table = new JTable();

        table.setBounds(30, 40, 200, 300);
        table.setVisible(true);

        DefaultTableModel model = new DefaultTableModel();

        model.addColumn("Name", new Object[]{"Name"});
        model.addColumn("Geography", new Object[]{"Geography"});
        model.addColumn("Science", new Object[]{"Science"});
        model.addColumn("Entertainment", new Object[]{"Entertainment"});

        for (String[] row : DictScore) {
            model.addRow(row);
        }
        table.setModel(model);
        add(table);

        ScorePanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        JButton btnBack = new JButton("Back");
        btnBack.setIcon(new ImageIcon("src/cyeddula/project/quiz/gui/back.png"));
        btnBack.setFont(new Font("Century Schoolbook", Font.BOLD, 20));
        btnBack.setVerticalTextPosition(SwingConstants.BOTTOM);
        btnBack.setHorizontalTextPosition(SwingConstants.CENTER);
        btnBack.setBorderPainted(false);
        btnBack.setContentAreaFilled(false);
        btnBack.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == btnBack) {
                    Frame[] frames = JFrame.getFrames();
                    for (Frame frame : frames) {
                        frame.dispose();
                    }
                    new TopQuizFrame(Name);
                }
            }
        });

        ScorePanel.add(scoreLabel);
        ScorePanel.add(btnBack);
        add(ScorePanel);
    }

    public int getScore() {
        return score;
    }

    public void loadScores(String Name)
    {
        File f = new File("src/cyeddula/project/quiz/gui/Score.txt");
        try{
            BufferedReader reader = new BufferedReader(new FileReader(f));
            String line;
            DictScore = new ArrayList<>();
            while((line = reader.readLine()) != null)
            {
                String[] row = line.split(" ");
                if(row[0].equals(Name))
                {
                    String[] temparray = new String[4];
                    temparray[0] = row[0];
                    temparray[1] = row[1];
                    temparray[2] = row[2];
                    temparray[3] = row[3];
                    DictScore.add(temparray);
                }

                DictScore.add(row);
            }
            if(DictScore.size()==0)
            {
                ErrorLabel = new JLabel("Scores not found");
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
