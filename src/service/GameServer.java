/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Set;
import javax.swing.*;
import javax.swing.border.LineBorder;

/**
 *
 * @author zzl
 */
public class GameServer extends MainFrm implements Runnable {

	private final static int PORT = 4000;
	private ServerSocket server;
	private int personNum;
	private String[] strAry;
	private String recMessage;

	private Container container = null;
	private JButton sendBtn;
	private JTextArea recMessageArea;
	private JScrollPane recMessagePanel;
	private JTextField inputText;

	private Font font22 = new Font("����", Font.BOLD, 22);
	private Font font17 = new Font("����", Font.BOLD, 17);

	public GameServer() {
		
		try {
			server = new ServerSocket(PORT);
		} catch (IOException ex) {
			recMessageArea.append(ex.getMessage());
		}
		
		this.setSize(700, 400);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		this.setLocationRelativeTo(this);
		container = this.getContentPane();
		container.setLayout(null);
		container.setBackground(new Color(153, 204, 255));

		recMessageArea = new JTextArea();
		recMessageArea.setForeground(new Color(204, 204, 204));
		recMessageArea.setFont(font22);
		recMessageArea.setLineWrap(true);
		recMessageArea.setEditable(false);
		recMessageArea.setWrapStyleWord(true);
		recMessageArea.setBackground(new Color(0, 102, 204));

		recMessagePanel = new JScrollPane(recMessageArea);
		recMessagePanel.setSize(700, 330);
		container.add(recMessagePanel);

		inputText = new JTextField();
		inputText.setBounds(10, 336, 580, 30);
		inputText.setFont(font17);
		inputText.setBorder(new LineBorder(new Color(0, 102, 204), 2));
		container.add(inputText);

		sendBtn = new JButton("�� ��");
		sendBtn.setBounds(595, 336, 95, 30);
		sendBtn.setFont(font17);
		container.add(sendBtn);
		sendBtn.addMouseListener(new sendBtnLis());

		this.setVisible(true);
		
	}

	@Override
	public void run() {

		while (true) {
			try {
				recMessageArea.append("waiting......." + "\n");
				Socket socket = server.accept();

				BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream(), "GBK"));
				PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

				String message = in.readLine();

				String[] messageAry = message.split(":");
				
				boolean bool = OperateMySQL.selectExist(messageAry[0]);
				if (bool) {
					
					out.println("loginsuccess");
					out.flush();
					
					ReceiveMessageThread rmt = new ReceiveMessageThread();
					SendMessageThread smt = new SendMessageThread();

					rmt.setSocket(socket);
					rmt.setIn(in);
					smt.setSocket(socket);
					smt.setOut(out);

					ArrayList<String> list = OperateMySQL.userInfor(message);

					UserInfor user = new UserInfor();
					user.setUserName(message);
					user.setNickname(list.get(1));
					user.setSocket(socket);
					user.setOut(out);
					user.setIp(list.get(2));
					user.setRmt(rmt);
					user.setSmt(smt);
					rmt.setname(message);
					smt.setname(message);

					UserData.USER_MAP.put(message, user);

					personNum = UserData.USER_MAP.size();

					strAry = new String[personNum];
					Set<String> set = UserData.USER_MAP.keySet();
					strAry = set.toArray(strAry);

					recMessageArea.append("<" + message + ">�û���½�ɹ�,��������" + personNum + "\n");
					recMessageArea.append("�����û���Ϣ��" + UserData.USER_MAP.toString() + "\n");

					rmt.start();
					smt.start();
				}else {
					out.println("loginfail");
					out.flush();
					out.close();
				}
			} catch (IOException ex) {
				recMessageArea.append(ex.getMessage());
			}
		}
	}

	class ReceiveMessageThread extends Thread {

		private Socket socket;
		private String name;
		BufferedReader in;

		public void setSocket(Socket socket) {
			this.socket = socket;
		}

		public void setIn(BufferedReader in) {
			this.in = in;
		}

		public void setname(String name) {
			this.name = name;
		}

		@Override
		public synchronized void run() {
			while (!interrupted()) {
				try {
					recMessage = in.readLine();
					recMessageArea.append("��<" + this.name + ">���ܵ���Ϣ�ǣ�" + recMessage + "\n");
					if (recMessage != null) {

						if (recMessage.startsWith("close")) {
							String s = in.readLine();
							UserData.USER_MAP.get(s).getSmt().interrupt();
							UserData.USER_MAP.get(s).getRmt().interrupt();
							UserData.USER_MAP.remove(s);
							personNum = UserData.USER_MAP.size();
							strAry = new String[personNum];
							Set<String> set = UserData.USER_MAP.keySet();
							strAry = set.toArray(strAry);

						} else if ("0:false".equals(recMessage) || "1:false".equals(recMessage) || "2:false".equals(recMessage) || "2:true".equals(recMessage) || "1:true".equals(recMessage) || "0:true".equals(recMessage) || "-100:false".equals(recMessage)) {
							String[] strtemp = recMessage.split(":");
							UserData.USER_MAP.get(name).setPosition(strtemp[0]);
							UserData.USER_MAP.get(name).setIfReady(strtemp[1]);
						}
					}

					//System.out.println(UserData.USER_MAP);
				} catch (Exception ex) {
					recMessageArea.append(ex.getMessage());
				}
			}
		}
	}

	class SendMessageThread extends Thread {

		Socket socket;
		String name;
		PrintWriter out;
		String[] sendStrAry = new String[4];

		public void setSocket(Socket socket) {
			this.socket = socket;
		}

		public void setOut(PrintWriter out) {
			this.out = out;
		}

		public void setname(String name) {
			this.name = name;
		}

		@Override
		public synchronized void run() {
			while (!interrupted()) {
				try {
//					
					for (int i = 0; i < strAry.length; i++) {
						if (!name.equals(strAry[i]) && strAry[i] != name) {
							if (UserData.USER_MAP.get(strAry[i]) != null) {
								if (sendStrAry[i] == null) {
									sendStrAry[i] = "";
								}
								if (!sendStrAry[i].equals(UserData.USER_MAP.get(strAry[i]).toString())) {
									if (!"-100:-100:-100:-100:-100".equals(UserData.USER_MAP.get(strAry[i]).toString())) {
										out.println(UserData.USER_MAP.get(strAry[i]).toString());
										out.flush();
										recMessageArea.append("��<" + this.name + ">���͵���Ϣ�ǣ�" + UserData.USER_MAP.get(strAry[i]).toString() + "\n");
										sendStrAry[i] = UserData.USER_MAP.get(strAry[i]).toString();
									}
								}
							}
						}
					}

				} catch (Exception ex) {
					recMessageArea.append(ex.getMessage());
				}
			}
		}
	}

	class sendBtnLis extends MouseAdapter {

		@Override
		public void mouseClicked(MouseEvent e) {
			String str = inputText.getText();
			recMessageArea.append(str + "\n");
			inputText.setText("");
			for (int i = 0; i < UserData.USER_MAP.size(); i++) {
				UserData.USER_MAP.get(strAry[i]).getOut().println("Server:" + str);
				UserData.USER_MAP.get(strAry[i]).getOut().flush();
			}

		}

	}
}
