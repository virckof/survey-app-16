package edu.ca.ualberta.ssrg.surveyvis;

import javax.swing.JTextArea;

/**
 * This class encapsulates athe text in all types of questions
 */
public class Textbox extends JTextArea{

	private static final long serialVersionUID = 1L;

	public Textbox(String content, boolean editable) {

		//TEXT AREA
		this.setText(content);

	    this.setLineWrap(true);
	    this.setWrapStyleWord(true);
	    this.setEditable(editable);
	    this.setVisible(true);

	}

	public void updateText(String newText) {
		this.setText(newText);

	}

}
