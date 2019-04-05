
//The class where the game takes place
//Greg Terrono

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.Timer;

public class Tetris extends JPanel 
{
	private static final long serialVersionUID = 1L;
	private Color[][] blocks=new Color[20][10], pausedBlocks= new Color[20][10];
	private Random r= new Random();
	private Piece currentPiece=new Piece(1,this), hold=null;
	private Piece[] nextPieces={newPiece(), newPiece(), newPiece()};
	private Timer t;
	private int points=0, savedGamesStart=0, level, linesToGo, savedGameSelected=-1, 
			deleteGameSelected=-1, newLevel;
	private boolean held=false, paused=false, menu, about=false, instruction=false, highScore=false, savedGame=false, 
			inGame=false, justDropped=false, dropped=false, upArrowSelected=false, downArrowSelected=false;
	private MainMenuItem[] items={new MainMenuItem("Play Game", 120, 150),
			new MainMenuItem("About", 170, 250),new MainMenuItem("Instructions", 100, 350),
			new MainMenuItem("High Scores", 100, 450), new MainMenuItem("Saved Games", 80, 550)};
	private MainMenuItem back= new MainMenuItem("Back", 200, 655);
	private SavedGame[] savedGames=SavedGame.readGames();
	
	//The constructor
	public Tetris()
	{
		//Initializes the pausedBlocks
		for (int i=0; i<pausedBlocks.length; i++)
		{
			Color[] c=new Color[10];
			for (int j=0; j<c.length; j++)
				c[j]=Color.black;
			pausedBlocks[i]=c;
		}
		
		//The timer that keeps the game moving
		t= new Timer(400-level*25, new ActionListener()
		{
			//The method that listens for the timers ActionEvent
			public void actionPerformed(ActionEvent e) 
			{
				//Controls the "Level:" writing on the screen
				if (newLevel>0)
					newLevel--;
				if (currentPiece.isMoving()==false&&!justDropped)
				{
					//If a piece stops moving, check for to see if a line was completed
					for (int i=0; i<blocks.length; i++)
					{
						if (isFull(blocks[i]))
						{
							for (int j=i; j>0; j--)
								blocks[j]=blocks[j-1];
							Color[] c=new Color[10];
							for (int j=0; j<c.length; j++)
								c[j]=Color.black;
							blocks[0]=c;
							points+=50*level;
							if (--linesToGo==0)
							{
								newLevel=7;
								linesToGo=5;
								level++;
								points+=500;
								t.setDelay(400-level*25);
								t.setInitialDelay(400-level*25);
							}
						}
								
					}
					//Prevents the lose() method from being called twice
					boolean alreadyLost=false;
					//Checking for losing conditions
					for (int i=0; i<nextPieces[0].getNumBlocks()[0]; i++)
						if (!(blocks[0][nextPieces[0].getStart()[0]+i].equals(Color.black)))
						{
							lose();
							alreadyLost=true;
							break;
						}
					if (currentPiece.getLine()[currentPiece.getLine().length-1]<0&&!(currentPiece.getColor().equals(Color.black))&&!alreadyLost)
						lose();
					//If the user did not lose, get a  new piece
					else if(!alreadyLost)
					{
						currentPiece=nextPieces[0];
						nextPieces[0]=nextPieces[1];
						nextPieces[1]=nextPieces[2];
						nextPieces[2]=newPiece();
						dropped=false;
						held=false;
					}
				}
				//If the piece is still moving, check to see if it can continue moving
				else if (!justDropped)
				{
					if (willWork(0,1))
					{
						//If so, move it down one
						blackoutPiece();
						currentPiece.add(0,1);
						dropped=false;
					}
					else
						//If not, the piece is set to stop moving
						currentPiece.setMoving(false);
				}
				//Show the change
				validatePiece();
				justDropped=false;
				repaint();
			}
		});
		setBackground(Color.black);
		//Adding the mouseListener and start the menu
		TetrisMouseListener menuListener=new TetrisMouseListener(this);
		addMouseMotionListener(menuListener);
		addMouseListener(menuListener);
		menuOn();
	}
	
	//Starts the game
	public void start()
	{
		//Initializing all the squares to be black
		blocks=new Color[20][10];
		for (int i=0; i<blocks.length; i++)
			for (int j=0; j<blocks[i].length; j++)
				blocks[i][j]=Color.black;
		//Deselecting all of the menu items
		setSelected(-1);
		//Initializing all of the variables
		currentPiece=newPiece();
		for (int i=0; i<nextPieces.length; i++)
			nextPieces[i]=newPiece();
		points=0;
		level=1;
		linesToGo=5;
		hold=null;
		newLevel=7;
		about=false;
		instruction=false;
		highScore=false;
		savedGame=false;
		menu=false;
		paused=false;
		inGame=true;
		t.setDelay(400-level*25);
		//Beginning the game
		t.start();
		repaint();
	}

	//A getter method
	public boolean isInGame() 
	{
		return inGame;
	}

	//A getter method
	public Color[][] getBlocks()
	{
		return blocks;
	}

	//A setter method
	public void setLevel(int level2) 
	{
		level=level2;
		t.setDelay(400-level*25);
	}

	//What happens when the down key is pressed
	public void down()
	{
		//The piece goes to the lowest square possible
		if (currentPiece.isMoving()&&!dropped&&!paused)
		{
			int i;
			for (i=0; i<20&&willWork(0,i); i++){}
			blackoutPiece();
			currentPiece.add(0,i-1);
			validatePiece();
			points+=(i-1)*3;
			dropped=true;
			justDropped=true;
			currentPiece.setMoving(true);
		}
	}

	//What happens when the left key is pressed
	public void left()
	{
		//Moving the piece left, if possible
		if(currentPiece.isMoving()&&!paused&&willWork(-1,0))
		{
			blackoutPiece();
			currentPiece.add(-1, 0);
			validatePiece();
			currentPiece.setMoving(true);
		}
	}

	//What happens when the right key is pressed
	public void right()
	{
		//Moving the piece right, if possible
		if(currentPiece.isMoving()&&!paused&&willWork(1,0))
		{
			blackoutPiece();
			currentPiece.add(1,0);
			validatePiece();
			currentPiece.setMoving(true);
		}
	}

	//What happens when the up key is pressed
	public void rotate()
	{
		//Rotating the piece
		if (currentPiece.isMoving()&&!paused)
		{
			blackoutPiece();
			currentPiece.rotate();
			currentPiece.setMoving(true);
			validatePiece();
		}
	}

	//What happens when the space button is pressed
	public void hold()
	{
		//Holds the piece
		if (currentPiece.isMoving()&&!held&&!paused)
		{
			blackoutPiece();
			if (hold==null)
			{
				hold=new Piece(currentPiece.getId(),this);
				currentPiece=nextPieces[0];
				nextPieces[0]=nextPieces[1];
				nextPieces[1]=nextPieces[2];
				nextPieces[2]=newPiece();
				dropped=false;
				held=true;
			}
			else
			{
				Piece temp=new Piece(currentPiece.getId(), this);
				currentPiece=hold;
				hold=temp;
				dropped=false;
				held=true;
			}
		}
	}

	//Pauses if the game is running, unpauses if the game is paused
	public void pause()
	{
		if (paused)
			t.start();
		else
			t.stop();
		paused=!paused;
		repaint();
	}

	//Turns pause on if it is not already
	public void pauseOn()
	{
		if (!paused)
			pause();
		repaint();
	}

	//Returns a new, random piece
	public Piece newPiece()
	{
		return new Piece(r.nextInt(7)+1, this);
	}
	
	//Returns whether or not line is full
	public boolean isFull(Color[] c)
	{
		for (int i=0; i<c.length; i++)
			if (c[i].equals(Color.black))
				return false;
		return true;
	}
	
	//Returns whether or not a change to the currentPiece would work
	public  boolean willWork(int spaces, int line)
	{
		for (int i=0; i<currentPiece.getStart().length; i++)
		{
			//If currentPiece would be moved off the board, return false
			if (currentPiece.getStart()[i]+spaces<0||currentPiece.getStart()[i]+currentPiece.getNumBlocks()[i]-1+spaces>9
					||currentPiece.getLine()[i]+line>19)
				return false;
			//If currentPiece would move into another piece, return false
			if (currentPiece.getLine()[i]+line>-1)
				for (int j=0; j<currentPiece.getNumBlocks()[i]; j++)
					if (!(blocks[currentPiece.getLine()[i]+line][currentPiece.getStart()[i]+spaces+j].equals(Color.black))
									&&blocks[currentPiece.getLine()[i]+line][currentPiece.getStart()[i]+spaces+j]!=currentPiece.getColor())
						return false;
		}
		//Otherwise, return true
		return true;
	}

	//Returns whether or not a change to a specified piece's info would work
	public  boolean willWork(int spaces, int line, int[] n, int s[], int[] l)
	{
		for (int i=0; i<s.length; i++)
		{
			//If the piece would be moved off the board, return false
			if (s[i]+spaces<0||s[i]+n[i]+spaces>10||l[i]+line>19)
				return false;
			//If the piece would move into another piece, return false
			if (l[i]+line>-1)
				for (int j=0; j<n[i]; j++)
					if (!(blocks[l[i]+line][s[i]+spaces+j].equals(Color.black))
									&&blocks[l[i]+line][s[i]+spaces+j]!=currentPiece.getColor())
							return false;
		}
		//Otherwise, return true
		return true;
	}

	//Ends a game
	public void lose() 
	{
		t.stop();
		//If the player got a high score, save it
		if (points>HighScore.getHighScores()[9].getScore())
		{
			String name=JOptionPane.showInputDialog(null, "You got a high score!\nPlease enter you name.\nNote: Only 10 characters will be saved.",
					"Tetris", JOptionPane.INFORMATION_MESSAGE);
			if (name!=null)
				HighScore.addHighScore(new HighScore(points,level,(name.length()>10)?name.substring(0, 10):name));
		}
		//Asking the player if they want to play again
		if (JOptionPane.showConfirmDialog(null, "You lost. Do you want to play again?",
				"Tetris", JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION)
			start();
		else
		{
			//If not, quit
			saveGames();
			System.exit(0);
		}
	}

	//Turns the main menu on
	public void menuOn()
	{
		t.stop();
		menu=true;
		about=false;
		instruction=false;
		highScore=false;
		savedGame=false;
		inGame=false;
		setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		repaint();
	}
	
	//A getter method
	public boolean isMenu() 
	{
		return menu;
	}

	//Turns the about menu on if it is off and off if it is on
	public void aboutMenu()
	{
		about=!about;
		repaint();
	}

	//A getter method
	public boolean isAbout() 
	{
		return about;
	}

	//Turns the instruction menu on if it is off and off if it is on
	public void instructionMenu() 
	{
		instruction=!instruction;
		repaint();
	}

	//A getter method
	public boolean isInstruction() 
	{
		return instruction;
	}

	//Turns the high score menu on if it is off and off if it is on
	public void highScoreMenu() 
	{
		highScore=!highScore;
		repaint();
	}

	//A getter method
	public boolean isHighScore() 
	{
		return highScore;
	}

	//Turns the saved games menu on if it is off and off if it is on
	public void savedGamesMenu() 
	{
		savedGame=!savedGame;
		savedGamesStart=0;
		repaint();
	}

	//A getter method
	public boolean isSavedGames() 
	{
		return savedGame;
	}

	//Saves the current game to the array
	public void saveGame(String name)
	{
		SavedGame[] games=new SavedGame[savedGames.length+1];
		System.arraycopy(savedGames, 0, games, 0, savedGames.length);
		Color[][] savedBlocks=new Color[20][10];
		for (int j=0; j<blocks.length; j++)
			System.arraycopy(blocks[j], 0, savedBlocks[j], 0, blocks[j].length);
		Piece savedCurrentPiece=new Piece(currentPiece.getId(), this);
		savedCurrentPiece.setColor(currentPiece.getColor());
		savedCurrentPiece.setInfo(currentPiece.getNumBlocks(), currentPiece.getStart(), currentPiece.getLine());
		Piece[] savedNextPiece=new Piece[3];
		for (int j=0; j<nextPieces.length; j++)
			savedNextPiece[j]=new Piece(nextPieces[j].getId(), this);
		SavedGame newGame=new SavedGame((name.length()>10)?name.substring(0, 10):name,savedCurrentPiece,savedBlocks,savedNextPiece,points,
				hold,held,level,linesToGo,new Date());
		games[games.length-1]=newGame;
		savedGames=games;
	}

	//Saves the savedGames array to the .dat file
	public void saveGames() 
	{
		SavedGame.saveGames(savedGames);
	}

	//This method is called when a game is chosen from the menu
	//It loads the chosen game
	public void loadGame(int i)
	{
		if (i<savedGames.length)
		{
			blocks=new Color[20][10];
			for (int j=0; j<blocks.length; j++)
				System.arraycopy(savedGames[i].getBlocks()[j], 0, blocks[j], 0, savedGames[i].getBlocks()[j].length);
			currentPiece=new Piece(savedGames[i].getCurrentPiece().getId(), this);
			currentPiece.setColor(savedGames[i].getCurrentPiece().getColor());
			currentPiece.setInfo(savedGames[i].getCurrentPiece().getNumBlocks(), savedGames[i].getCurrentPiece().getStart(), savedGames[i].getCurrentPiece().getLine());
			for (int j=0; j<nextPieces.length; j++)
				nextPieces[j]=new Piece(savedGames[i].getNextPieces()[j].getId(), this);
			points=savedGames[i].getPoints();
			level=savedGames[i].getLevel();
			linesToGo=savedGames[i].getLinesToGo();
			hold=(savedGames[i].getHold()==null)?null:new Piece(savedGames[i].getHold().getId(), this);
			about=false;
			instruction=false;
			highScore=false;
			savedGame=false;
			menu=false;
			paused=false;
			inGame=true;
			newLevel=7;
			t.setDelay(400-level*25);
			t.start();
			repaint();
		}
	}

	//This method is called when a delete button is clicked
	//Deletes the specified saved game from the array
	public void deleteGame(int i)
	{
		SavedGame[] games=new SavedGame[savedGames.length-1];
		int k=0;
		for (int j=0; j<savedGames.length; j++)
		{
			if (j!=i)
			{
				games[k]=savedGames[j];
				k++;
			}
		}
		savedGames=games;
	}
	
	//A getter method
	public SavedGame[] getSavedGames()
	{
		return savedGames;
	}

	//A getter method
	public int getSavedGamesStart()
	{
		return savedGamesStart;
	}
	
	//This method is called when one of the arrows on the saved game menu is clicked
	public void changeSavedGameStartBy(int i)
	{
		savedGamesStart+=i;
	}

	//Replaces the place in the blocks matrix where currentPiece is with black
	public void blackoutPiece ()
	{
		for (int i=0; i<currentPiece.getLine().length; i++)
			if (currentPiece.getLine()[i]>-1)
				for (int j=0; j<currentPiece.getNumBlocks()[i]; j++)
					blocks[currentPiece.getLine()[i]][currentPiece.getStart()[i]+j]=Color.black;
		repaint();
	}
	
	//Replaces the place in the blocks matrix where currentPiece is with its color	
	public void validatePiece()
	{
		for (int i=0; i<currentPiece.getLine().length; i++)
			if (currentPiece.getLine()[i]>-1)
				for (int j=0; j<currentPiece.getNumBlocks()[i]; j++)
					blocks[currentPiece.getLine()[i]][currentPiece.getStart()[i]+j]=currentPiece.getColor();
	}

	//Selects and deselects menu items
	public void setSelected(int selected)
	{
		for (int i=0; i<items.length; i++)
			items[i].setSelected(i==selected);
		back.setSelected(6==selected);
		savedGameSelected=selected-10;
		deleteGameSelected=selected-100;
		upArrowSelected=37==selected;
		downArrowSelected=73==selected;
	}

	//Draws the game
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		Graphics2D g2 = (Graphics2D) g;
		if (menu)
		{
			g2.setColor(Color.darkGray);
			g2.fillRect(0, 0, 480, 728);
			//Draws the about menu
			if (about)
			{
				g2.setColor(Color.white);
				g2.setFont(new Font("Arial", Font.BOLD, 50));
				g2.drawString("About", 170, 110);				
				g2.setFont(new Font("Arial", Font.BOLD, 30));
				g2.drawString("Code Inspired by Greg Terrono", 32, 200);
				g2.drawString("Made by Nadeem Omar", 75, 300);
				g2.drawString("Made by Shael Verma", 130, 400);
				g2.setFont(new Font("Arial", Font.BOLD, 40));
				g2.setColor(back.getC());
				g2.drawString(back.getTitle(), back.getX(), back.getY());
			}
			//Draws the instruction menu
			else if (instruction)
			{
				g2.setColor(Color.white);
				g2.setFont(new Font("Arial", Font.BOLD, 50));
				g2.drawString("Instructions", 100, 75);
				g2.drawLine(75, 110, 75, 140);
				g2.fillPolygon(new int []{65,85,75}, new int[] {110,110,93}, 3);
				g2.drawLine(76, 180, 106, 180);
				g2.fillPolygon(new int []{76,76,59}, new int[] {170,190,180}, 3);
				g2.drawLine(54, 240, 84, 240);
				g2.fillPolygon(new int []{84,84,101}, new int[] {230,250,240}, 3);
				g2.drawLine(75, 285, 75, 315);
				g2.fillPolygon(new int []{65,85,75}, new int[] {315,315,332}, 3);
				g2.setFont(new Font("Arial", Font.BOLD, 30));
				g2.drawString("Space", 27, 378);
				g2.drawString("R", 65, 438);
				g2.drawString("P", 65, 493);
				g2.drawString("S", 65, 546);
				g2.drawString("Esc", 45, 600);
				g2.drawString("_",200,108);
				g2.drawString("_",200,178);
				g2.drawString("_",200,238);
				g2.drawString("_",200,302);
				g2.drawString("_",200,361);
				g2.drawString("_",200,421);
				g2.drawString("_",200,475);
				g2.drawString("_",200,527);
				g2.drawString("_",200,583);
				g2.drawString("Rotate",240,122);
				g2.drawString("Move Left",240,192);
				g2.drawString("Move Right",240,252);
				g2.drawString("Drop",240,316);
				g2.drawString("Hold Piece", 240, 378);
				g2.drawString("Return to Menu", 240, 438);
				g2.drawString("Pause", 240, 493);
				g2.drawString("Save Game", 240, 546);
				g2.drawString("Quit", 240, 600);
				g2.setFont(new Font("Arial", Font.BOLD, 40));
				g2.setColor(back.getC());
				g2.drawString(back.getTitle(), back.getX(), back.getY());
			}
			//Draws the high score menu
			else if (highScore)
			{
				HighScore[] h=HighScore.getHighScores();
				g2.setColor(Color.white);
				g2.setFont(new Font("Arial", Font.BOLD, 50));
				g2.drawString("High Scores", 100, 60);
				g2.setFont(new Font("Arial", Font.BOLD, 24));
				g2.drawString("Name", 50,120);
				g2.drawString("Score", 270, 120);
				g2.drawString("Level", 405, 120);
				g2.drawLine(0, 140, 480, 140);
				g2.setFont(new Font("Arial", Font.BOLD, 22));
				for (int i=0; i<h.length; i++)
					if (h[i].getScore()>0)
					{
						
						g2.drawString(new Integer(i+1).toString()+".", 15, 180+i*47);
						g2.drawString(h[i].getName(), 50, 180+i*47);
						g2.drawString(new Integer(h[i].getScore()).toString(), 280, 180+i*47);
						g2.drawString(new Integer(h[i].getLevel()).toString(), 420, 180+i*47);
					}
				g2.setFont(new Font("Arial", Font.BOLD, 40));
				g2.setColor(back.getC());
				g2.drawString(back.getTitle(), back.getX(), back.getY());
			}
			//Draws the saved games menu
			else if (savedGame)
			{
				SavedGame[] games=savedGames;
				g2.setColor(Color.white);
				g2.setFont(new Font("Arial", Font.BOLD, 50));
				g2.drawString("Saved Games", 80, 80);
				g2.setFont(new Font("Arial", Font.BOLD, 22));
				if (games.length>10)
				{
					if (savedGamesStart==0)
						g2.setColor(new Color(165,165,165));
					else if (upArrowSelected)
						g2.setColor(Color.yellow);
					else
						g2.setColor(Color.white);
					g2.fillPolygon(new int []{440,460,450}, new int[] {95,95,78}, 3);
					if (savedGamesStart+10==games.length)
						g2.setColor(Color.gray);
					else if (downArrowSelected)
						g2.setColor(Color.yellow);
					else
						g2.setColor(Color.white);
					g2.fillPolygon(new int []{440,460,450}, new int[] {615,615,632}, 3);
				}
				for (int i=savedGamesStart; i<games.length&&i<savedGamesStart+10; i++)
				{
					g2.setColor((savedGameSelected==i)?Color.yellow:Color.white);
					g2.drawString(games[i].getName(), 35, 135+(i-savedGamesStart)*50);
					g2.drawString(games[i].getDate().toString().substring(4, 10)
							+","+games[i].getDate().toString().substring(23), 240, 135+(i-savedGamesStart)*50);
					g2.setColor((deleteGameSelected==i)?Color.yellow:Color.white);
					g2.drawString("Delete", 400, 135+(i-savedGamesStart)*50);
				}
				g2.setFont(new Font("Arial", Font.BOLD, 40));
				g2.setColor(back.getC());
				g2.drawString(back.getTitle(), back.getX(), back.getY());
			}
			//Draws the main menu
			else
			{
				g2.setFont(new Font("Arial", Font.BOLD, 50));
				for (int i=0; i<items.length; i++)
				{
					g2.setColor(items[i].getC());
					g2.drawString(items[i].getTitle(), items[i].getX(), items[i].getY());
				}
			}
		}
		//Draws the game
		else
		{
			g2.setColor(new Color(165,165,165));
			g2.fillRect(0, 0, 90, 760);
			g2.fillRect(390, 0, 100, 760);
			g2.fillRect(80, 0, 375, 40);
			g2.fillRect(80, 640, 375, 50);
			Color[][] pblocks=blocks;
			//If it is paused, replace the game blocks with the paused blocks
			if (paused)
				pblocks=pausedBlocks;
			for (int i=0; i<pblocks.length; i++)
			{
				for (int j=0; j<pblocks[i].length; j++)
				{
					g2.setColor(pblocks[i][j]);
					if (g2.getColor()==Color.black)
						g2.fillRect(90+j*30, 40+i*30, 30, 30);
					else
						g2.fill3DRect(90+j*30, 40+i*30, 30, 30, true);
					g2.setColor(new Color(165,165,165));
					g2.drawRect(90+j*30, 40+i*30, 30, 30);
				}
			}
			
			g2.setColor(Color.white);
			g2.setFont(new Font("Arial", Font.BOLD, 20));
			g2.drawString("Level: "+level, 50, 665);
			g2.drawString("Lines To Go: "+linesToGo, 280, 665);
			g2.drawString("Points: "+points, 190, 28);
			g2.drawString("Next:", 412, 50);
			
			//Draws the held piece
			g2.drawString("Hold:", 15, 50);
			if (hold!=null&&!paused)
			{
				for(int i=hold.getNumBlocks().length-1; i>-1; i--)
					for (int j=0; j<hold.getNumBlocks()[i]; j++)
					{
						g2.setColor(hold.getColor());
						g2.fill3DRect(23+(j+(hold.getStart()[i]-4))*15, 60+(hold.getNumBlocks().length-1-i)*15, 15, 15, true);
						g2.setColor(new Color(165,165,165));
						g2.drawRect(23+(j+(hold.getStart()[i]-4))*15, 60+(hold.getNumBlocks().length-1-i)*15, 15, 15);
					}
			}
			//Draws the next three pieces
			if (!paused)
				for (int q=0; q<nextPieces.length; q++)
					for(int i=nextPieces[q].getNumBlocks().length-1; i>-1; i--)
						for (int j=0; j<nextPieces[q].getNumBlocks()[i]; j++)
						{
							int correction=0;
							for (int w=0; w<q+1; w++)
								correction+=nextPieces[w].getNumBlocks().length;
							g2.setColor(nextPieces[q].getColor());
							g2.fill3DRect(420+(j+(nextPieces[q].getStart()[i]-4))*15, 60+(correction+q-1-i)*15, 15, 15, true);
							g2.setColor(new Color(165,165,165));
							g2.drawRect(420+(j+(nextPieces[q].getStart()[i]-4))*15, 60+(correction+q-1-i)*15, 15, 15);
						}
			if (paused)
			{
				g2.setFont(new Font("Arial", Font.BOLD, 50));
				g2.setColor(Color.white);
				g2.drawString("Paused", 150, 358);
			}
			else if (newLevel>0)
			{
				g2.setFont(new Font("Arial", Font.BOLD, 50));
				g2.setColor(Color.white);
				g2.drawString("Level "+level, 158, 355);
			}
		}
	}
}
