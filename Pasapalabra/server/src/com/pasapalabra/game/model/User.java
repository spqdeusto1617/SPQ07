package com.pasapalabra.game.model;

import java.awt.image.BufferedImage;
import java.util.Date;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Field;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Index;
import org.mongodb.morphia.annotations.IndexOptions;
import org.mongodb.morphia.annotations.Indexes;

import com.pasapalabra.game.model.DTO.UserDTO;

/**
 * Class for managing the user´s information
 * @author ivan
 * @param: userName: the username of the user
 * @param: mail: the mail of the user
 * @param: password: the password of the user
 * @param: profileImage:the profile image of an user
 * @param: DOB: the date of birth of an user
 * @param: gamesWon: the amount of games won by the user
 * @param: gamesLost: the amount of games lost by the user
 * @param: games: the amount of games participated by the user (gamesWon + gamesLost)
 */
@Entity("user")
@Indexes({
	@Index(fields = @Field("userName"), options = @IndexOptions(name="uname", unique=true)),
	@Index(fields = @Field("userName"), options = @IndexOptions(name="uname", unique=true))
})
public class User{

	@Id
	private ObjectId id;

	private String userName;

	private String mail;

	private String pass;

	private BufferedImage profileImage;

	private Date DOB;

	private int gamesWon;

	private int gamesLost;


	private User(){super();}

	public User(String userName, String mail, String pass, BufferedImage profileImage, Date dOB, int gamesWon,
			int gamesLost, int gamesPerticipated) {
		super();
		this.userName = userName;
		this.mail = mail;
		this.pass = pass;
		this.profileImage = profileImage;
		DOB = dOB;
		this.gamesWon = gamesWon;
		this.gamesLost = gamesLost;

	}

	public User(ObjectId oid, String userName, String mail, String pass, BufferedImage profileImage, Date dOB, int gamesWon,
			int gamesLost) {
		super();
		this.id = oid;
		this.userName = userName;
		this.mail = mail;
		this.pass = pass;
		this.profileImage = profileImage;
		DOB = dOB;
		this.gamesWon = gamesWon;
		this.gamesLost = gamesLost;

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
		return gamesWon;
	}



	public void setGamesWon(int gamesWon) {
		this.gamesWon = gamesWon;
	}



	public int getGamesLost() {
		return gamesLost;
	}

	public int getGamesParticipated() {
		return this.gamesLost + this.gamesWon;
	}


	public void setGamesLost(int gamesLost) {
		this.gamesLost = gamesLost;
	}


	@Override
	public String toString() {
		return "User [userName=" + userName + ", mail=" + mail + ", pass=" + pass + ", "
				+ "DOB=" + DOB + ", GamesWon=" + gamesWon + ", GamesLost=" + 
				gamesLost + "]";
	}
	
	public boolean equals(User user) {
		return this.userName.equals(user.getUserName());
	}
	
	@Override
	public int hashCode() {
		return userName.hashCode();
	}
}
