package com.example.udpchat;

import java.awt.Font;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Structure;
import com.sun.jna.win32.StdCallLibrary;

public class ChangeTime implements ActionListener{
	
	private JTextField jtHour;
	private JTextField jtMinute;
	private JTextField jtSecond;
	private JButton jbConfirm;
	private JButton jbCancel;

	public interface Kernel32 extends StdCallLibrary {
		public static class SYSTEMTIME extends Structure {
			public short wYear;  
			public short wMonth;   
			public short wDayOfWeek;   
			public short wDay;    
			public short wHour;   
			public short wMinute;     
			public short wSecond;   
			public short wMilliseconds;
			
			@Override
			protected List<String> getFieldOrder() {
				List<String> list = new ArrayList<>();
				list.add("wYear");
				list.add("wMonth");
				list.add("wDayOfWeek");
				list.add("wDay");
				list.add("wHour");
				list.add("wMinute");
				list.add("wSecond");
				list.add("wMilliseconds");
				return list;
			}
		}
		void SetLocalTime (SYSTEMTIME result);
		void GetLocalTime (SYSTEMTIME result);
	}
	
	public static void main(String[] args) {
		ChangeTime changeTime = new ChangeTime();
		changeTime.init();
		
	}

	private void init() {
		JFrame jFrame = new JFrame("更改时间");
		jFrame.setSize(600, 400);
		jFrame.setLocation(600, 200);
		jFrame.setLayout(null);
		jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		JPanel jpTitle = new JPanel();
		JLabel jLabelTitle = new JLabel("修改系统事件");
		jLabelTitle.setFont(new Font("Dialog", 1, 40));
		jpTitle.add(jLabelTitle);
		jpTitle.setBounds(0,0,600,100);
		JPanel jpField = new JPanel();
		jpField.setBounds(0,100,600,100);
		jtHour = new JTextField(10);
		jtMinute = new JTextField(10);
		jtSecond = new JTextField(10);
		jpField.add(jtHour);
		jpField.add(new JLabel("时"));
		jpField.add(jtMinute);
		jpField.add(new JLabel("分"));
		jpField.add(jtSecond);
		jpField.add(new JLabel("秒"));
		JPanel jpButton = new JPanel();
		jbConfirm = new JButton("确定");
		jbConfirm.addActionListener(this);
		jbCancel = new JButton("取消");
		jbCancel.addActionListener(this);
		jpButton.add(jbConfirm);
		jpButton.add(jbCancel);
		jpButton.setBounds(0, 200, 600, 150);
		jFrame.add(jpTitle);
		jFrame.add(jpField);
		jFrame.add(jpButton);
		jFrame.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == jbConfirm) {
			short hour = Short.parseShort(jtHour.getText().trim());
			short minute = Short.parseShort(jtMinute.getText().trim());
			short second = Short.parseShort(jtSecond.getText().trim());
			Kernel32 lib = (Kernel32)Native.loadLibrary("kernel32",Kernel32.class);
			Kernel32.SYSTEMTIME time = new Kernel32.SYSTEMTIME();
			lib.GetLocalTime(time);
			time.wHour = hour;
			time.wMinute = minute;
			time.wSecond = second;
			lib.SetLocalTime(time);
		} else {
			jtHour.setText("");
			jtMinute.setText("");
			jtSecond.setText("");
		}
	}
}
