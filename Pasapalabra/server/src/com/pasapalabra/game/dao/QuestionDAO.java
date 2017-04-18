package com.pasapalabra.game.dao;

import com.pasapalabra.game.model.Question;

/** Defines all database Question related needed methods.
 * @author Guti
 *
 */
public abstract class QuestionDAO {

	/** Gets a random question given a letter.
	 * @param letter Letter to search with.
	 * @return Question that has that letter.
	 */
	public abstract Question getRandomQuestionByLeter(char letter);
	
}
