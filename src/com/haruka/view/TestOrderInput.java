package com.haruka.view;

import javax.swing.JFrame;

import com.haruka.dto.impl.MemoryImpl;
import com.haruka.sora.ArtificialIntelligence;
import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JLabel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JPanel;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class TestOrderInput extends JFrame {
	private ArtificialIntelligence sora ;
	private JTextField txtEdtxtinputorder;
	private final JButton btn_sendOrder = new JButton("   确认   ");
	public TestOrderInput(ArtificialIntelligence sora){
		this.sora = sora ;
		getContentPane().setLayout(new BorderLayout(0, 0));
		
		txtEdtxtinputorder = new JTextField();
		txtEdtxtinputorder.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
					if(e.getKeyCode() == KeyEvent.VK_ENTER)	{
						btn_sendOrder.doClick();
					}
					
			}
		});
		getContentPane().add(txtEdtxtinputorder, BorderLayout.CENTER);
		txtEdtxtinputorder.setColumns(10);
		
		JLabel label = new JLabel(" ");
		getContentPane().add(label, BorderLayout.NORTH);
		
		JLabel label_1 = new JLabel(" ");
		getContentPane().add(label_1, BorderLayout.SOUTH);
		
		JPanel panel = new JPanel();
		getContentPane().add(panel, BorderLayout.EAST);
		panel.setLayout(new BorderLayout(0, 0));
		btn_sendOrder.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String order = txtEdtxtinputorder.getText() ;
				sora.getEar().setListContext(order);
				txtEdtxtinputorder.setText("");
				sora.listenCompleted();
			}
		});
		panel.add(btn_sendOrder, BorderLayout.CENTER);
		
		JLabel label_2 = new JLabel(" ");
		panel.add(label_2, BorderLayout.WEST);
		
		JLabel label_3 = new JLabel(" ");
		panel.add(label_3, BorderLayout.EAST);
		
		JLabel label_4 = new JLabel(" ");
		getContentPane().add(label_4, BorderLayout.WEST);
		this.setBounds(0, 0, 500, 159);
		this.setBackground(Color.white);
	}
	
	public static void main(String[] args) {

		TestOrderInput input = new TestOrderInput(null) ;
		input.setVisible(true);
	}
	
}
