
//The menu bar for the frame
//Greg Terrono

import java.awt.event.*;
import javax.swing.*;

public class TetrisMenuBar extends JMenuBar
{
	private static final long serialVersionUID = 1L;
	private Tetris t;
	
	//The constructor
	public TetrisMenuBar(Tetris tet)
	{
		//Saves the tetris JPanel
		t=tet;
		//Making the "Game" menu
		JMenu menu1= new JMenu("Game");
		//Making the items in the "Game" menu with various shortcuts to call them
		//A new game option
		JMenuItem newGame= new JMenuItem("New Game", KeyEvent.VK_N);
		newGame.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e) 
			{
				if (t.isInGame())
					t.start();
			}
		});
		newGame.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.ALT_MASK));
		menu1.add(newGame);
		//Another way to pause the game
		JMenuItem pause= new JMenuItem("Pause",KeyEvent.VK_P);
		pause.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e) 
			{
				if (t.isInGame())
					t.pause();
			}
		});
		pause.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, ActionEvent.ALT_MASK));
		menu1.add(pause);
		//You can jump to whatever level you want between 1 and 15
		JMenuItem levelJump= new JMenuItem("Level Jump",KeyEvent.VK_L);
		levelJump.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e) 
			{
				if (t.isInGame())
				{
					int level=-1;
					while (level<1||level>15)
						level=Integer.parseInt(JOptionPane.showInputDialog(null, 
								"Choose a level (1-15)", 
								"Enter Level", JOptionPane.QUESTION_MESSAGE));
					t.start();
					t.setLevel(level);
				}
			}
		});
		levelJump.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, ActionEvent.ALT_MASK));
		menu1.add(levelJump);
		//Goes back to the main menu
		JMenuItem returnToMenu= new JMenuItem("Return to Menu",KeyEvent.VK_R);
		returnToMenu.addActionListener(new ActionListener()
			{public void actionPerformed(ActionEvent e) {t.menuOn();}});
		returnToMenu.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, ActionEvent.ALT_MASK));
		menu1.add(returnToMenu);
		//Saves the game
		JMenuItem save= new JMenuItem("Save Game",KeyEvent.VK_S);
		save.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e) 
			{
				if (!t.isMenu())
				{
					String name=JOptionPane.showInputDialog(null, "What name would you like to save this game under?\nNote: Your name can be no longer than 10 characters.",
							"Tetris", JOptionPane.INFORMATION_MESSAGE);	
					if (name!=null)
						t.saveGame(name);
				}
			}
		});
		save.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.ALT_MASK));
		menu1.add(save);
		menu1.addSeparator();
		//Quits the game
		JMenuItem exit= new JMenuItem("Exit",KeyEvent.VK_E);
		exit.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e) 
			{
				t.saveGames();
				System.exit(0);
			}
		});
		menu1.add(exit);
		exit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, ActionEvent.ALT_MASK));
		add(menu1);
		//Making the "Help" menu
		JMenu menu2=new JMenu("Help");
		//Making the items in the "Help" menu with various shortcuts to call them
		//Brings up a JOptionPane with the controls
		JMenuItem instructions= new JMenuItem("Instructions",KeyEvent.VK_I);
		instructions.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e) 
			{
				JOptionPane.showMessageDialog(null, "Up        -Rotate\nLeft      -Move Left\nRight    -Move Right\nDown   -Drop\nSpace  -Hold" +
						"\nR           -Return to Menu\nP           -Pause\nS           -Save Game\nEsc       -Quit","Instructions", JOptionPane.OK_OPTION, new ImageIcon());
			}});
		instructions.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_I, ActionEvent.ALT_MASK));
		menu2.add(instructions);
		menu2.addSeparator();
		//Brings up a JOptionPane with the about info
		JMenuItem about=new JMenuItem("About",KeyEvent.VK_A);
		about.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0) 
			{
				JOptionPane.showMessageDialog(null, "Made by Nadeem Omar\nand Shael Verma",
						"About", JOptionPane.OK_OPTION, new ImageIcon());
			}
		});
		about.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, ActionEvent.ALT_MASK));
		menu2.add(about);
		add(menu2);
		
	}
}
