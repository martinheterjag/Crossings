package se.kth.martine7;

import java.awt.Color;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Random;

public class Car {

	private int speed;
	private int x, y, dx, dy;
	private Color color;
	private Direction direction;
	private StopLight stopLight;
	private boolean copCar;
	private Rectangle driversView;
	private ArrayList<Car> allOtherCars;
	
	public Car(int x, int y, Direction direction, StopLight[] stopLight, boolean copCar, ArrayList<Car> allOtherCars)
	{
		
		this.allOtherCars = allOtherCars;
		
		this.direction = direction;
		
		this.copCar = copCar;
		this.x = x;
		this.y = y;
		this.speed = 4;
		
		switch(direction)
		{
			case NORTH:
				dx=0;
				dy=-speed;
				this.stopLight = stopLight[Direction.NORTH.ordinal()];
				break;
			case SOUTH: 
				dx=0;
				dy=speed;
				this.stopLight = stopLight[Direction.SOUTH.ordinal()];
				break;
			case EAST: 
				dx=speed;
				dy=0;
				this.stopLight = stopLight[Direction.EAST.ordinal()];
				break;
			case WEST:
				dx=-speed;
				dy=0;
				this.stopLight = stopLight[Direction.WEST.ordinal()];
				break;
		}
		updateDriversView();
		
		Random rand = new Random();
		this.color = new Color(rand.nextInt(255),rand.nextInt(255),rand.nextInt(255));
	}
	
	private void updateDriversView()
	{
		switch(direction)
		{
			case NORTH:
				driversView = new Rectangle(x-10, y-25, 20, 20);
				break;
			case SOUTH: 
				driversView = new Rectangle(x-10, y+5, 20, 20);
				break;
			case EAST: 
				driversView = new Rectangle(x+5, y-10, 20, 20);
				break;
			case WEST:
				driversView = new Rectangle(x-25, y-10, 20, 20);
				break;
		}
	}
	


	public Rectangle getDriversView() {
		return driversView;
	}



	/**
	 * updates the position of the car
	 * 
	 */
	public void updatePosition()
	{

		if(!closeToOtherCar())
		{
			if(redLight())
			{
				stopAtLight();
			}else{
				x = x + dx;
				y = y + dy;
			}
			updateDriversView();
		}
	}

	private boolean redLight()
	{
		if(stopLight.getColor() == Color.GREEN)
		{
			return false;
		}
		
		return true;
	}
	
	private void stopAtLight()
	{
		switch(direction)
		{
			case NORTH:
				if(!(y == 300))
				{
					x = x + dx;
					y = y + dy;
				}
				break;
				
			case SOUTH: 
				if(!(y == 200))
				{
					x = x + dx;
					y = y + dy;
				}
				break;
			case EAST: 
				if(!(x == 200))
				{
					x = x + dx;
					y = y + dy;
				}
				break;
			case WEST:
				if(!(x == 300))
				{
					x = x + dx;
					y = y + dy;
				}
				break;
		}
		
	}
	
	public boolean closeToOtherCar()
	{
		for(Car c : allOtherCars)
		{
			if(driversView.contains(c.getX(), c.getY()))
			{
				return true;
			}

		}
		return false;

	}


	public int getSpeed() {
		return speed;
	}


	public void setSpeed(int speed) {
		this.speed = speed;
	}


	public int getX() {
		return x;
	}


	public void setX(int x) {
		this.x = x;
	}


	public int getY() {
		return y;
	}


	public void setY(int y) {
		this.y = y;
	}


	public int getDx() {
		return dx;
	}


	public void setDx(int dx) {
		this.dx = dx;
	}


	public int getDy() {
		return dy;
	}


	public void setDy(int dy) {
		this.dy = dy;
	}
	
	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}



	public boolean isCop() {
		if(copCar)
			return true;
		
		return false;
	}



	
	
}
