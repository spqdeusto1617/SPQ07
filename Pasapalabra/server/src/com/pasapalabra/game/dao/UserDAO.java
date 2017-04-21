package com.pasapalabra.game.dao;

import com.pasapalabra.game.model.User;

/** Defines all database User related needed methods.
 * @author Guti
 *
 */
public abstract class UserDAO {

	/** Makes a login checkout.
	 * @param username
	 * @param password
	 * @return User if it gets found, null if not.
	 */
	public abstract User getUserByLogin(String username, String password);
	
	
	/** Check if user exists
	 * @param username Username to be checked.
	 * @return true if exists, false if not.
	 */
	public abstract boolean checkIfExists(String username);
	
	
	/** Updates score of a given user
	 * @param username User who's score must be updated.
	 * @param won true if won, false if not.
	 */
	public abstract void updateScore(User user, boolean won);
	
	
}
