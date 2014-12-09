import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.LinkedList;

import javax.swing.JFrame;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class Board extends JFrame {
	
	private static final int start = 50;
	private static final int r = 40;
	public static final Color teamColor[] = {Color.red, Color.blue};
	public static final Color adjColor[] = {Color.white, Color.green, Color.yellow};
	
	private JPanel boardPanel;
	private Hex selected;
	
	public LinkedList<Hex> hexList = new LinkedList<Hex>();
	
	public Board(int size) {
		//------------- CREATE HEXAGON -------------
		int x = ((3*size+2)*r)/2, y = start;
		for(int n = -size+1; n < size; n++) {
			int jIndex = n+4;
			int xoffset = 60*n + x;
			int maxHeight = 2*size - Math.abs(n) - 1;
			for(int h = 0; h < maxHeight; h++) {
				int iIndex = 2*size - maxHeight + 2*h -1;
				if((iIndex == 6 && jIndex == 4) || (iIndex == 9 && jIndex == 3) || (iIndex == 9 && jIndex == 5))
					continue; // hole
				int yoffset = y + 69*h + 35*Math.abs(n);			// with r = 40
				Hex hex = new Hex(iIndex, jIndex, xoffset, yoffset);
				for(int i = 0; i < 6; i++) {
				    hex.addPoint((int)(xoffset + r*Math.cos(i*2*Math.PI/6)), (int)(yoffset + r*Math.sin(i*2*Math.PI/6)));
				}
				hexList.add(hex);
			}
		}
		//------------- END CREATE HEXAGON -------------
		
		//------------- DRAW ON PANEL -------------
		boardPanel = new JPanel() {
			protected void paintComponent(Graphics g) {
				super.paintComponents(g);
				Graphics2D g2 = (Graphics2D) g;
				g2.setStroke(new BasicStroke(3));
				for(Hex h : hexList) {
					g.setColor(h.getBg());	// color for background
					g.fillPolygon(h);
					g.setColor(Color.black);	// color for line
					g.drawPolygon(h);
					int value = h.getValue();
					if(value > 0) {
						g.setColor(teamColor[value-1]);
						int r = 25;
						g.fillOval(h.getxCenter()-r, h.getyCenter()-r, 2*r, 2*r);
						g.setColor(Color.black);
						g.drawOval(h.getxCenter()-r, h.getyCenter()-r, 2*r, 2*r);
					}
				}
			}
		};
		//------------- END DRAW ON PANEL -------------
		
		//------------- ADD MOUSE ACTION -------------
		MouseAdapter ma = new MouseAdapter() {
			
	        @Override
	        public void mousePressed(MouseEvent m) {
	        	Point p = m.getPoint();
	        	for(int i = 0; i < hexList.size(); i++) {
	        		Hex h1 = hexList.get(i);
	          		if(h1.contains(p)) {
	          			selected = h1;
	          			System.out.println(h1);
	          			for(Hex h2 : hexList){
	          				int distance = distance(h1,h2);
	          				if(distance < 3)
	          					h2.setBg(adjColor[distance]);
	          			}
	          			break;
	          		}
	        	}
	            repaint();
	        }

	        @Override
	        public void mouseReleased(MouseEvent m) {
	        	selected = null;
	        	for(Hex h: hexList)
	        		h.setBg(adjColor[0]);
	        }

	        @Override
	        public void mouseDragged(MouseEvent m) {
	        }
		};
		boardPanel.addMouseListener(ma);
		boardPanel.addMouseMotionListener(ma);
		//------------- END MOUSE ACTION -------------
		
//		setLayout(new BorderLayout());
//		boardPanel.setPreferredSize(new Dimension((3*size+2)*r, 50*(2*size-1)+start));
//		add(boardPanel, BorderLayout.EAST);
		add(boardPanel);
		setBackground(Color.darkGray);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setFocusable(false);
		setSize(700, 700);
		setLocationRelativeTo(null);
		setVisible(true);
	}

	public void updateBoard(int[][] state) {
		for(Hex h: hexList) {
			h.setValue(state[h.getI()][h.getJ()]);
		}
		repaint();
	}
	
	public int distance(Hex h1, Hex h2) {
		return distance(h1.getI(), h1.getJ(), h2.getI(), h2.getJ());
	}
	
	public int distance(int i1, int j1, int i2, int j2) {
		return (Math.abs(i1-i2)/2+Math.abs(j1-j2));
	}
}
