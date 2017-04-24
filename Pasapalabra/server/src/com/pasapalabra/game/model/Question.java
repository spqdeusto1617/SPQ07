package com.pasapalabra.game.model;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Field;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Index;
import org.mongodb.morphia.annotations.IndexOptions;
import org.mongodb.morphia.annotations.Indexes;

import com.pasapalabra.game.model.DTO.UserDTO;

/**Utility class for tracing the user´s questions 
 * @author Ivan
 * @param: question: the question
 * @param: answer: the answer of the question
 * @param: questionType: the question type
 * @param: leter: the letter of the question
 * @param: answered: if the question has been answered or not
 * @param: creator: the creator of the question
 */
@Entity("question")
@Indexes(
    @Index(fields = @Field("letter"), options = @IndexOptions(name="letter"))
)
public class Question{
	/**
	 * 
	 */
	
	private static final long serialVersionUID = 1L;
	
	@Id
    private ObjectId id;
	
	String question;
	
	String answer;
	
	String questionType;
	
	char letter;
	
	String creator;
	
	boolean answered = false;
	
	private Question(){super();}
	
	public Question(ObjectId oid, String question, String answer, char leter, String creator) {
		super();
		this.id = oid;
		this.question = question;
		this.answer = answer;
		this.letter = leter;
		this.creator = creator;
	}
	
	public Question(String question, String answer, char leter, String creator) {
		super();
		this.question = question;
		this.answer = answer;
		this.letter = leter;
		this.creator = creator;
	}
	public ObjectId getId() {
		return id;
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
		return letter;
	}
	public void setLeter(char leter) {
		this.letter = leter;
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
		return "Questions [question=" + question + ", answer=" + answer + ", leter=" + letter + ", creator=" + creator
				+ ", answered=" + answered + "]";
	}
	
	public boolean equals(Question question) {
		return this.getQuestion().equals(question.getQuestion());
	}
	
	@Override
	public int hashCode() {
		return question.hashCode();
	}
	
}