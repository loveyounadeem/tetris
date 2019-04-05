
//The Piece class that represents a tetris piece
//Greg Terrono

import java.awt.Color;
import java.io.Serializable;

public class Piece implements Serializable
{
	private static final long serialVersionUID = 1L;
	private int id;
	private boolean moving=true;
	private Color color;
	private transient Tetris t;
	//These are the arrays that represent the info of the piece
	//Their length is the number of lines that the piece takes up at one time
	//On each line the piece starts start blocks over and continues on the line for numBlocks blocks
	//The line is which line in the block[][] matrix each line of the piece is
	//The lowest line of the piece is in the [0] marker because it appears on the screen first
	private int[] numBlocks, start, line;
	
	//The constructor
	public Piece(int i, Tetris t)
	{
		this.t=t;
		id=i;
		//Which of the seven pieces it is depends on the int i that is given
		if (id==1)
		{
			numBlocks=new int[4];
			start=new int[4];
			line=new int[4];
			for (int in=0; in<numBlocks.length; in++)
			{
				numBlocks[in]=1;
				start[in]=4;
				line[in]=0-in;
			}
			
			color=new Color(30,144,255);
		}
		if (id==2)
		{
			numBlocks=new int[2];
			start=new int[2];
			line=new int[2];
			numBlocks[0]=2;
			numBlocks[1]=2;
			for (int in=0; in<numBlocks.length; in++)
			{
				start[in]=4;
				line[in]=0-in;
			}
			
			color=new Color(110,139,61);
		}
		if (id==3)
		{
			numBlocks=new int[2];
			start=new int[2];
			line=new int[2];
			numBlocks[0]=2;
			numBlocks[1]=2;
			start[0]=5;
			start[1]=4;
			line[1]=-1;
			line[0]=0;
			
			color=new Color(255,140,0);
		}
		if (id==4)
		{
			numBlocks=new int[2];
			start=new int[2];
			line=new int[2];
			numBlocks[0]=2;
			numBlocks[1]=2;
			start[0]=4;
			line[0]=0;
			start[1]=5;
			line[1]=-1;
			
			color=new Color(219,112,147);
		}
		if (id==5)
		{
			numBlocks=new int[3];
			start=new int[3];
			line=new int[3];
			numBlocks[0]=1;
			numBlocks[1]=2;
			numBlocks[2]=1;
			for (int in=0; in<numBlocks.length; in++)
			{
				start[in]=4;
				line[in]=0-in;
			}
			
			color=new Color(255,215,0);
		}
		if (id==6)
		{
			numBlocks=new int[2];
			start=new int[2];
			line=new int[2];
			numBlocks[0]=1;
			numBlocks[1]=3;
			for (int in=0; in<numBlocks.length; in++)
			{
				start[in]=4;
				line[in]=0-in;
			}
			
			color=new Color (180,82,205);
		}
		if (id==7)
		{
			numBlocks=new int[2];
			start=new int[2];
			line=new int[2];
			numBlocks[0]=1;
			numBlocks[1]=3;
			start[0]=6;
			line[0]=0;
			start[1]=4;
			line[1]=-1;
			
			color=new Color(255,48,48);
		}
	}
	
	//Changes the start position and the lines depending on the arguments
	public void add(int spaces, int lines)
	{
		for (int i=0; i<start.length; i++)
		{
			start[i]+=spaces;
			line[i]+=lines;
		}
	}
	
	//Rotates the piece
	public void rotate()
	{
		int[] newNumBlocks, newStart, newLine;
		//Finding the width of the piece so that it can be made the height of the rotated piece
		int maxSpace=numBlocks[0]+start[0], minSpace=start[0];
		for (int i=1; i<numBlocks.length; i++)
		{
			if (maxSpace<numBlocks[i]+start[i])
				maxSpace=numBlocks[i]+start[i];
			if (minSpace>start[i])
				minSpace=start[i];
		}
		newNumBlocks=new int[maxSpace-minSpace];
		newStart=new int[maxSpace-minSpace];
		newLine=new int[maxSpace-minSpace];
		//Determines where the piece's lines start in relation to each other
		int [] spaces=new int[newLine.length];
		for (int i=1; i<numBlocks.length; i++)
		{
			boolean did=false, dod=false;
			for (int h=0; h<i; h++)
			{
				if (start[i]-start[h]>0&&!did)
				{
					for (int j=0; j<start[i]-start[h]; j++)
						spaces[j]++;
					did=true;
				}
				if (start[i]+numBlocks[i]-start[h]-numBlocks[h]<0&&!dod)
				{
					for (int j=0; j<spaces.length+start[i]+numBlocks[i]-start[h]-numBlocks[h];j++)
					{
						spaces[j]--;
					}
					dod=true;
				}
			}
		}
		int correction=0;
		for (int i=0; i<spaces.length; i++)
			if (spaces[i]<0&&spaces[i]<correction)
				correction=spaces[i];
		for (int i=0; i<spaces.length; i++)
			spaces[i]-=correction;
		for (int i=0; i<newNumBlocks.length; i++)
			for (int j=numBlocks.length-1; j>-1; j--)
				if (minSpace+i>=start[j]&&minSpace+i<start[j]+numBlocks[j])
				{
					if (newStart[i]==0)
						newStart[i]=spaces[i]+minSpace;
					newNumBlocks[i]++;
				}
		//The rotated piece starts on the bottom most line of the current piece
		for (int i=0; i<newLine.length; i++)
		{
			newLine[i]=line[0]-i;
		}
		fit(newNumBlocks, newStart, newLine);
	}
	
	//Makes the rotated piece fit within the board, if possible
	public void fit(int[] n, int[] s, int[] l)
	{
		int spaces=0;
		//Adjusts the piece left and right to right to fit it in the boundaries and in between the other pieces
		for (int i=0; i<n.length; i++)
		{
			if (s[i]+n[i]-1>9&&9-(s[i]+n[i]-1)<spaces)
				spaces=9-(s[i]+n[i]-1);
			for (int j=0; j<n[i]; j++)
				if (l[i]>-1&&!(s[i]+j>9))
					if (!(t.getBlocks()[l[i]][s[i]+j].equals(Color.black))&&s[i]+j-(s[i]+n[i])<spaces)
						spaces=s[i]+j-(s[i]+n[i]);
		}
		//If the rotation can fit into the board then sets it, if not then the piece does not rotate
		if (t.willWork(spaces, 0, n, s, l))
		{
			for (int i=0; i<s.length; i++)
				s[i]+=spaces;
			numBlocks=n;
			start=s;
			line=l;
		}
	}
	
	//The setters and getters
	public void setNumBlocks(int[] numBlocks) 
	{
		this.numBlocks = numBlocks;
	}

	public int[] getNumBlocks() 
	{
		return numBlocks;
	}

	public void setStart(int[] start) 
	{
		this.start = start;
	}

	public int[] getStart() 
	{
		return start;
	}

	public void setLine(int[] line) 
	{
		this.line = line;
	}

	public int[] getLine() 
	{
		return line;
	}
	public void setColor(Color c)
	{
		color=c;
	}
	
	public Color getColor()
	{
		return color;
	}

	public void setMoving(boolean m)
	{
		moving=m;
	}
	
	public boolean isMoving()
	{
		return moving;
	}
	
	public int getId()
	{
		return id;
	}

	public void setTetris(Tetris t)
	{
		this.t=t;
	}

	public void setInfo(int[] n, int s[], int[] l) 
	{
		numBlocks=new int[n.length];
		start=new int[s.length];
		line=new int[l.length];
		System.arraycopy(n, 0, numBlocks, 0, n.length);
		System.arraycopy(s, 0, start, 0, s.length);
		System.arraycopy(l, 0, line, 0, l.length);
	}
}