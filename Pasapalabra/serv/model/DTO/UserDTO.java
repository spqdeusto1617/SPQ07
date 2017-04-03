package model.DTO;

import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.util.Date;

public class UserDTO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String userName;

	private String mail;

	private BufferedImage profileImage;

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
		return gamesWon;
	}

	public void setGamesWon(int gamesWon) {
		this.gamesWon = gamesWon;
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

	public UserDTO(String userName, String mail, BufferedImage profileImage, Date dOB, int games,int gamesWon, int gamesLost) {
		super();
		this.userName = userName;
		this.mail = mail;
		this.profileImage = profileImage;
		DOB = dOB;
		this.games = games;
		this.gamesWon = gamesWon;
		this.gamesLost = gamesLost;
	}

	

}
