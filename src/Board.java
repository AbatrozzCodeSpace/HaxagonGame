import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedList;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

@SuppressWarnings("serial")
public class Board extends JFrame {

	private static final int start = 50;
	private static final int r = 40;
	public static final Color teamColor[] = { Color.red, Color.blue };
	public static final Color adjColor[] = { new Color(1.0f, 1.0f, 1.0f, 0.3f), // Transparent
																				// White
			new Color(200, 8, 255, 125), new Color(250, 126, 250, 125) };
	
	public static final Color beforeAfterMoveColor[] = {new Color(192,234,251,175),new Color(0,189,255,175),new Color(251,207,192,175),new Color(255,36,0,175)};

	private JPanel boardPanel;
	private JLabel redScore;
	private JLabel blueScore;
	private JLabel redBarIndicator;
	private JLabel blueBarIndicator;
	private HaxagonUI firstFrame;
	private Hex selected;

	private LinkedList<Hex> hexList = new LinkedList<Hex>();
	private State state;

	private static Move move;

	private BufferedImage bgImage;
	private ImageIcon blueBall = new ImageIcon(getClass().getResource(
			"Pic/WaterBall.png"));
	private ImageIcon redBall = new ImageIcon(getClass().getResource(
			"Pic/FireBall.png"));

	private ImageIcon blueStar = new ImageIcon(getClass().getResource(
			"Pic/star_blue.png"));
	private ImageIcon redStar = new ImageIcon(getClass().getResource(
			"Pic/star_red.png"));
	private ImageIcon redBar = new ImageIcon(getClass().getResource(
			"Pic/bar_red.png"));
	private ImageIcon blueBar = new ImageIcon(getClass().getResource(
			"Pic/bar_blue.png"));

	private int scoreR;
	private int scoreB;


	public Board(int size, State state, HaxagonUI ui) {
		 bgImage = null;
		try {
			bgImage = ImageIO.read(new File("src\\Pic\\space_800_700.png"));
		} catch (IOException e) {
		}
		setResizable(false);
		setTitle("Haxagon");
		this.state = state;
		firstFrame = ui;
		// ------------- CREATE HEXAGON -------------
		int x = ((3 * size + 2) * r) / 2, y = start;
		for (int n = -size + 1; n < size; n++) {
			int jIndex = n + 4;
			int xoffset = 60 * n + x + 50;
			int maxHeight = 2 * size - Math.abs(n) - 1;
			for (int h = 0; h < maxHeight; h++) {
				int iIndex = 2 * size - maxHeight + 2 * h - 1;
				if ((iIndex == 6 && jIndex == 4)
						|| (iIndex == 9 && jIndex == 3)
						|| (iIndex == 9 && jIndex == 5))
					continue; // hole
				int yoffset = y + 69 * h + 35 * Math.abs(n); // with r = 40
				Hex hex = new Hex(iIndex, jIndex, xoffset, yoffset);
				for (int i = 0; i < 6; i++) {
					hex.addPoint(
							(int) (xoffset + r * Math.cos(i * 2 * Math.PI / 6)),
							(int) (yoffset + r * Math.sin(i * 2 * Math.PI / 6)));
				}
				hexList.add(hex);
			}
		}
		// ------------- END CREATE HEXAGON -------------

		// ------------- DRAW ON PANEL -------------
		
		boardPanel = new JPanel() {
			protected void paintComponent(Graphics g) {
				super.paintComponents(g);
				g.drawImage(bgImage,0,0,null);
				Graphics2D g2 = (Graphics2D) g;
				g2.setStroke(new BasicStroke(3));
				Hexpos[] lastMove = getGameState().getLastHexpos();
				for (Hex h : hexList) {
					State s = getGameState();
					if(!h.getBg().equals(adjColor[0])) {
						g.setColor(h.getBg());
					}
					else if(lastMove[0] != null && h.equal(lastMove[0]) ){
						if(s.whoseTurn()=="blue")g.setColor(beforeAfterMoveColor[2]);
						else g.setColor(beforeAfterMoveColor[0]);
					}
						
					else if(lastMove[0] != null && h.equal(lastMove[1])){
						if(s.whoseTurn()=="blue")g.setColor(beforeAfterMoveColor[3]);
						else g.setColor(beforeAfterMoveColor[1]);
					}
					else
						g.setColor(h.getBg()); // color for background
					g.fillPolygon(h);
					g.setColor(Color.black); // color for line
					g.drawPolygon(h);
					int value = h.getValue();
					if (value < 1)
						continue;
					try {
						if (value == 1) {
							int offsetx = 40;
							int offsety = 29;
							g.drawImage(redBall.getImage(), h.getxCenter()
									- offsetx, h.getyCenter() - offsety, null);
						} else if (value == 2) {
							int offsetx = 34;
							int offsety = 32;
							g.drawImage(blueBall.getImage(), h.getxCenter()
									- offsetx, h.getyCenter() - offsety, null);
						}
					} catch (NullPointerException e) {
						int r = 25;
						g.setColor(teamColor[value - 1]);
						g.fillOval(h.getxCenter() - r, h.getyCenter() - r,
								2 * r, 2 * r);
						g.setColor(Color.black);
						g.drawOval(h.getxCenter() - r, h.getyCenter() - r,
								2 * r, 2 * r);
					}
				}
			}
		};
		// add label for score
		scoreR = 3;
		scoreB = 3;
		boardPanel.setLayout( null );
		redScore = new JLabel( redStar );
		Font f = new Font(Font.SERIF, Font.PLAIN, 32 );

		try {
			f = Font.createFont(Font.TRUETYPE_FONT,
					new FileInputStream(new File("src\\Font\\space age.ttf")))
					.deriveFont(Font.PLAIN, 48);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (FontFormatException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		redScore.setText("");
		redScore.setFont(f);
		redScore.setForeground(Color.white);
		redScore.setHorizontalTextPosition(SwingConstants.CENTER);
		redScore.setHorizontalAlignment(SwingConstants.CENTER);
		redScore.setVerticalTextPosition(SwingConstants.CENTER);
		redScore.setVerticalAlignment(SwingConstants.CENTER);
		boardPanel.add(redScore);
		redScore.setBounds(50, 550, 120, 100);

		blueScore = new JLabel(blueStar);
		blueScore.setFont(f);
		blueScore.setForeground(Color.black);
		blueScore.setHorizontalTextPosition(SwingConstants.CENTER);
		blueScore.setHorizontalAlignment(SwingConstants.CENTER);
		blueScore.setVerticalTextPosition(SwingConstants.CENTER);
		blueScore.setVerticalAlignment(SwingConstants.CENTER);
		boardPanel.add( blueScore );
		blueScore.setBounds(625 , 550 ,120,100);
		
		redBarIndicator = new JLabel( redBar );
		redBarIndicator.setText("Red");
		redBarIndicator.setFont(f);
		redBarIndicator.setForeground(Color.white);
		redBarIndicator.setHorizontalTextPosition(SwingConstants.CENTER);
		redBarIndicator.setHorizontalAlignment(SwingConstants.LEFT);
		redBarIndicator.setVerticalTextPosition(SwingConstants.CENTER);
		redBarIndicator.setVerticalAlignment(SwingConstants.CENTER);
		boardPanel.add( redBarIndicator );
		redBarIndicator.setBounds( 0, 25 ,247,55);
		
		blueBarIndicator = new JLabel( blueBar );
		blueBarIndicator.setText("Blue");
		blueBarIndicator.setFont(f);
		blueBarIndicator.setForeground(Color.white);
		blueBarIndicator.setHorizontalTextPosition(SwingConstants.CENTER);
		blueBarIndicator.setHorizontalAlignment(SwingConstants.RIGHT);
		blueBarIndicator.setVerticalTextPosition(SwingConstants.CENTER);
		blueBarIndicator.setVerticalAlignment(SwingConstants.CENTER);
		boardPanel.add( blueBarIndicator );
		blueBarIndicator.setBounds( 800-247, 25 ,247,55);

		// ------------- END DRAW ON PANEL -------------

		// ------------- ADD MOUSE ACTION -------------
		MouseAdapter ma = new MouseAdapter() {

			@Override
			public void mousePressed(MouseEvent m) {
				State s = getGameState();
				Point p = m.getPoint();
				int whose = s.whoseTurn().equals("red") ? 1 : 2; // if it's
																	// red's
																	// turn,
																	// value
																	// goes to
																	// 1, else 2
				for (int i = 0; i < hexList.size(); i++) {
					Hex h1 = hexList.get(i);
					if (h1.contains(p)) {
						if (h1.getBg().equals(adjColor[0])) {
							if (h1.getValue() == -1 || whose != h1.getValue())
								return;
							for (Hex h : hexList) {
								h.setBg(adjColor[0]);
							}
							System.out.println("selected " + h1 + "value = "
									+ h1.getValue());
							selected = h1;
							for (Hex h2 : hexList) {
								int distance = distance(h1, h2);
								if (distance < 3) {
									if (h2.getValue() == -1) {
										h2.setBg(adjColor[distance]);
									}
									if (distance == 0) {
										h2.setBg(Color.getHSBColor(0, 0, 0.2f));
									}
								}
							}
						} else {
							s = getGameState();
							move = new Move(selected.convertToHexpos(), h1.convertToHexpos(),
									s.whoseTurn());

							if (s.legalMove(move)) {
								System.out.println("move " + selected + " to "
										+ h1);
								synchronized (s) {
									s.notifyAll();
								}
							} else {
								System.out.println("Illegal move " + selected
										+ " to " + h1);
							}
							selected = null;
							for (Hex h : hexList)
								h.setBg(adjColor[0]);
							
						}

						break;
					}
				}
				repaint();
			}

			@Override
			public void mouseReleased(MouseEvent m) {
			}

			@Override
			public void mouseDragged(MouseEvent m) {
			}
		};
		boardPanel.addMouseListener(ma);
		// ------------- END MOUSE ACTION -------------

		// ------------- KEYLISTENER -------------
		KeyListener l = new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {

			}

			@Override
			public void keyReleased(KeyEvent e) {
			}

			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_R && e.isControlDown()) {
					Arbiter.reset();
					Hexxagon.arbiThread.interrupt();
				}
			}
		};
		boardPanel.addKeyListener(l);

		add(firstFrame);
		setBackground(Color.darkGray);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setFocusable(false);
		setSize(800, 700);
		setLocationRelativeTo(null);
		setVisible(true);
	}

	public void updateBoard(int[][] state) {
		for (Hex h : hexList) {
			h.setValue(state[h.getI()][h.getJ()]);
		}

		repaint();
	}

	public void setScore(int red, int blue) {
		scoreR = red;
		scoreB = blue;
		redScore.setText("" + scoreR);
		blueScore.setText("" + scoreB);
	}
	
	public void setPlayer( int player) {
		if ( player == 1) {
			redBarIndicator.setVisible(true);
			blueBarIndicator.setVisible(false);
		} else {
			blueBarIndicator.setVisible(true);
			redBarIndicator.setVisible(false);
		}
	}

	public int distance(Hex h1, Hex h2) {
		return distance(h1.getI(), h1.getJ(), h2.getI(), h2.getJ());
	}

	public int distance(int i1, int j1, int i2, int j2) {
		if (Math.abs(j1 - j2) >= Math.abs(i2 - i1)) {
			return Math.abs(j1 - j2);
		} else {
			return (Math.abs(j1 - j2) + Math.abs(i2 - i1)) / 2;
		}
	}

	public State getGameState() {
		return state;
	}

	public void setGameState(State state) {
		this.state = state;
	}

	public static Move getMove() {
		return move;
	}

	public void gotoMain() {
		remove(firstFrame);
		add(boardPanel);
		boardPanel.requestFocus();
		validate();
		repaint();
	}
}
