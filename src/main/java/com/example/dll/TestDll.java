package com.example.dll;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class TestDll implements ActionListener{

	private JTextField number1;
	private JTextField number2;
	private JButton equal;
	private JTextField result;

	public static void main(String[] args) {
		TestDll testDll = new TestDll();
		testDll.init();
	}

	private void init() {
		JFrame jFrame = new JFrame("调用AddDll程序");
		jFrame.setSize(400, 200);
		jFrame.setLocation(400, 200);
		jFrame.setLayout(null);
		JPanel jPanel = new JPanel();
		number1 = new JTextField();
		number1.setColumns(8);
		//number1.setPreferredSize(new Dimension(40, 30));
		JLabel add = new JLabel("+");
		number2 = new JTextField();
		number2.setColumns(8);
		//number2.setPreferredSize(new Dimension(40, 30));
		equal = new JButton("=");
		equal.addActionListener(this);
		result = new JTextField();
		result.setEnabled(false);
		result.setColumns(8);
		//result.setPreferredSize(new Dimension(40, 30));
		jPanel.add(number1);
		jPanel.add(add);
		jPanel.add(number2);
		jPanel.add(equal);
		jPanel.add(result);
		jPanel.setBounds(0, 60, 400, 100);;
		jFrame.add(jPanel);
		jFrame.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == equal) {
			int addNumber1 = Integer.parseInt(number1.getText());
			int addNumber2 = Integer.parseInt(number2.getText());
			int content = add(addNumber1, addNumber2);
			result.setText(String.valueOf(content));
		}
	}
	
	private int add(int a, int b) {
		return AddDll.addDll.add(a, b);
	}
	
	
	
	
}
