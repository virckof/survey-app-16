package edu.ca.ualberta.ssrg.surveygen;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * Long answer option (polymorph Question type)
 */
public class LongAnswerQuestion extends Question{

	public LongAnswerQuestion(String title, String body, BufferedImage image, String type) {
		super(title, body, image, type);
	}

	@Override
	public ArrayList<String> getPossibleAnswers() {
		return null;
	}

}
