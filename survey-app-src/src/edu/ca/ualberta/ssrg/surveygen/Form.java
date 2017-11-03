package edu.ca.ualberta.ssrg.surveygen;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Event;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.BorderFactory;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import org.apache.commons.lang3.time.StopWatch;
import edu.ca.ualberta.ssrg.surveydata.DataRecorder;
import edu.ca.ualberta.ssrg.surveymodels.SurveySession;
import edu.ca.ualberta.ssrg.surveyvis.AnswerSpace;
import edu.ca.ualberta.ssrg.surveyvis.MainWindow;
import edu.ca.ualberta.ssrg.surveyvis.PictureFrame;
import edu.ca.ualberta.ssrg.surveyvis.Textbox;
import edu.ca.ualberta.ssrg.surveyvis.Toast;
import edu.ca.ualberta.ssrg.surveyvis.CloseCallback;

/**
 * This class is responsible for the form the user can see. It contains
 * all the components such as the image, body of the question, responses etc.
 */
public class Form {

	private SurveySession session;
	private Survey survey;
	private ArrayList<Question> formQuestions;
	private Question currentQuestion;
	private int questionIndex = 0;
	private ArrayList<String> response;
	private DataRecorder storage;
	private StopWatch stopWatch;

	// Frame components
	private MainWindow main;
	private Textbox questionBody;
	private PictureFrame picFrame;
	private JScrollPane bodyPane, ansPane;
	private JButton nextButton;
	private AnswerSpace ansSpace;

	public Form(Survey survey) {
		this.survey = survey;

		formQuestions = survey.getQuestionList();
		currentQuestion = formQuestions.get(0);
		this.main = new MainWindow(survey.getSurveyTitle(), new CloseCallback() {
			public void onClose() {
				storage.endSession();
			}

			public boolean shouldPromptOnClose() {
				return session != null;
			}
		});
		this.storage = new DataRecorder();
		this.stopWatch = new StopWatch();
	}

	public void launch() {
		// Set up layout
		FlowLayout frameLayout = new FlowLayout();
		main.setLayout(frameLayout);

		loadLoginForm();

		main.show();
	}

	// Starts the stop watch
	private void startStopWatch() {
		this.stopWatch.reset();
		this.stopWatch.start();
	}

	// Pauses the stop watch, and disable the answer space
	private void pauseStopWatch() {
		this.stopWatch.suspend();
		this.ansSpace.disableEdit();
	}

	// Resumes the stop watch, and enable the answer space
	private void resumeStopWatch() {
		this.stopWatch.resume();
		this.ansSpace.enableEdit();
	}

	// Stops the stop watch, and returns the recorded time
	private long stopStopWatch() {
		this.stopWatch.stop();

		long timeElapsed = this.stopWatch.getTime();

		return timeElapsed;
	}

	private void loadLoginForm() {
		final JPanel loginPanel = new JPanel();
		loginPanel.setLayout(new GridLayout(8, 1));

		JLabel label = new JLabel("User ID:");

		final JTextField idText = new JTextField("", 20);
		idText.setPreferredSize(new Dimension(10, 30));

		final JButton startButton = new JButton("Start Survey");
		startButton.setPreferredSize(new Dimension(30, 40));

		startButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String sessionId = idText.getText();

				session = new SurveySession(sessionId, formQuestions.size(), new Date());
				storage.startSession(session);

				// Set up first question
				loadNextQuestion();
			}
		});

		startButton.setEnabled(false);

		idText.getDocument().addDocumentListener(new DocumentListener() {
			  public void changedUpdate(DocumentEvent e) {
				  enableStartButton();
			  }
			  public void removeUpdate(DocumentEvent e) {
				  enableStartButton();
			  }
			  public void insertUpdate(DocumentEvent e) {
				  enableStartButton();
			  }

			  public void enableStartButton() {
			     if (idText.getText().length() > 0){
			    	 startButton.setEnabled(true);
			     } else {
			    	 startButton.setEnabled(false);
			     }
			  }
			});

		JLabel empty = new JLabel("");
		loginPanel.add(empty);
		loginPanel.add(empty);
		loginPanel.add(empty);
		loginPanel.add(empty);
		loginPanel.add(empty);
		loginPanel.add(label);
		loginPanel.add(idText);
		loginPanel.add(startButton);

		label.setAlignmentX(Component.LEFT_ALIGNMENT);
		idText.setAlignmentX(Component.LEFT_ALIGNMENT);
		startButton.setAlignmentX(Component.LEFT_ALIGNMENT);

		main.add(loginPanel);
		main.validate();
		main.repaint();
	}


	// Loads the next question to the form
	private void loadNextQuestion() {
		main.getContentPane().removeAll();

		// Add question body
		String questionTitle = "Question "
				+ (questionIndex + 1)
				+ " of "
				+ formQuestions.size();
		this.questionBody = new Textbox(currentQuestion.getBody(), false);
		this.questionBody.setFont(this.questionBody.getFont().deriveFont(20f));
		bodyPane = scrollGen(questionBody);
		this.questionBody.setOpaque(false);
		bodyPane.setOpaque(false);
		bodyPane.getViewport().setOpaque(false);
		bodyPane.setBorder (BorderFactory.createTitledBorder (
				BorderFactory.createEtchedBorder(),
				questionTitle,
                TitledBorder.LEFT,
                TitledBorder.TOP));
		main.add(bodyPane);

		// Add image
		this.picFrame = new PictureFrame(currentQuestion.getImage());
		main.add(picFrame);

		// Add response space
		String answerSpaceTitle = currentQuestion.getTypeDescription();
		switch (currentQuestion.getType()) {
		case "LA":
			this.ansSpace = new AnswerSpace();
			break;
		default:
			this.ansSpace = new AnswerSpace(currentQuestion.getType(), currentQuestion.getPossibleAnswers());
			break;
		}
		ansPane = scrollGen(ansSpace);
		ansPane.setOpaque(false);
		ansPane.getViewport().setOpaque(false);
		ansPane.setBorder (BorderFactory.createTitledBorder (
				BorderFactory.createEtchedBorder(),
				answerSpaceTitle,
                TitledBorder.LEFT,
                TitledBorder.TOP));
		ansPane.addMouseListener(new MouseAdapter() {
		     @Override
		     public void mouseClicked(MouseEvent mouseEvent) {
		    	 ansSpace.editLongAnswer();
		     }});

		main.add(ansPane);

		// Add next/finish button
		nextButton = new JButton("Next");
		addNextButtonFunctionality(nextButton);
		main.add(nextButton);

		// Update button text to finish on last question
		if (questionIndex == formQuestions.size() - 1) {
			nextButton.setText("Finish");
		}

		// Size components
		sizeCompnonents(currentQuestion);

		setPauseKeyBindings();

		// Refresh
		main.validate();
		main.repaint();

		startStopWatch();
	}

	private void addNextButtonFunctionality(JButton button) {
		// Action listener for next button
		button.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if(ansSpace.isAnswered(currentQuestion.getType())) {
					if (survey.isNextConfirmationEnabled()) {
						int promptResult = displayNextConfirmation();

						if (promptResult != JOptionPane.YES_OPTION) {
							return;
						}
					}

					questionIndex++;
					// Grab the selected solution and store it.
					response = ansSpace.getResponse(currentQuestion.getType());
					long elapsedTime = stopStopWatch();
					storage.saveAns(response, questionIndex, elapsedTime);
					// Move to next question or close
					if (questionIndex < formQuestions.size()) {
						currentQuestion = formQuestions.get(questionIndex);
						loadNextQuestion();
					} else {
						nextButton.setEnabled(false);

						String message = " Thank-you for your participation! ";
						int xpos = ansSpace.getX() + (ansSpace.getWidth()/2) - 100;
						int ypos = ansSpace.getY() + (ansSpace.getHeight()/2);
						Point p = new Point(xpos, ypos);
						final int toastDelayMs = 2000;
						Toast.showToast(ansSpace, message, p, toastDelayMs);

						new Thread(new Runnable(){
			                @Override
			                public void run(){
			                    try {
			                        Thread.sleep(toastDelayMs);
			                    }
													catch (InterruptedException ex) {
														// Todo: log the exception
			                    }
			                    finally{
			                    	main.closeWindow();
			                    }
			                }
			            }).start();
					}
				} else {
					// Question was not answered. Toast user.
					String message = " This is not a valid answer.";
					int xpos = ansSpace.getX() + (ansSpace.getWidth()/2) - 100;
					int ypos = ansSpace.getY() + (ansSpace.getHeight()/2);
					Point p = new Point(xpos, ypos);
					Toast.showToast(ansSpace, message, p, 2000);
				}
			}
		});
	}

	// Determines the size needed for each question component
	private void sizeCompnonents(Question q) {
		int preferredWidth = MainWindow.WIDTH - MainWindow.BORDER;

		if (q.getImage() != null) {
			picFrame.setPreferredSize(new Dimension(preferredWidth, MainWindow.IMAGE_HEIGHT));
			bodyPane.setPreferredSize(new Dimension(preferredWidth, (MainWindow.HEIGHT)/7));
			ansPane.setPreferredSize(new Dimension(preferredWidth, (MainWindow.HEIGHT)/5));
		} else {
			picFrame.setPreferredSize(new Dimension(preferredWidth, 0));
			bodyPane.setPreferredSize(new Dimension(preferredWidth, (MainWindow.HEIGHT)/3));
			ansPane.setPreferredSize(new Dimension(preferredWidth, (MainWindow.HEIGHT)/2));
		}

		int totalQuestionHeight = 0;
		totalQuestionHeight += picFrame.getPreferredSize().getHeight();
		totalQuestionHeight += bodyPane.getPreferredSize().getHeight();
		totalQuestionHeight += ansPane.getPreferredSize().getHeight();

		int actionPaneHeight = MainWindow.HEIGHT - totalQuestionHeight - 60;

		nextButton.setPreferredSize(new Dimension(preferredWidth-10, actionPaneHeight));
	}

	// Creates the scrollpanes for the components
	private JScrollPane scrollGen(JComponent component) {
		JScrollPane scroll = new JScrollPane (component);
	    scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
	    scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		return scroll;

	}

	private int displayNextConfirmation() {
		String objButtons[] = {"Yes","No"};

		String dialogText = "Are you sure you want to go to next question? You cannot go back.";

		int promptResult = JOptionPane.showOptionDialog(
				null, // use default frame
				dialogText,
				"Go To Next Question",
				JOptionPane.DEFAULT_OPTION,
				JOptionPane.WARNING_MESSAGE,
				null, // use default icon
				objButtons,
				objButtons[0]); // defaulted button selection - yes

		return promptResult;
	}

	/*
	 * On Ctrl + P, pauses/resumes the survey.
	 */
	private void setPauseKeyBindings() {
        InputMap inputMap = questionBody.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = questionBody.getActionMap();

        String pauseKeyStrokeName = "Pause/Resume";

        KeyStroke ctrlP = KeyStroke.getKeyStroke(KeyEvent.VK_P, Event.CTRL_MASK);
        inputMap.put(ctrlP, pauseKeyStrokeName);

        Action pauseAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	if(stopWatch.isSuspended()) {
					resumeStopWatch();
					main.setTitle(survey.getSurveyTitle());
					nextButton.setEnabled(true);
				} else {
					pauseStopWatch();
					main.setTitle(survey.getSurveyTitle() + " - Paused");
					nextButton.setEnabled(false);
				}
            }
        };
        actionMap.put(pauseKeyStrokeName, pauseAction);
	}
}
