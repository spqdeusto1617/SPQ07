package com.pasapalabra.game.model.DTO;

import java.io.Serializable;
import java.util.Date;

/**
 * Class for sending the userdata thought Internet
 * @author ivan
 * @param: userName: the username of the user
 * @param: mail: the mail of the user
 * @param: profileImage:the profile image of an user
 * @param: DOB: the date of birth of an user
 * @param: gamesWon: the amount of games won by the user
 * @param: gamesLost: the amount of games lost by the user
 * @param: games: the amount of games participated by the user (gamesWon + gamesLost)
 */
public class UserDTO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String userName;

	private String mail;

	private String profileImage;

	private Date DOB;

	private int gamesWon;

	private int gamesLost;
	
	private int games;
	
	public int getGames() {
		return games;
	}

	public void setGames(int games) {
		this.games = games;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public String getProfileImage() {
		return profileImage;
	}

	public void setProfileImage(String profileImage) {
		this.profileImage = profileImage;
	}

	public Date getDOB() {
		return DOB;
	}

	public void setDOB(Date dOB) {
		DOB = dOB;
	}

	public int getGamesWon() {
		return gamesWon;
	}

	public void setGamesWon(int gamesWon) {
		this.gamesWon = gamesWon;
	}

	public void updateGames() {
		this.games = this.gamesWon + this.gamesLost;
	}

	public int getGamesLost() {
		return gamesLost;
	}

	public void setGamesLost(int gamesLost) {
		this.gamesLost = gamesLost;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return "UserDTO [userName=" + userName + ", mail=" + mail + ", profileImage=" + profileImage + ", DOB=" + DOB
				+ ", GamesWon=" + gamesWon + ", GamesLost=" + gamesLost + "]";
	}

	public UserDTO(String userName, String mail, String profileImage, Date dOB,int gamesWon, int gamesLost) {
		super();
		this.userName = userName;
		this.mail = mail;
		this.profileImage = profileImage;
		DOB = dOB;
		this.games = gamesWon + gamesLost;
		this.gamesWon = gamesWon;
		this.gamesLost = gamesLost;
	}

	public UserDTO(String userName) {
		super();
		this.userName = userName;
		this.mail = "";
		this.profileImage = null;
		DOB = new Date();
		this.gamesWon = 0;
		this.gamesLost = 0;
		this.games = this.gamesWon + this.gamesLost;	
	}

	public boolean equals(UserDTO user) {
		return this.userName.equals(user.getUserName());
	}
	
	@Override
	public int hashCode() {
		return userName.hashCode();
	}
}
