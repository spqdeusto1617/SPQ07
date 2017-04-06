package com.pasapalabra.game.model;

public class UserScore {

	int rightAnswered;
	
	int wrongAnswered;
	
	char currentLetter;
	
	

	public UserScore() {
		super();
		this.rightAnswered = 0;
		this.wrongAnswered = 0;
		this.currentLetter = 'a';
	}

	public void setRightAnswered(int rightAnswered) {
		this.rightAnswered = rightAnswered;
	}

	public void setWrongAnswered(int wrongAnswered) {
		this.wrongAnswered = wrongAnswered;
	}

	public void setCurrentLetter(char currentLetter) {
		this.currentLetter = currentLetter;
	}

	public int getRightAnswered() {
		return rightAnswered;
	}

	public int getWrongAnswered() {
		return wrongAnswered;
	}

	public char getCurrentLetter() {
		return currentLetter;
	}

	@Override
	public String toString() {
		return "UserScore [rightAnswered=" + rightAnswered + ", wrongAnswered=" + wrongAnswered + ", currentLetter="
				+ currentLetter + "]";
	}
	
	public void nextLetter(){
		if(this.currentLetter == 'n'){
			this.currentLetter = 'ñ';
		}
		else if(this.currentLetter == 'ñ'){
			this.currentLetter = 'o';
		}
		else if(this.currentLetter == 'z'){
		this.currentLetter = 'a';	
		}
		else{
			this.currentLetter++;
		}
	}
	
	public void increaseRight(){
		this.rightAnswered++;
	}
	public void increaseWrong(){
		this.wrongAnswered++;
	}
	
}
