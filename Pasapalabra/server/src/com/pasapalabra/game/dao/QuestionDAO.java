package com.pasapalabra.game.dao;

import com.pasapalabra.game.model.Question;

public abstract class QuestionDAO {

	public abstract Question getRandomQuestionByLeter(char letter);
	
}
