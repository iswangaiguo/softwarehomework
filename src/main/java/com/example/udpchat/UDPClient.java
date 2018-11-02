package com.example.udpchat;

import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

public class UDPClient implements ActionListener, Runnable {

	private JButton sendMessage;
	private JButton exit;
	private JButton setParam;
	private JTextArea chatArea;
	private JTextArea inputArea;
	private JTextField localPortJtf;
	private JTextField remotePortJtf;
	private JTextField remoteIPJtf;
	private JTextField localNameJtf;
	private DatagramSocket datagramSendSocket;
	private InetAddress destination;

	public static void main(String[] args) {
		UDPClient udpClient = new UDPClient();
		udpClient.init();
	}

	private void init() {
		JFrame jFrame = new JFrame("chatClient");
		jFrame.setTitle("DUP聊天室");
		jFrame.setSize(820, 350);
		jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		jFrame.setIconImage(Toolkit.getDefaultToolkit().createImage(this.getClass().getResource("/icon.jpg")));
		jFrame.setLayout(null);
		chatArea = new JTextArea();
		chatArea.setEditable(false);
		chatArea.setLineWrap(true);
		JScrollPane jspChat = new JScrollPane(chatArea);
		jspChat.setBounds(0, 0, 380, 300);
		jFrame.add(jspChat);
		JPanel jparam = new JPanel();
		jparam.setLayout(new GridLayout(2, 5));
		jparam.setBounds(400, 20, 380, 40);
		JLabel localPoatLabel = new JLabel("本地端口");
		localPortJtf = new JTextField("9090");
		JLabel remotePoatLabel = new JLabel("远程端口");
		remotePortJtf = new JTextField("9191");
		JLabel remoteIPLabel = new JLabel("远程IP");
		remoteIPJtf = new JTextField("localhost");
		JLabel localNameLabel = new JLabel("本机名称");
		localNameJtf = new JTextField("ClientA");
		jparam.add(localPoatLabel);
		jparam.add(localPortJtf);
		jparam.add(remotePoatLabel);
		jparam.add(remotePortJtf);
		jparam.add(new Label());
		jparam.add(remoteIPLabel);
		jparam.add(remoteIPJtf);
		jparam.add(localNameLabel);
		jparam.add(localNameJtf);
		jFrame.add(jparam);
		setParam = new JButton("设置");
		setParam.addActionListener(this);
		JPanel setParamjp = new JPanel();
		setParamjp.add(setParam);
		setParamjp.setBounds(400, 80, 380, 30);
		jFrame.add(setParamjp);
		JLabel chatContent = new JLabel("发送内容:");
		chatContent.setBounds(400, 110, 380, 20);
		jFrame.add(chatContent);
		inputArea = new JTextArea();
		inputArea.setLineWrap(true);
		JScrollPane jspInput = new JScrollPane(inputArea);
		jspInput.setBounds(400, 130, 380, 120);
		jFrame.add(jspInput);
		JPanel jb = new JPanel();
		sendMessage = new JButton("发送消息");
		sendMessage.addActionListener(this);
		exit = new JButton("退出窗体");
		exit.addActionListener(this);
		jb.add(sendMessage);
		jb.add(exit);
		jb.setBounds(400, 260, 400, 30);
		jFrame.add(jb);
		jFrame.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == sendMessage) {
			sendMessage();
		} else if (e.getSource() == setParam) {
			int localPort = Integer.parseInt(localPortJtf.getText().trim());
			try {
				datagramSendSocket = new DatagramSocket(localPort);
				Thread receive = new Thread(UDPClient.this);
				receive.start();
			} catch (SocketException e1) {
				e1.printStackTrace();
			}
		} else {
			System.exit(0);
		}
	}

	private void sendMessage() {
		if (inputArea.getText().length() == 0) {
			return;
		}
		String remoteIP = remoteIPJtf.getText().trim();
		int remotePort = Integer.parseInt(remotePortJtf.getText().trim());
		String data = inputArea.getText().trim();
		String fmtData = formatData(data);
		try {
			destination = InetAddress.getByName(remoteIP);
			DatagramPacket datagramPacket = new DatagramPacket(fmtData.getBytes(), fmtData.getBytes().length,
					destination, remotePort);
			String preContent = chatArea.getText().trim();
			if (preContent.length() == 0) {
				chatArea.setText(fmtData);
			} else {
				chatArea.append("\n" + fmtData);
			}
			inputArea.setText("");
			datagramSendSocket.send(datagramPacket);
		} catch (UnknownHostException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	private String formatData(String data) {
		String name = localNameJtf.getText().trim();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
		String now = dateFormat.format(new Date());
		String content = name + "\t" + now + "\n" + "    " + data;
		return content;
	}

	@Override
	public void run() {
		while (true) {
			byte[] buf = new byte[1024];
			DatagramPacket datagramPacket = new DatagramPacket(buf, 0, buf.length);
			try {
				datagramSendSocket.receive(datagramPacket);
			} catch (IOException e) {
				e.printStackTrace();
			}
			String data = new String(datagramPacket.getData(), 0, datagramPacket.getLength());
			String preContent = chatArea.getText().trim();
			if (preContent.length() == 0) {
				chatArea.setText(data);
			} else {
				chatArea.append("\n" + data);
			}
		}
	}
//
//	public static void main(String[] args) {
//		UDPClient udpClient = new UDPClient();
//		Thread sendMessageThread = new Thread(udpClient); 
//		sendMessageThread.start();
//		DatagramSocket datagramSocket = null;
//		try {
//			datagramSocket = new DatagramSocket(8080);
//			byte[] buf = new byte[1024];
//			DatagramPacket datagramPacket = new DatagramPacket(buf, 0, buf.length);
//			datagramSocket.receive(datagramPacket);
//			String data = new String(datagramPacket.getData(), 0, datagramPacket.getLength());
//			System.out.println(data);
//		} catch (SocketException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		datagramSocket.close();
//		
//	}
//
//	@Override
//	public void run() {
//		String data = "你好";
//		DatagramSocket datagramSocket = null;
//		try {
//			datagramSocket = new DatagramSocket(8081);
//			InetAddress destination = Inet4Address.getByName("localhost");
//			DatagramPacket datagramPacket = new DatagramPacket(data.getBytes(), data.getBytes().length, destination, 8080);
//			datagramSocket.send(datagramPacket);
//
//		} catch (SocketException e) {
//			e.printStackTrace();
//		} catch (UnknownHostException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		datagramSocket.close();
//	}

}
