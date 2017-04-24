package com.pasapalabra.game.model.DTO;

import java.io.Serializable;

/**
 * Class to track the user score during a game
 * @author ivan
 * @param: rightAnswered: the amount of questions right answered
 * @param: wrongAnswered: the amount of questions wrong answered
 * @param: victory: a boolean that indicates if the user has won or not 
 */
public class UserScoreDTO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	int rightAnswered;

	int wrongAnswered;

	boolean victory;

	public int getRightAnswered() {
		return rightAnswered;
	}

	public void setRightAnswered(int rightAnswered) {
		this.rightAnswered = rightAnswered;
	}

	public int getWrongAnswered() {
		return wrongAnswered;
	}

	public void setWrongAnswered(int wrongAnswered) {
		this.wrongAnswered = wrongAnswered;
	}

	public UserScoreDTO(int rightAnswered, int wrongAnswered,boolean victory) {
		super();
		this.rightAnswered = rightAnswered;
		this.wrongAnswered = wrongAnswered;
		this.victory = victory;
	}

	public void won(){
		this.victory = true;
	}

	public void lost(){
		this.victory = false;
	}
	
}
