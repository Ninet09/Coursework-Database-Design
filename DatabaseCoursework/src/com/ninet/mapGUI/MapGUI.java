package com.ninet.mapGUI;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.ninet.mapBuilder.MapBuilder;

public class MapGUI {
	
	private static final int SIZE_WIDTH = 1200;
	private static final int SIZE_HEIGHT = 700;
	private static final String TITLE = "BEIJING MAP";
	private String resultActual,resultNoActual;
	private JTextField startText = null,reachText = null;
	
	public class ResultGUI extends JFrame{

		public ResultGUI(String resultActual,String resultNoActual){
			super();
			setTitle(TITLE);
			setSize(SIZE_WIDTH,SIZE_HEIGHT);
			Font font = new Font("TimesRoman", Font.BOLD, 15);
			Container container = getContentPane();
			container.setLayout(new GridLayout(2,1,1,1));
			JPanel jPanel1 = new JPanel();
			JPanel jPanel2 = new JPanel();
			JLabel label1 = new JLabel("<html>" + startText.getText().toString() + " to " + reachText.getText().toString() + "<br>" + resultNoActual + "</html>",JLabel.CENTER);
			label1.setFont(font);		
			jPanel1.add(label1,JPanel.CENTER_ALIGNMENT);		
			JLabel label2 = new JLabel("<html>" + startText.getText().toString() + " to " + reachText.getText().toString() + "<br>" + resultActual + "</html>",JLabel.CENTER);
			label2.setFont(font);
			jPanel2.add(label2,JPanel.CENTER_ALIGNMENT);
			jPanel1.setBackground(new Color(250,255,245));
			jPanel2.setBackground(new Color(250,255,240));
			container.add(jPanel1);
			container.add(jPanel2);
			setVisible(true);
		}
	}
	
	private class WelcomGUI extends JFrame{

		public WelcomGUI() throws InterruptedException{
			super();
			setTitle(TITLE);
			setResizable(false); 
			setSize(SIZE_WIDTH,SIZE_HEIGHT);
			Container container = getContentPane();		
			JLabel label = new JLabel("BEI JING SUBWAY",JLabel.CENTER);
			Font font = new Font("TimesRoman", Font.BOLD, 60);
			label.setFont(font);
			container.add(label);
			setVisible(true);
			Thread.sleep(2000);
			setVisible(false);
			dispose();
		}		
	}
	
	private class SearchGUI extends JFrame implements ActionListener{
		
		private static final String HINT = "Route Search";
		private JPanel hintPanel = null,mapPanel = null,searchPanel = null;
		private JLabel hintLabel = null,mapLabel = null,startLabel = null,reachLabel = null;
		private JButton searchButton;

		public SearchGUI() throws ClassNotFoundException, SQLException{
			super();		
			setTitle(TITLE);
			setResizable(false);
			setSize(SIZE_WIDTH,SIZE_HEIGHT);
			setLayout(new BorderLayout());
			Container container = getContentPane();				
			Font font = new Font("TimesRoman", Font.BOLD, 20);
			Font font1 = new Font("TimesRoman", Font.BOLD, 50);
			Font font2 = new Font("TimesRoman", Font.BOLD, 18);
			
			hintPanel = new JPanel();
			hintLabel = new JLabel(HINT,JLabel.CENTER);
			hintLabel.setFont(font);
			hintPanel.add(hintLabel);			
			mapPanel = new JPanel();
			mapLabel =new JLabel("<html><br><br><br>Beijing Subway Search System</html>",JLabel.CENTER);
			mapLabel.setFont(font1);
			mapPanel.add(mapLabel);
		
			searchPanel = new JPanel();
			searchPanel.setLayout(new FlowLayout());
			startLabel = new JLabel("Start station: ", JLabel.CENTER);
			startLabel.setFont(font2);
			reachLabel = new JLabel("    Reach station: ", JLabel.CENTER);
			reachLabel.setFont(font2);
			startText = new JTextField(15);
			startText.setFont(font2);
			reachText = new JTextField(15);
			reachText.setFont(font2);
			searchButton = new JButton("SEARCH");
			searchButton.setContentAreaFilled(false);
			searchButton.addActionListener(this);
			
			searchPanel.add(startLabel);
			searchPanel.add(startText);
			searchPanel.add(reachLabel);
			searchPanel.add(reachText);
			searchPanel.add(searchButton);
			
			container.add(hintPanel,BorderLayout.NORTH);
			container.add(mapPanel,BorderLayout.CENTER);
			container.add(searchPanel,BorderLayout.SOUTH);
			
			setVisible(true);
		}

		@Override
		public void actionPerformed(ActionEvent arg0) {
			try {
				MapBuilder mapBuilder = new MapBuilder();
				resultActual = mapBuilder.show(startText.getText().toString(), reachText.getText().toString(),true);
				MapBuilder mapBuilder2 = new MapBuilder();
				resultNoActual = mapBuilder2.show(startText.getText().toString(), reachText.getText().toString(),false);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			ResultGUI resultGUI = new ResultGUI(resultActual,resultNoActual);
			resultGUI.addMouseListener(new MouseListener() {

				public void mouseReleased(MouseEvent arg0) {
				}
				public void mousePressed(MouseEvent arg0) {		
				}
				public void mouseExited(MouseEvent arg0) {
					resultGUI.setVisible(false);
					resultGUI.dispose();
				}
				public void mouseEntered(MouseEvent arg0) {
				}
				public void mouseClicked(MouseEvent arg0) {
					resultGUI.setVisible(false);
					resultGUI.dispose();
				}
			});
		}		
	}
	
	public MapGUI() throws InterruptedException, ClassNotFoundException, SQLException{
		new WelcomGUI();
		new SearchGUI();
	}
}
