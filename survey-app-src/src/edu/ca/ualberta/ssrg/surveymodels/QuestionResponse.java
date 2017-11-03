package edu.ca.ualberta.ssrg.surveymodels;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

/**
 *  Question response Value Object
 */
public class QuestionResponse {

	@SerializedName("qid")
	private int questionIndex;

	private String response;

	@SerializedName("totalTime")
	private long totalElapsedTime;

	@SerializedName("timeString")
	private String totalElapsedTimeString;

	public QuestionResponse(int questionIndex, String response, long totalElapsedTime) {
		this.questionIndex = questionIndex;
		this.response = response;
		this.totalElapsedTime = totalElapsedTime;
		this.totalElapsedTimeString = TimeUtils.getTimeAsString(this.totalElapsedTime);
	}

	public int getQuestionIndex() {
		return questionIndex;
	}

	public String getResponse() {
		return response;
	}

	public long getTotalElapsedTime() {
		return totalElapsedTime;
	}

	public String getTotalElapsedTimeString() {
		return totalElapsedTimeString;
	}

	public String getResponseAsJson() {
		return new Gson().toJson(this);
	}
}
