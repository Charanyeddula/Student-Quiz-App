package cyeddula.project.quiz.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class HomePageUI extends JFrame{
    private JPanel MainPanel;
    private JLabel NameLabel;
    private JTextField tfName;
    private JButton NextButton;
    private JLabel HeadingLabel;
    private JPanel fieldsPanel;

    public HomePageUI() {
        super("Welcome Page");

        createUIComponents();
        HeadingLabel.setFont(new java.awt.Font("Chalkboard SE", 1, 42));
        HeadingLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        HeadingLabel.setVerticalAlignment(SwingConstants.TOP);

        HeadingLabel.setAlignmentX(Component.LEFT_ALIGNMENT);


        NameLabel.setFont(new Font("Chalkboard SE", 1, 20));
        NameLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        NameLabel.setVerticalAlignment(SwingConstants.BOTTOM);
        NextButton.setText("Next");


        // Adding the components to the Panel
        MainPanel.add(HeadingLabel);

        fieldsPanel.setLayout(new GridLayout(1, 2));
        fieldsPanel.add(NameLabel);
        fieldsPanel.add(tfName);

        MainPanel.add(fieldsPanel);

        MainPanel.add(NextButton);


        setContentPane(MainPanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setVisible(true);
        setSize(500, 500);
        setLocationRelativeTo(null);

        NextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = tfName.getText();

                FileOutputStream out = null;
                FileInputStream in= null;
                try {
                    out = new FileOutputStream("Name.txt");
                    ObjectOutputStream oos = new ObjectOutputStream(out);
                    oos.writeObject(name);
                    oos.flush();
                    oos.close();
                    out.close();

                    in = new FileInputStream("Name.txt");
                    ObjectInputStream ois = new ObjectInputStream(in);
                    String name1 = (String) ois.readObject();
                }
                catch (Exception e1) {
                    e1.printStackTrace();
                }
                dispose();
                new TopQuizFrame(name);
            }
        });
    }



    private void createUIComponents() {
        // TODO: place custom component creation code here
        MainPanel = new JPanel();
        NameLabel = new JLabel("Enter Name");
        tfName = new JTextField();
        NextButton = new JButton();
        HeadingLabel = new JLabel(": Top Quiz :");
        fieldsPanel = new JPanel();
    }
}
