package se.kth.martine7;

import java.awt.Color;

public class StopLight {
	
	private Status color;
	
	public StopLight(Status color)
	{
		this.color = color;
		
	}

	public void setColor(Status color)
	{
		this.color = color;
	}
	
	public Color getColor()
	{
		Color color = Color.BLACK;
		
		switch(this.color)
		{
		case RED:
			color = Color.RED;
			break;
		case YELLOW:
			color = Color.YELLOW;
			break;
		case GREEN:
			color = Color.GREEN;
			break;
		}
		
		return color;
	}
	
	
	
	
	
}
