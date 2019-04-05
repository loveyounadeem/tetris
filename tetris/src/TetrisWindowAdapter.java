
//The WindowListener for the frame
//Greg Terrono

import java.awt.event.*;

public class TetrisWindowAdapter extends WindowAdapter
{
	private Tetris t;

	//The constructor
	public TetrisWindowAdapter(Tetris tet)
	{
		//Saving the Tetris JPanel
		t=tet;
	}
	
	//Saves the "savedGames" before the window is closed and the program stops
	public void windowClosing(WindowEvent e)
	{
		t.saveGames();
		System.exit(0);
	}
	
	//Pauses the game if the window is deactivated
	public void windowDeactivated(WindowEvent e)
	{
		t.pauseOn();
	}
}
