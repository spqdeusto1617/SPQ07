package com.pasapalabra.game.model.DTO;

import java.io.Serializable;

/**
 * Class to track the user score during a game
 * @author alvaro
 * @param: rightAnswered: the amount of questions right answered
 * @param: wrongAnswered: the amount of questions wrong answered
 */
public class UserScoreDTO implements Serializable{

	int rightAnswered;

	int wrongAnswered;

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

	public UserScoreDTO(int rightAnswered, int wrongAnswered) {
		super();
		this.rightAnswered = rightAnswered;
		this.wrongAnswered = wrongAnswered;
	}

	


}
