
//The MainMenuItem class is used as a convenient method of keeping the information about "buttons" on the menus together
//Greg Terrono

import java.awt.Color;

public class MainMenuItem
{
	private String title;
	private int x, y;
	private Color c=Color.white;
	
	//The constructor
	public MainMenuItem(String t, int x, int y)
	{
		setTitle(t);
		this.setX(x);
		this.setY(y);
	}
	
	//The text turns yellow if it is selected
	public void setSelected(boolean selected)
	{
		c=(selected)?Color.yellow:Color.white;
	}

	//The setters and getters
	public void setTitle(String title) 
	{
		this.title = title;
	}

	public String getTitle() 
	{
		return title;
	}

	public void setX(int x) 
	{
		this.x = x;
	}

	public int getX() 
	{
		return x;
	}

	public void setY(int y) 
	{
		this.y = y;
	}

	public int getY() 
	{
		return y;
	}

	public void setC(Color c) 
	{
		this.c = c;
	}

	public Color getC() 
	{
		return c;
	}
}
