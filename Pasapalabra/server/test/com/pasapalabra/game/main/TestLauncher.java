package com.pasapalabra.game.main;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.pasapalabra.game.dao.mongodb.QuestionDAOTest;
import com.pasapalabra.game.dao.mongodb.UserDAOTest;
import com.pasapalabra.game.db.MongoTest;
import com.pasapalabra.game.service.ServiceBasicTest;

@RunWith(Suite.class)
@SuiteClasses({
	MongoTest.class,
	QuestionDAOTest.class, UserDAOTest.class,
	ServiceBasicTest.class
        })
public class TestLauncher { }