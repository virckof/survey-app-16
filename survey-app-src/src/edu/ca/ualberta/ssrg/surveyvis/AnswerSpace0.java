package edu.ca.ualberta.ssrg.surveyvis;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;

public class AnswerSpace extends JPanel implements ActionListener{
	
	private ButtonGroup group;
	private List<JCheckBox> checkboxes;
	private JTextArea answer;
	
	private static Color DISABLED_GREY = new Color(230, 230, 230);
	
	// Used to construct multiple choice and check box questions
	public AnswerSpace(String questionType, ArrayList<String> answers) {
		this.setBackground(Color.WHITE);
		GridLayout layout = new GridLayout(answers.size(), 2);
		this.setLayout(layout);
		if (questionType.equals("MC")) {
			// Multiple choice question
			List<JRadioButton> radiobuttons = new ArrayList<JRadioButton>();
			group = new ButtonGroup();
			for (String ans : answers) {
				JRadioButton btn = new JRadioButton(ans);
				group.add(btn);
				radiobuttons.add(btn);
				btn.addActionListener(this);
				this.add(btn);
			}
			
		} else {
			// Check box question
			checkboxes = new ArrayList<JCheckBox>();
			for (String ans : answers) {
				JCheckBox box = new JCheckBox(ans);
			    checkboxes.add(box);
			    this.add(box);
			}
			
		}
		
	}
	
	// Used for constructing long answer questions
	public AnswerSpace() {
		FlowLayout layout = new FlowLayout();
		this.setLayout(layout);
		this.setBackground(Color.WHITE);
		answer = new JTextArea();
		answer.setSize(MainWindow.WIDTH - (2*MainWindow.BORDER), ((MainWindow.HEIGHT)/10) - (MainWindow.BORDER));
		answer.setText("");   
		answer.setLineWrap(true);
		answer.setWrapStyleWord(true);
		answer.setEditable(true);
		answer.setVisible(true);
		this.add(answer);
		
	}
	
	// Focusing/begin edit on long answer questions
	public void editLongAnswer() {
		if (answer != null) {
			answer.requestFocusInWindow();
		}
	}
	
	// Disable editing on the answer
	public void disableEdit() {
		if (answer != null) {
			this.setBackground(DISABLED_GREY);
			answer.setBackground(DISABLED_GREY);
			answer.setEditable(false);
		}
		
		if (group != null) {
			for (AbstractButton btn : Collections.list(group.getElements())) {
				btn.setEnabled(false);
			}
		}
		
		if (checkboxes != null) {
			for (JCheckBox box : checkboxes) {
			    box.setEnabled(false);
			}
		}
	}
	
	// Enable editing on the answer
	public void enableEdit() {
		if (answer != null) {
			this.setBackground(Color.WHITE);
			answer.setBackground(Color.WHITE);
			answer.setEditable(true);
		}
		
		if (group != null) {
			for (AbstractButton btn : Collections.list(group.getElements())) {
				btn.setEnabled(true);
			}
		}
		
		if (checkboxes != null) {
			for (JCheckBox box : checkboxes) {
			    box.setEnabled(true);
			}
		}
	}
	
	// Returns the user's response to the question
	public ArrayList<String> getResponse(String questionType) {
		ArrayList<String> answers = new ArrayList<String>();
		switch (questionType) {
		case "MC":
			for (Enumeration<AbstractButton> btnGroup = group.getElements(); btnGroup.hasMoreElements();) {
				AbstractButton btn = btnGroup.nextElement();
				
				if (btn.isSelected()) {
					answers.add(btn.getText());
				}
				
			}
			break;
		case "CB":
			for (JCheckBox check : checkboxes) {
				if (check.isSelected()) {
					answers.add(check.getText());
				}
			}
			break;
		default:
			answers.add(answer.getText());
			break;
		}
		
		return answers;
		
	}
	
	// Check if there is an answer available
	public boolean isAnswered(String questionType) {
		boolean answered = false;
		switch (questionType) {
		case "MC":
			for (Enumeration<AbstractButton> btnGroup = group.getElements(); btnGroup.hasMoreElements();) {
				AbstractButton btn = btnGroup.nextElement();
				
				if (btn.isSelected()) {
					answered = true; 
					break;
				}
				
			}
			break;
		case "CB":
			for (JCheckBox check : checkboxes) {
				if (check.isSelected()) {
					answered = true; 
					break;
				}
			}
			break;
		default:
			if (answer.getText().length() != 0) {
				answered = true;
			}
			break;
		}
		
		return answered;
	}

	// Action listener for radio buttons
	@Override
	public void actionPerformed(ActionEvent e) {
		
		
	}

}
