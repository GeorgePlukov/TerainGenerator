package com.GeorgePlukov.Main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.Random;
import java.util.Scanner;

import javax.swing.JPanel;

public class MapPanel extends JPanel {
	private Map m;
	private long currentUpdateTime;
	private long previousUpdateTime = 0;

	private boolean firstTime = true;

	public MapPanel(Map m) {
		this.m = m;
	}

	@Override
	public void paintComponent(Graphics g) {
		// calculate time since last update
		previousUpdateTime = currentUpdateTime;
		currentUpdateTime = System.currentTimeMillis();
		// print fps
		System.out.println("FPS:" + (float) 1000
				/ (float) (currentUpdateTime - previousUpdateTime));

		// print the map
		Tile[][] map = m.getMap();
		int length = map[0].length;
		for (int row = 0; row < length; row++) {
			for (int col = 0; col < length; col++) {
				switch (map[row][col].getType()) {
				case SAND:
					g.setColor(new Color(194, 178, 128));
					break;
				case GRASS:
					g.setColor(new Color(1, 166, 17));
					break;
				case WATER:
					g.setColor(Color.BLUE);
					break;
				default:
					g.setColor(Color.BLACK);
					break;
				}
				g.fillRect(row * Frame.WIDTH / length, col * Frame.HEIGHT
						/ length, Frame.WIDTH / length, Frame.HEIGHT / length);
				//g.setColor(Color.BLACK);
				g.drawRect(row * Frame.WIDTH / length, col * Frame.HEIGHT
						/ length, Frame.WIDTH / length, Frame.HEIGHT / length);

			}
		}
	}

	public void setPreferredSize(int width, int height) {
		super.setPreferredSize(new Dimension(width, height));
	}

	public void update() {
		if (firstTime) {
			m.addLand();
			firstTime = false;
			return;
		}
		// get teh tile map for changing
		Tile[][] map = m.getMap();

		Tile[][] tempMap = new Tile[map[0].length][map[0].length];
		for (int i = 0; i < map.length; i++) {
			tempMap[i] = new Tile[map[i].length];
			for (int j = 0; j < map[i].length; j++) {
				tempMap[i][j] = map[i][j];
			}
		}
		Random r = new Random();

		for (int row = 1; row < map[0].length - 1; row++) {
			for (int col = 1; col < map[0].length - 1; col++) {
				Type t = map[row][col].getType();
				
				// / Create some sort of lakes
				if (m.areLakesEnabled()) {
					if (r.nextInt(100) <= 1) {
						// change all blocks to Water,
						tempMap[row + 1][col].setType(Type.WATER);
						tempMap[row - 1][col].setType(Type.WATER);
						tempMap[row][col + 1].setType(Type.WATER);
						tempMap[row][col - 1].setType(Type.WATER);
					}
					
					else if (r.nextInt(100) <= 100 && m.isSurroundedBy(row, col, Type.WATER)){
						tempMap[row][col].setType(Type.WATER);
					}
					
				}
				
				
				
				/*******************
				 * GRASS CHECK
				 ************************/
				
				if (t == Type.GRASS) {

					// check number of sides have sand surrounding
					int waterCounter = 0;
					if (map[row + 1][col].getType() == Type.WATER)
						waterCounter++;
					if (map[row - 1][col].getType() == Type.WATER)
						waterCounter++;
					if (map[row][col - 1].getType() == Type.WATER)
						waterCounter++;
					if (map[row][col + 1].getType() == Type.WATER)
						waterCounter++;

					// check number of sides have sand surrounding
					int sandCounter = 0;
					if (map[row + 1][col].getType() == Type.SAND)
						sandCounter++;
					if (map[row - 1][col].getType() == Type.SAND)
						sandCounter++;
					if (map[row][col - 1].getType() == Type.SAND)
						sandCounter++;
					if (map[row][col + 1].getType() == Type.SAND)
						sandCounter++;

					// full water surround
					if (waterCounter == 4) { // right
						System.out.println("surrounded");
						// change all blocks to sand
						tempMap[row + 1][col].setType(Type.SAND);
						tempMap[row - 1][col].setType(Type.SAND);
						tempMap[row][col + 1].setType(Type.SAND);
						tempMap[row][col - 1].setType(Type.SAND);
					}
					// 3 sided respond
					else if (waterCounter == 3) {
						System.out.println("Three sides");
						if (map[row - 1][col].getType() == Type.WATER) {
							tempMap[row - 1][col].setType(Type.SAND);
						} else if (map[row + 1][col].getType() == Type.WATER) {
							tempMap[row + 1][col].setType(Type.SAND);
						} else if (map[row][col + 1].getType() == Type.WATER) {
							tempMap[row][col + 1].setType(Type.SAND);
						} else if (map[row][col - 1].getType() == Type.WATER) {
							tempMap[row][col - 1].setType(Type.SAND);
						}
					}

					// ######## SAND COUNT #######
					if (sandCounter == 4) {
						if (r.nextBoolean() == true) {
							tempMap[row][col].setType(Type.WATER);
						}
					}

					// ####### GRASS COUNT ######
				}
				/*******************
				 * SAND CHECK
				 ************************/
				else if (t == Type.SAND) {

					// check number of sides have sand surrounding
					int sandCounter = 0;
					if (map[row + 1][col].getType() == Type.SAND)
						sandCounter++;
					if (map[row - 1][col].getType() == Type.SAND)
						sandCounter++;
					if (map[row][col - 1].getType() == Type.SAND)
						sandCounter++;
					if (map[row][col + 1].getType() == Type.SAND)
						sandCounter++;

					// Check water around sand
					int waterCounter = 0;
					if (map[row + 1][col].getType() == Type.WATER)
						waterCounter++;
					if (map[row - 1][col].getType() == Type.WATER)
						waterCounter++;
					if (map[row][col - 1].getType() == Type.WATER)
						waterCounter++;
					if (map[row][col + 1].getType() == Type.WATER)
						waterCounter++;

					// check number of sides have grass surrounding
					int grassCounter = 0;
					if (map[row + 1][col].getType() == Type.GRASS)
						grassCounter++;
					if (map[row - 1][col].getType() == Type.GRASS)
						grassCounter++;
					if (map[row][col - 1].getType() == Type.GRASS)
						grassCounter++;
					if (map[row][col + 1].getType() == Type.GRASS)
						grassCounter++;

					// full sand surround
					if (sandCounter + grassCounter == 4) { // right
						// change all blocks to sand
						if (r.nextInt(10) <= 5)
							tempMap[row][col].setType(Type.GRASS);
					}
					// 3 sided respond

					else if (sandCounter == 3) {
						if (r.nextInt(10) <= 3)
							if (map[row - 1][col].getType() == Type.WATER) {
								tempMap[row - 1][col].setType(Type.SAND);
							} else if (map[row + 1][col].getType() == Type.WATER) {
								tempMap[row + 1][col].setType(Type.SAND);
							} else if (map[row][col + 1].getType() == Type.WATER) {
								tempMap[row][col + 1].setType(Type.SAND);
							} else if (map[row][col - 1].getType() == Type.WATER) {
								tempMap[row][col - 1].setType(Type.SAND);
							}

					}

					// / waterCounter tests
					// w
					// w s w
					// w
					if (waterCounter == 4) {
						// tempMap[row + 1][col].setType(Type.SAND);
						// tempMap[row - 1][col].setType(Type.SAND);
						// tempMap[row][col - 1].setType(Type.SAND);
						// tempMap[row][col + 1].setType(Type.SAND);
					} else if (waterCounter == 3) {
						// check the w
						// w s w
						if (map[row - 1][col].getType() == Type.WATER
								&& map[row][col - 1].getType() == Type.WATER
								&& map[row][col + 1].getType() == Type.WATER) {
							// randomness
							int a = r.nextInt(6);
							if (a <= 1) {
								tempMap[row - 1][col].setType(Type.SAND);
								if (r.nextBoolean() == true)
									tempMap[row][col - 1].setType(Type.SAND);
								if (r.nextBoolean() == true)
									tempMap[row][col + 1].setType(Type.SAND);
							}
						}
						// check the w
						// s w
						// w
						else if (map[row - 1][col].getType() == Type.WATER
								&& map[row][col + 1].getType() == Type.WATER
								&& map[row - 1][col].getType() == Type.WATER) {
							// randomness
							int a = r.nextInt(6);
							if (a <= 1) {
								tempMap[row - 1][col].setType(Type.SAND);
								tempMap[row][col - 1].setType(Type.SAND);
								tempMap[row][col + 1].setType(Type.SAND);
							}
						}
						// check the
						// w s w
						// w
						else if (map[row + 1][col].getType() == Type.WATER
								&& map[row][col - 1].getType() == Type.WATER
								&& map[row][col + 1].getType() == Type.WATER) {
							// randomness
							int a = r.nextInt(6);
							if (a <= 1) {
								tempMap[row - 1][col].setType(Type.SAND);
								tempMap[row][col - 1].setType(Type.SAND);
								tempMap[row][col + 1].setType(Type.SAND);
							}
						}
						// check the w
						// w s
						// w
						if (map[row - 1][col].getType() == Type.WATER
								&& map[row][col - 1].getType() == Type.WATER
								&& map[row - 1][col].getType() == Type.WATER) {
							// randomness
							int a = r.nextInt(6);
							if (a <= 1) {
								tempMap[row - 1][col].setType(Type.SAND);
								tempMap[row][col - 1].setType(Type.SAND);
								tempMap[row][col + 1].setType(Type.SAND);
							}
						}
					}
					// Edge w w w
					// s s s
					else if (waterCounter == 1) {
						// top
						if (map[row - 1][col].getType() == Type.WATER) {
							tempMap[row - 1][col].setType(Type.SAND);
						}
						// bottom
						else if (map[row + 1][col].getType() == Type.WATER) {
							tempMap[row + 1][col].setType(Type.SAND);
						} else if (map[row][col + 1].getType() == Type.WATER) {
							tempMap[row][col + 1].setType(Type.SAND);
						} else if (map[row][col - 1].getType() == Type.WATER) {
							tempMap[row][col - 1].setType(Type.SAND);
						}
					}

					else if (waterCounter == 0) {
					}

					if (grassCounter == 4) {
						// map[row][col].setType(Type.GRASS);

					}

				} else if (t == Type.WATER) {
					// map[row][col].setType(Type.GRASS);
					// check number of sides have grass surrounding
					int landCounter = 0;
					if (map[row + 1][col].getType() == Type.GRASS
							|| map[row + 1][col].getType() == Type.SAND)
						landCounter++;
					if (map[row - 1][col].getType() == Type.GRASS
							|| map[row - 1][col].getType() == Type.SAND)
						landCounter++;
					if (map[row][col - 1].getType() == Type.GRASS
							|| map[row][col - 1].getType() == Type.SAND)
						landCounter++;
					if (map[row][col + 1].getType() == Type.GRASS
							|| map[row][col + 1].getType() == Type.SAND)
						landCounter++;

					

				}

			}

		}

		m.setMap(tempMap);
		this.repaint();
	}
}
