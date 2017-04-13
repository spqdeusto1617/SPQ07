package com.pasapalabra.game.model;

import java.io.Serializable;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Field;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Index;
import org.mongodb.morphia.annotations.Indexes;

/**Clase de utilidad para poder gestionar las preguntas de los jugadores
 * @author Ivan
 *
 */
@Entity("question")
@Indexes(
    @Index(value = "leter", fields = @Field("leter"))
)
public class Question implements Serializable{
	/**
	 * 
	 */
	
	private static final long serialVersionUID = 1L;
	
	@Id
    private ObjectId id;
	
	String question;
	
	String answer;
	
	
	String questionType;
	
	char leter;
	
	String creator;
	
	boolean answered = false;
	

	
	public Question(String question, String answer, char leter, String creator) {
		super();
		this.question = question;
		this.answer = answer;
		this.leter = leter;
		this.creator = creator;
	}
	public String getQuestion() {
		return question;
	}
	public void setQuestion(String question) {
		this.question = question;
	}
	public String getAnswer() {
		return answer;
	}
	public void setAnswer(String answer) {
		this.answer = answer;
	}
	public char getLeter() {
		return leter;
	}
	public void setLeter(char leter) {
		this.leter = leter;
	}
	
	public String getQuestionType() {
		return questionType;
	}
	public void setQuestionType(String questionType) {
		this.questionType = questionType;
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
	
	@Override
	public String toString() {
		return "Questions [question=" + question + ", answer=" + answer + ", leter=" + leter + ", creator=" + creator
				+ ", answered=" + answered + "]";
	}
	
}