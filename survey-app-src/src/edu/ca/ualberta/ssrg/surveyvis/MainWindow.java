package edu.ca.ualberta.ssrg.surveyvis;

import java.awt.FlowLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * This class is the main frame of the application and serves as the main
 controller
 */
public class MainWindow extends JFrame{

	private static final long serialVersionUID = 1L;
	public static final int BORDER = 20;
	public static final int WIDTH = 600;
	public static final int HEIGHT = 640;
	public static final int IMAGE_HEIGHT = HEIGHT/2;

	private CloseCallback closeCallback;

	public MainWindow(String windowTitle, CloseCallback callback) {
		if (windowTitle == null || windowTitle.isEmpty()) {
			windowTitle = "Survey";
		}

		this.setSize(WIDTH, HEIGHT);
		this.setResizable(false);
		this.setTitle(windowTitle);
		this.setLayout(new FlowLayout(FlowLayout.LEFT));

		closeCallback = callback;

		addConfirmationOnWindowClose();
	}

	private void addConfirmationOnWindowClose() {
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new WindowAdapter() {
		    @Override
		    public void windowClosing(WindowEvent windowEvent)
		    {
		    	boolean closeWindow = true;

		        if (closeCallback.shouldPromptOnClose()) {
		        	int promptResult = displayCloseConfirmation();

		        	closeWindow = promptResult == JOptionPane.YES_OPTION;
		        }

		       if (closeWindow) {
		    	   closeWindow();
		       }
		    }
		});
	}

	private int displayCloseConfirmation() {
		String objButtons[] = {"Yes","No"};

		String dialogText = "<html>"
				+ "Are you sure you want to exit? Your previous answers will still be saved."
				+ "</html>";

		int promptResult = JOptionPane.showOptionDialog(
				null, // use default frame
				dialogText,
				"Exiting Survey",
				JOptionPane.DEFAULT_OPTION,
				JOptionPane.WARNING_MESSAGE,
				null, // use default icon
				objButtons,
				objButtons[1]); // defaulted button selection - no

		return promptResult;
	}

	// Calls the callback for wrap up and then closes the window
	public void closeWindow() {
		closeCallback.onClose();
		System.exit(0);
	}
}
