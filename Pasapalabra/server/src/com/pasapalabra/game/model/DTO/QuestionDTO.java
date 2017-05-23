package com.pasapalabra.game.model.DTO;

import java.io.Serializable;

/**
 * Class for sending the question to the user
 * @author ivan
 * @param: question: the question
 * @param: leter: the letter of the question
 * @param: answered: if the question has been answered or not
 * @param: creator: the creator of the question
 */
public class QuestionDTO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	String question;

	char leter;
	
	boolean answered;

	String creator;

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public char getLeter() {
		return leter;
	}

	public void setLeter(char leter) {
		this.leter = leter;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public boolean isAnswered() {
		return answered;
	}

	public void setAnswered(boolean answered) {
		this.answered = answered;
	}

	public QuestionDTO(String question, char leter, String creator ) {
		super();
		this.answered = false;
		this.question = question;
		this.leter = leter;
		this.creator = creator;
	}

	public QuestionDTO(boolean answered,String question, char leter, String creator ) {
		super();
		this.answered = answered;
		this.question = question;
		this.leter = leter;
		this.creator = creator;
	}
	@Override
	public String toString() {
		return "QuestionDTO [question=" + question + ", leter=" + leter + ", creator=" + creator + "]";
	}

	public boolean equals(QuestionDTO question) {
		return this.question.equals(question.getQuestion());
	}
	
	@Override
	public int hashCode() {
		return question.hashCode();
	}
}
