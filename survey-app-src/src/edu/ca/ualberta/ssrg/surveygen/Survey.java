package edu.ca.ualberta.ssrg.surveygen;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import javax.imageio.ImageIO;

/**
 * This class reads in the survey file and generates the appropriate questions.
 * It also contains the main method.
 */
public class Survey {

	private String surveyTitle;
	private ArrayList<Question> questionList;
	private String curDir = System.getProperty("user.dir");
	private String surDir = curDir + "/surveyfile/survey";
	private boolean nextConfirmationEnabled = false;

	public Survey() {
		// Create blank questions list
		questionList = new ArrayList<Question>();

		// Read the available survey file from the SurveyFile folder
		parseSurvey();
	}

	// Read the survey file and generate appropriate questions
	private void parseSurvey() {

		// Open the file
		FileInputStream fstream = null;
		try {
			fstream = new FileInputStream(surDir);

		} catch (FileNotFoundException e) {
			System.out.println("Could not find the survey file " + surDir + ".");
			System.exit(0);
		}

		BufferedReader br = new BufferedReader(new InputStreamReader(fstream));

		String strLine, header, content;
		String hasPic = null;
		String [] lineParts;
		ArrayList<String> questionContent = new ArrayList<String>();
		Question newQ = null;

		// Read File Line By Line
		try {
			while ((strLine = br.readLine()) != null)   {
				if (strLine.isEmpty()) {
					continue;
				}

				// Check if line is a question break
				if (strLine.equals("--")) {
					newQ = processNewQuestion(questionContent, hasPic);
					if (newQ != null) {
						questionList.add(newQ);
					}

					questionContent = new ArrayList<String>();
					hasPic = null;

				} else {
					lineParts = strLine.split(":");
					header = lineParts[0];
					content = lineParts[1];

					if (header.matches("^title\\.+\\d$")) {
						questionContent.add(0, content);

					} else if (header.matches("^body\\.+\\d$")) {
						questionContent.add(1, content);

					} else if (header.matches("^type\\.+\\d$")) {
						questionContent.add(2, content);

					} else if (header.matches("^picture\\.+\\d$")) {
						hasPic = content;

					} else if (header.matches("^option\\.+\\d$")) {
						questionContent.add(content);
					} else if (header.matches("survey title")) {
						surveyTitle = content;
					} else if (header.matches("next confirmation enabled")) {
						content = content.toLowerCase().trim();
						if (content.equals("yes") || content.equals("true")) {
							nextConfirmationEnabled = true;
						}
					} else {
						System.out.println("Unidentified line header in survey file!");
						br.close();
						System.exit(0);
					}

				}

			}

			// Close the input stream
			br.close();

		} catch (IOException e) {
			System.out.println("Err encountered while reading the survey file.");
			System.exit(0);
		}

	}

	// Takes the question's content from a file and creates the new question
	private Question processNewQuestion(ArrayList<String> content, String picture) {
		// Check the question has been populated
		if (content.size() == 0) { return null; }

		// Determine picture if present
		BufferedImage img = null;
		String imgDir = curDir + "/pictures/" + picture;
		if (picture != null) {
			try {
			    img = ImageIO.read(new File(imgDir));
			} catch (IOException e) {
				System.out.println("Could not find image " + picture + ".");
				System.exit(0);
			}
		}

		// Obtain question details
		String type = content.remove(2);
		String body = content.remove(1);
		String title = content.remove(0);

		// Determine question type and create new question
		Question newQ = null;
		switch (type) {
			case "MC":
				newQ = new MultipleChoiceQuestion(title, body, img, type, content);
				break;
			case "CB":
				newQ = new CheckBoxQuestion(title, body, img, type, content);
				break;
			case "LA":
				newQ = new LongAnswerQuestion(title, body, img, type);
				break;

		}

		return newQ;
	}

	public String getSurveyTitle() {
		return surveyTitle;
	}

	public ArrayList<Question> getQuestionList() {
		return questionList;
	}

	public boolean isNextConfirmationEnabled() {
		return nextConfirmationEnabled;
	}

	/*/////////////////////////////////////////
	MAIN
	/////////////////////////////////////////*/

	public static void main (String [] args) {

		Survey survey = new Survey();
		Form form = new Form(survey);
		form.launch();

	}

}
