
//Both the MouseListener and the MouseMotionListener for the game
//Greg Terrono

import java.awt.*;
import java.awt.event.*;
import java.awt.image.MemoryImageSource;

//I had to use only one "Adapter" and the other had to be a "Listener" because I can only extend one class 
//while I can implement any amount of interfaces I want
//And "Adapters" are classes and "Listeners" are interfaces
public class TetrisMouseListener extends MouseAdapter implements MouseMotionListener
{
	private Tetris t;
	
	//The constructor
	public TetrisMouseListener(Tetris tet)
	{
		//Saves the Tetris JPanel
		t=tet;
	}
	
	//Listens for when the mouse is moved
	public void mouseMoved(MouseEvent arg0) 
	{
		int selected=-1;
		//During the game the cursor is hidden when it is over the center area
		if (t.isInGame())
		{
			if(arg0.getX()>90&&arg0.getX()<390&&arg0.getY()<640&&arg0.getY()>40)
			{
				Image image = Toolkit.getDefaultToolkit().createImage(
				        new MemoryImageSource(16, 16, new int[256], 0, 16));
				Cursor transparentCursor =
				        Toolkit.getDefaultToolkit().createCustomCursor
				             (image, new Point(0, 0), "invisibleCursor");
				t.setCursor(transparentCursor);
			}
			else
				t.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			return;
		}
		//Listens for the following few movements only if the user is in the main menu, none of the sub-menus
		if (!t.isAbout()&&!t.isInstruction()&&!t.isHighScore()&&!t.isSavedGames())
		{
			//If the user hovers over "Play Game", turn it yellow and change the cursor
			if (arg0.getX()>110&&arg0.getX()<385&&arg0.getY()<165&&arg0.getY()>105)
			{
				selected=0;
				t.setCursor(new Cursor(Cursor.HAND_CURSOR));
			}
			//If the user hovers over "About", turn it yellow and change the cursor
			else if (arg0.getX()>160&&arg0.getX()<325&&arg0.getY()<260&&arg0.getY()>205)
			{
				selected=1;
				t.setCursor(new Cursor(Cursor.HAND_CURSOR));
			}
			//If the user hovers over "Instructions", turn it yellow and change the cursor
			else if (arg0.getX()>90&&arg0.getX()<395&&arg0.getY()<360&&arg0.getY()>305)
			{
				selected=2;
				t.setCursor(new Cursor(Cursor.HAND_CURSOR));
			}
			//If the user hovers over "High Scores", turn it yellow and change the cursor
			else if (arg0.getX()>90&&arg0.getX()<395&&arg0.getY()<465&&arg0.getY()>405)
			{
				selected=3;
				t.setCursor(new Cursor(Cursor.HAND_CURSOR));
			}
			//If the user hovers over "Saved Games", turn it yellow and change the cursor
			else if (arg0.getX()>70&&arg0.getX()<410&&arg0.getY()<560&&arg0.getY()>505)
			{
				selected=4;
				t.setCursor(new Cursor(Cursor.HAND_CURSOR));
			}
		}
		//In the sub-menus, if the user hovers over "Back", turn it yellow and change the cursor
		else if (arg0.getX()>190&&arg0.getX()<300&&arg0.getY()<660&&arg0.getY()>615)
		{
			selected=6;
			t.setCursor(new Cursor(Cursor.HAND_CURSOR));
		}
		//The savedGames sub-menu has a more complicated mouse setup
		else if (t.isSavedGames())
			{
				int numGames=t.getSavedGames().length;
				//Handles the actual saved games to choose
				if (arg0.getX()>15&&arg0.getX()<380&&arg0.getY()>100&&arg0.getY()<595)
				{
					selected=10+(int)((arg0.getY()-100)/50+t.getSavedGamesStart());
					t.setCursor(new Cursor((selected>9+numGames||selected>19+t.getSavedGamesStart())?Cursor.DEFAULT_CURSOR:Cursor.HAND_CURSOR));
				}
				//Handles the delete buttons for the saved games
				else if (arg0.getX()>385&&arg0.getX()<470&&arg0.getY()>100&&arg0.getY()<595)
				{
					selected=100+(int)((arg0.getY()-100)/50+t.getSavedGamesStart());
					t.setCursor(new Cursor((selected>99+numGames||selected>109+t.getSavedGamesStart())?Cursor.DEFAULT_CURSOR:Cursor.HAND_CURSOR));
				}
				//Handles the up arrow
				else if (arg0.getX()>430&&arg0.getX()<470&&arg0.getY()>63&&arg0.getY()<100&&numGames>10&&t.getSavedGamesStart()>0)
				{
					selected=37;
					t.setCursor(new Cursor(Cursor.HAND_CURSOR));
				}
				//Handles the down arrow
				else if (arg0.getX()>430&&arg0.getX()<465&&arg0.getY()>600&&arg0.getY()<637&&numGames>10&&t.getSavedGamesStart()+10<numGames)
				{
					selected=73;
					t.setCursor(new Cursor(Cursor.HAND_CURSOR));
				}
			}
		//If nothing is selected change the cursor back to normal
		if (selected==-1)
			t.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		//Turns the proper things yellow and others white
		t.setSelected(selected);
		t.repaint();
	}

	//Listens for when the mouse is clicked
	public void mouseClicked(MouseEvent arg0) 
	{
		//Listens for the following few movements only if the user is in the main menu, none of the sub-menus
		if (t.isMenu()&&!t.isAbout()&&!t.isInstruction()&&!t.isHighScore()&&!t.isSavedGames())
		{
			//Handles the clicks on the proper main menu item
			if (arg0.getX()>110&&arg0.getX()<385&&arg0.getY()<165&&arg0.getY()>105)
				t.start();
			else if (arg0.getX()>160&&arg0.getX()<325&&arg0.getY()<260&&arg0.getY()>205)
				t.aboutMenu();
			else if (arg0.getX()>90&&arg0.getX()<395&&arg0.getY()<360&&arg0.getY()>305)
				t.instructionMenu();
			else if (arg0.getX()>90&&arg0.getX()<395&&arg0.getY()<465&&arg0.getY()>405)
				t.highScoreMenu();
			else if (arg0.getX()>70&&arg0.getX()<410&&arg0.getY()<560&&arg0.getY()>505)
				t.savedGamesMenu();
		}
		//In the sub-menus, listens for when the "Back" button is clicked and returns to the main menu
		else if (arg0.getX()>190&&arg0.getX()<300&&arg0.getY()<660&&arg0.getY()>615)
			if (t.isAbout())
				t.aboutMenu();
			else if (t.isInstruction())
				t.instructionMenu();
			else if (t.isHighScore())
				t.highScoreMenu();
			else
				t.savedGamesMenu();
		//The savedGames sub-menu has a more complicated mouse setup
		else if (t.isSavedGames())
		{
			int numGames=t.getSavedGames().length;
			//Handles when a saved games is chosen
			if (arg0.getX()>15&&arg0.getX()<380&&arg0.getY()>100&&arg0.getY()<595)
				t.loadGame((int)((arg0.getY()-115)/50)+t.getSavedGamesStart());
			//Handles the delete buttons for the saved games
			else if (arg0.getX()>385&&arg0.getX()<430&&arg0.getY()>100&&arg0.getY()<595&&numGames>0)
				t.deleteGame((int)((arg0.getY()-115)/50)+t.getSavedGamesStart());
			//Handles the up arrow
			else if (arg0.getX()>430&&arg0.getX()<470&&arg0.getY()>63&&arg0.getY()<100&&numGames>10&&t.getSavedGamesStart()>0)
				t.changeSavedGameStartBy(-1);
			//Handles the down arrow
			else if (arg0.getX()>430&&arg0.getX()<465&&arg0.getY()>600&&arg0.getY()<637&&numGames>10&&t.getSavedGamesStart()+10<numGames)
				t.changeSavedGameStartBy(1);
		}
		mouseMoved(arg0);
	}

	//A method that is needed because I implemented the MouseMotionListener but does nothing for me
	public void mouseDragged(MouseEvent arg0) {}

}
