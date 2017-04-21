package com.pasapalabra.game.dao.mongodb;

import java.util.List;
import java.util.Random;

import com.pasapalabra.game.dao.QuestionDAO;
import com.pasapalabra.game.model.Question;

public class QuestionMongoDAO extends QuestionDAO {

	private MongoConnection connection;

	public QuestionMongoDAO(MongoConnection mongoConnection) {
		this.connection = mongoConnection;
	}


	@Override
	public Question getRandomQuestionByLeter(char letter) {
		List<Question> lQuestion = this.connection.datastore.createQuery(Question.class)
				.field("letter").equal(letter).asList();

		int size = lQuestion.size();
		Random rand = new Random();

		return lQuestion.get(rand.nextInt(size));
	}

}
