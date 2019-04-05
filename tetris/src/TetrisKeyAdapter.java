
//The KeyListener for the frame
//Greg Terrono

import java.awt.event.*;
import javax.swing.JOptionPane;


public class TetrisKeyAdapter extends KeyAdapter
{
	private Tetris t;
	
	//The constructor
	public TetrisKeyAdapter(Tetris t)
	{
		//Saves the Tetris JPanel
		this.t=t;
	}
	
	//Handles events when keys are pressed
	public void keyPressed (KeyEvent e)
	{
		//Listen for these keys only if the game is happening
		if (t.isInGame())
		{
			//If the user presses p, pause the game
			if (e.getKeyCode() == KeyEvent.VK_P)
				t.pause();
			//If the user presses e, return to the main menu
			else if (e.getKeyCode() == KeyEvent.VK_R)
				t.menuOn();
			//If the user presses s, ask for their name and save the game
			else if (e.getKeyCode() == KeyEvent.VK_S)
			{
				String name;
				name=JOptionPane.showInputDialog(null, "What name would you like to save this game under?\nNote: Your name can be no longer than 10 characters.",
						"Tetris", JOptionPane.INFORMATION_MESSAGE);	
				//If the user presses the "cancel" button then the string it returns is null, so it does not save it
				if (name!=null)
					t.saveGame(name);
			}
			//If the user presses space, hold the current piece
			else if (e.getKeyCode() == KeyEvent.VK_SPACE)
				t.hold();
			//If the user presses the up arrow key, rotate the current piece
			else if (e.getKeyCode() == KeyEvent.VK_UP)
				t.rotate();
			//If the user presses the down arrow key, the piece drops to the bottom
			else if (e.getKeyCode() == KeyEvent.VK_DOWN)
				t.down();
			//If the user presses the left arrow key, the current piece moves left one space
			else if (e.getKeyCode() == KeyEvent.VK_LEFT)
				t.left();
			//If the user presses the right arrow key, the current piece moves right one space
			else if (e.getKeyCode() == KeyEvent.VK_RIGHT)						
				t.right();
		}
		//If the user presses escape, save the "savedGames" and quit
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
		{
			t.saveGames();
			System.exit(0);
		}
		t.repaint();
	}
}
