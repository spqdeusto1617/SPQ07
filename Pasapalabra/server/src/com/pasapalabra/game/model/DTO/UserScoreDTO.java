package com.pasapalabra.game.model.DTO;

import java.io.Serializable;

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
