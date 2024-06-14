
package cyeddula.project.quiz.gui;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class Main {


	public static void main(String[] args) {
		//runs the application as the event dispatch thread
		
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				try {//look and feel made cross platform
				    UIManager.setLookAndFeel( UIManager.getCrossPlatformLookAndFeelClassName() );
				 } catch (Exception e) {
				            e.printStackTrace();
				 }
				
				new HomePageUI();//call the main frame
			}
		});

	}

}
