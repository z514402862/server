/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;
import javax.swing.ImageIcon;
import service.GameServer.*;

/**
 *
 * @author zzl
 */
public class UserInfor {
	
	private String userName = "-100";
	private String nickname = "-100";
	private ImageIcon headPortiait;
	private Socket socket;
	private BufferedReader in;
	private PrintWriter out;
	private String ip = "-100";
	private String position = "-100";
	private ReceiveMessageThread rmt;
	private SendMessageThread smt;
	private String ifReady="-100";

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}
	
	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}
	

	public BufferedReader getIn() {
		return in;
	}

	public void setIn(BufferedReader in) {
		this.in = in;
	}

	public PrintWriter getOut() {
		return out;
	}

	public void setOut(PrintWriter out) {
		this.out = out;
	}
	
	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}
	
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public ImageIcon getHeadProtiait() {
		return headPortiait;
	}

	public void setHeadProtiait(ImageIcon headPortiait) {
		this.headPortiait = headPortiait;
	}

	public ReceiveMessageThread getRmt() {
		return rmt;
	}

	public void setRmt(ReceiveMessageThread rmt) {
		this.rmt = rmt;
	}

	public SendMessageThread getSmt() {
		return smt;
	}

	public void setSmt(SendMessageThread smt) {
		this.smt = smt;
	}

	public String getIfReady() {
		return ifReady;
	}

	public void setIfReady(String ifReady) {
		this.ifReady = ifReady;
	}
	

	@Override
	public String toString() {
		return this.userName+":"+this.nickname+":"+this.ip+":"+this.position+":"+this.ifReady;
	}
	
	
}
