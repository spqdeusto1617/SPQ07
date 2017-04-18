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

	private  int gamesPerticipated;
	

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
		this.gamesPerticipated = gamesPerticipated;
	}
	
	public User(ObjectId oid, String userName, String mail, String pass, BufferedImage profileImage, Date dOB, int gamesWon,
			int gamesLost, int gamesPerticipated) {
		super();
		this.id = oid;
		this.userName = userName;
		this.mail = mail;
		this.pass = pass;
		this.profileImage = profileImage;
		DOB = dOB;
		this.gamesWon = gamesWon;
		this.gamesLost = gamesLost;
		this.gamesPerticipated = gamesPerticipated;
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



	public void setGamesLost(int gamesLost) {
		this.gamesLost = gamesLost;
	}



	public int getGames() {
		return gamesPerticipated;
	}



	public void setGames(int games) {
		gamesPerticipated = games;
	}

	@Override
	public String toString() {
		return "User [userName=" + userName + ", mail=" + mail + ", pass=" + pass + ", "
				+ "DOB=" + DOB + ", GamesWon=" + gamesWon + ", GamesLost=" + 
				gamesLost + ", Games=" + gamesPerticipated + "]";
	}


}
