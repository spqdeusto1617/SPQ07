package com.pasapalabra.game.model;

import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.util.Date;

public class User implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String userName;
	
	private String mail;
	
	private String pass;
	
	private BufferedImage profileImage;
	
	private Date DOB;
	
	private int GamesWon;
	
	private int GamesLost;
	
	
	
	@Override
	public String toString() {
		return "User [userName=" + userName + ", mail=" + mail + ", pass=" + pass + ", "
				+ "DOB=" + DOB + ", GamesWon=" + GamesWon + ", GamesLost=" + 
				GamesLost + ", Games=" + Games + "]";
	}



	public User(String userName, String mail, String pass, BufferedImage profileImage, Date dOB, int gamesWon,
			int gamesLost, int games) {
		super();
		this.userName = userName;
		this.mail = mail;
		this.pass = pass;
		this.profileImage = profileImage;
		DOB = dOB;
		GamesWon = gamesWon;
		GamesLost = gamesLost;
		Games = games;
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



	public String getPass() {
		return pass;
	}



	public void setPass(String pass) {
		this.pass = pass;
	}



	public BufferedImage getProfileImage() {
		return profileImage;
	}



	public void setProfileImage(BufferedImage profileImage) {
		this.profileImage = profileImage;
	}



	public Date getDOB() {
		return DOB;
	}



	public void setDOB(Date dOB) {
		DOB = dOB;
	}



	public int getGamesWon() {
		return GamesWon;
	}



	public void setGamesWon(int gamesWon) {
		GamesWon = gamesWon;
	}



	public int getGamesLost() {
		return GamesLost;
	}



	public void setGamesLost(int gamesLost) {
		GamesLost = gamesLost;
	}



	public int getGames() {
		return Games;
	}



	public void setGames(int games) {
		Games = games;
	}



	private  int Games;
	
	
}
