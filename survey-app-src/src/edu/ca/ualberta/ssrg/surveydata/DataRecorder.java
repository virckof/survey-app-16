package edu.ca.ualberta.ssrg.surveydata;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import edu.ca.ualberta.ssrg.surveymodels.QuestionResponse;
import edu.ca.ualberta.ssrg.surveymodels.SurveySession;

/*
 * Starts a session by storing the provided
 * session and writing to file the session info.
 */
public class DataRecorder {

	private String curDir = System.getProperty("user.dir");
	private String ansDir = curDir + "/surveyfile/responses";
	private BufferedWriter out = null;

	private SurveySession session;

	public DataRecorder() {
		// Create the file for storing the data if it does not already exist.
		try {
			FileWriter fstream = new FileWriter(ansDir, true);
			out = new BufferedWriter(fstream);
			if (out == null) {
				System.out.println("Could not open responses file");
				System.exit(0);
			}
		} catch (IOException e) {
			System.out.println("Err opening response file");
		}

	}

	/*
	 * Starts a session by storing the provided
	 * session and writing to file the session info.
	 */
	public void startSession(SurveySession session) {
		this.session = session;

		writeToFile(session.getSessionStartAsJson());
	}

	/*
	 * Ends a session by writing to the file the session info
	 * and then closing the file.
	 */
	public void endSession() {
		if (this.session != null) {
			session.setEndTime(new Date());

			writeToFile(session.getSessionEndAsJson());
		}

		closeFile();
	}

	/*
	 *  Save the provided answer to the file. The questionIndex provided will
	 *  be the question number (starting at 1). The "answer" variable is an
	 *  ArrayList containing all the selected items or the long answer text.
	 */
	public void saveAns(ArrayList<String> answer, int questionIndex, long elapsedTime) {
		String fullAns = "";
		if (answer.size() > 1) {
			// Store all items checked
			for (int i = 0; i < answer.size() - 1; i++) {
				fullAns = fullAns.concat(answer.get(i) + ", ");
			}
			fullAns = fullAns.concat(answer.get(answer.size() - 1));

		} else {
			// Store single answer
			if (answer.size() != 0) {
				fullAns = answer.get(0);
			}
		}

		if (session != null) {
			QuestionResponse response = new QuestionResponse(questionIndex, fullAns, elapsedTime);

			if (session.getQuestionResponses().size() > 0) {
				writeToFile(",");
			}

			session.addQuestionResponses(response);

			writeToFile("\n");
			writeToFile(response.getResponseAsJson());
		}
	}

	/*
	 * Writes and flushes a string to the output file.
	 */
	private void writeToFile(String str) {
		// Write to file
		try {
			out.write(str);
			out.flush();
		} catch (IOException e) {
			System.out.println("Err writting to responses file");
		}
	}

	// Close file
	private void closeFile() {
		if (out != null) {
			try {
				out.close();
			} catch (IOException e) {
				System.out.println("Err closing responses file");
			}
		}
	}

}
