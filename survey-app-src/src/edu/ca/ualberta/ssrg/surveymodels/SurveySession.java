package edu.ca.ualberta.ssrg.surveymodels;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

public class SurveySession {

	@SerializedName("sid")
	private String sessionId;
	
	private Date startTime;
	
	@SerializedName("responses")
	private ArrayList<QuestionResponse> questionResponses;
	
	private Date endTime;
	
	@SerializedName("totalTime")
	private long totalElapsedTime = 0;
	
	@SerializedName("timeString")
	private String totalElapsedTimeString;
	
	private int questionsAnswered = 0;
	
	private int questionTotal = 0;
	
	public SurveySession(String userId, int questionTotal, Date startTime) {
		this.sessionId = userId;
		this.questionTotal = questionTotal;
		
		this.startTime = startTime;
		
		this.questionResponses = new ArrayList<QuestionResponse>();
	}
	
	public String getSessionId() {
		return sessionId;
	}

	public Date getStartTime() {
		return startTime;
	}

	public ArrayList<QuestionResponse> getQuestionResponses() {
		return questionResponses;
	}

	public void addQuestionResponses(QuestionResponse questionResponse) {
		this.questionResponses.add(questionResponse);
		this.questionsAnswered++;
		this.totalElapsedTime += questionResponse.getTotalElapsedTime();
		this.totalElapsedTimeString = TimeUtils.getTimeAsString(this.totalElapsedTime);
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public long getTotalElapsedTime() {
		return totalElapsedTime;
	}

	public String getTotalElapsedTimeString() {
		return totalElapsedTimeString;
	}
	
	public int getQuestionsAnswered() {
		return questionsAnswered;
	}
	
	public int getQuestionTotal() {
		return questionTotal;
	}

	public String getSessionStartAsJson() {
		return "{\n"
				+ "\t\"sid\": \"" + this.getSessionId() +"\",\n"
				+ "\t\"startTime\":" + new Gson().toJson(this.getStartTime()) +",\n"
				+ "\t\"responses\": [";
	}
	
	public String getSessionEndAsJson() {
		return "\t],\n"
				+ "\t\"endTime\":" + new Gson().toJson(this.getEndTime()) +",\n"
				+ "\t\"totalTime\":" + this.getTotalElapsedTime() +",\n"
				+ "\t\"timeString\": \"" + this.getTotalElapsedTimeString() +"\",\n"
				+"\t\"questionsAnswered\":" + this.getQuestionsAnswered() +",\n"
				+ "\t\"questionTotal\":" + this.getQuestionTotal() +"\n"
				+ "}\n";
	}
}
