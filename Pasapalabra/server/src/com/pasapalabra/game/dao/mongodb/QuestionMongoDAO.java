package com.pasapalabra.game.dao.mongodb;

import java.util.List;
import java.util.Random;

import com.pasapalabra.game.dao.QuestionDAO;
import com.pasapalabra.game.model.Question;
import com.pasapalabra.game.server.Server;

public class QuestionMongoDAO extends QuestionDAO {

	@Override
	public Question getRandomQuestionByLeter(char letter) {
		List<Question> lQuestion = Server.mongoConnection.datastore.createQuery(Question.class)
				.field("letter").equal(letter).asList();
		
		int size = lQuestion.size();
		Random rand = new Random();
		
		return lQuestion.get(rand.nextInt(size));
	}

}
