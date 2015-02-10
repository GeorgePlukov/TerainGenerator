package com.GeorgePlukov.Main;

import java.util.Scanner;

import javax.swing.JFrame;


public class Frame extends JFrame{
	private final static String APPNAME = "Terrain Generator";
	
	public final static int WIDTH = 700;
	public final static int HEIGHT = 700;

	
	public static void main(String[] args){
		new Frame(APPNAME);
	}
	
	private Frame (final String appName){
		this.setTitle(appName);
		
		
		
		// create the map object
		Map m = new Map(40,40);
		m.toggleLakes();
		// create the map panel and specifications 
		MapPanel mp = new MapPanel(m);
		mp.setPreferredSize(WIDTH,HEIGHT);
		// add the panel to the screen
		this.setContentPane(mp);

		
		// pack her up
		this.pack();		
		// JFrame settings
		this.setResizable(false);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setVisible(true);
		
		Scanner scan = new Scanner(System.in);
		int count = 0;
		while (count < 70){
			System.out.println("current iterate: " + count);
			count++;
			//scan.nextLine();
			mp.update();
			try {
				Thread.sleep(300);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		
		}
		
		
	
	}
}
