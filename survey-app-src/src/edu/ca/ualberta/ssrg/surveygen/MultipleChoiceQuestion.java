package edu.ca.ualberta.ssrg.surveygen;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * Multiple choice question (polymorph Question type)
 */
public class MultipleChoiceQuestion extends Question{

	private ArrayList<String> possibleAnswers = new ArrayList<String>();

	public MultipleChoiceQuestion(String title, String body, BufferedImage image, String type, ArrayList<String> answers) {
		super(title, body, image, type);
		this.possibleAnswers = answers;
	}

	@Override
	public ArrayList<String> getPossibleAnswers() {
		return possibleAnswers;
	}

}
