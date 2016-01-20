package se.kth.martine7;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

import se.kth.martine7.Car;
import se.kth.martine7.Direction;
import se.kth.martine7.Status;
import se.kth.martine7.StopLight;

public class JunctionPanel extends JPanel implements ActionListener{
	
	private final int height = 500, width = 500;
	private final int roadWidth = 100;
	
	private Random rand = new Random();
	
	private ArrayList<Car> cars;
	private Timer timer;
	private JFrame frame;
	private Rectangle verticalRoad, horizontalRoad;

	private StopLight[] stopLights = new StopLight[4];

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public JunctionPanel(JFrame frame)
	{
		

		this.frame = frame;
		this.setBackground(new Color(0.0f,0.4f,0.05f));
		this.setPreferredSize(new Dimension(width,height));
		
		cars = new ArrayList<>();
		
		stopLights[Direction.SOUTH.ordinal()] = new StopLight(Status.RED);
		stopLights[Direction.NORTH.ordinal()] = new StopLight(Status.RED);
		stopLights[Direction.EAST.ordinal()] = new StopLight(Status.GREEN);
		stopLights[Direction.WEST.ordinal()] = new StopLight(Status.GREEN);
		initRoad();
		initStopLight();
		
		new Thread(new ControlStoplights(stopLights)).start();
		
		new Thread(new SpawnCars(cars, Direction.SOUTH)).start();
		new Thread(new SpawnCars(cars, Direction.NORTH)).start();

		new Thread(new SpawnCars(cars, Direction.EAST)).start();
		new Thread(new SpawnCars(cars, Direction.WEST)).start();
	
		
	}

	/**
	 * start the animation
	 */
	public void startAnimation()
	{
		timer = new Timer(10, this);
		timer.start();
	}
	
	/**
	 * initializes the road positions
	 */
	private void initRoad()
	{
		verticalRoad = new Rectangle(height/2-roadWidth/2,0,roadWidth,height);
		horizontalRoad = new Rectangle(0,width/2-roadWidth/2,width,roadWidth);
	}
	
	/**
	 * initializes the stop lights positions
	 */
	private void initStopLight()
	{
	}
	
	

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		
		// PAINT ROADS
		g.setColor(Color.lightGray);
		g.fillRect(verticalRoad.x, verticalRoad.y, verticalRoad.width, verticalRoad.height);
		g.fillRect(horizontalRoad.x, horizontalRoad.y, horizontalRoad.width, horizontalRoad.height);
		
		g.setColor(Color.white);
		g.fillRect(height/2-1,0,2,200);
		g.fillRect(0,width/2-1,200,2);

		g.fillRect(height/2-1,300,2,200);
		g.fillRect(300,width/2-1,200,2);
	
		// PAINT STOPLIGHTS
		g.setColor(stopLights[Direction.SOUTH.ordinal()].getColor());
		for(int i = 0; i < 50; i+=20)
		{
			int[] x_vector = {200+i,210+i,205+i};
			int[] y_vector = {200,200,210};
			g.fillPolygon(x_vector, y_vector , 3);
		}

		for(int i = 0; i < 50; i+=20)
		{
			g.setColor(stopLights[Direction.NORTH.ordinal()].getColor());
			int[] x_vector = {300-i,295-i,290-i};
			int[] y_vector = {300,290,300};
			g.fillPolygon(x_vector, y_vector , 3);
		}
		
		for(int i = 0; i < 50; i+=20)
		{
		g.setColor(stopLights[Direction.EAST.ordinal()].getColor());
		int[] x_vector = {300,290,300};
		int[] y_vector = {200+i,205+i,210+i};
		g.fillPolygon(x_vector, y_vector , 3);
		}
		
		for(int i = 0; i < 50; i+=20)
		{
		g.setColor(stopLights[Direction.WEST.ordinal()].getColor());
		int[] x_vector = {200,200,210};
		int[] y_vector = {300-i,290-i,295-i};
		g.fillPolygon(x_vector, y_vector , 3);
		}
		
		// PAINT CARS
		synchronized(cars)
		{
			if(cars.size() != 0)
			{
				
				for(Car c : cars)
				{
					if(c.isCop())
					{
						
						g.setColor(Color.WHITE);
						g.fillRect(c.getX()-10, c.getY()-10, 20, 20);
						if(c.getDx() == 0) //moving up/down
						{
							g.setColor(Color.CYAN);
							g.fillRect(c.getX()-10, c.getY()-2, 9, 4);
							g.fillRect(c.getX()+1, c.getY()-2, 9, 4);
						}else
						{
							g.setColor(Color.CYAN);
							g.fillRect(c.getX()-2, c.getY()-10, 4, 9);
							g.fillRect(c.getX()-2, c.getY()+1, 4, 9);
							
							
						}

					}
					else
					{
						g.setColor(c.getColor());
						g.fillRect(c.getX()-5, c.getY()-5, 10, 10);
						//paint drivers view
						//g.drawRect(c.getDriversView().x, c.getDriversView().y, c.getDriversView().width, c.getDriversView().height );
					}

				}
			}
		}
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		ArrayList<Car> carsOutOfFrame = new ArrayList<>();
		if(cars.size() != 0){
			synchronized(cars)
			{
				for(Car c : cars)
				{
					c.updatePosition();
					if(c.getX() < 0 || c.getX() > width|| c.getY() < 0 || c.getY() > height)
					{	
						carsOutOfFrame.add(c);
					}
				}
				cars.removeAll(carsOutOfFrame);
			}
		}
		repaint();
	}
	
	
	
	/**
	 * this class spawn cars on one of the four roads going in the direction entered.
	 * @author Martin
	 *
	 */
	class SpawnCars implements Runnable
	{
		private ArrayList<Car> cars;
		private Direction dir;
		private int x,y;
		private StopLight stopLight;
		
		public SpawnCars(ArrayList<Car> cars, Direction dir)
		{
			this.dir = dir;
			this.cars = cars;
			generateSpawnPoint();
		}

		@Override
		public void run() {
			boolean running = true;
			while(running){	
				if(rand.nextInt(50) == 0)
					{
						synchronized(cars)
						{
							if(rand.nextInt(30) == 0)
								cars.add(new Car(x, y, dir, stopLights, true, cars));
							else
								cars.add(new Car(x, y, dir, stopLights, false, cars));
						}
					}
					
				
				try {
					Thread.sleep(20);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		
		
		// makes the cars spawn at correct place with correct direction and look at the correct stop light
		private void generateSpawnPoint()
		{
			switch(dir)
			{
				case NORTH:
					x =	width/2 + 25;
					y = height;
					stopLight = stopLights[Direction.NORTH.ordinal()];
					break;
				case SOUTH:
					x =	width/2 - 25;
					y = 0;
					stopLight = stopLights[Direction.SOUTH.ordinal()];
					break;
				case EAST:
					x = 0;
					y = height/2 + 25;
					stopLight = stopLights[Direction.EAST.ordinal()];
					break;
				case WEST:
					x = width;
					y = height/2 - 25;
					stopLight = stopLights[Direction.WEST.ordinal()];
					break;
			}
		}
	}
	
	
	class ControlStoplights implements Runnable
	{

		private StopLight[] stopLights = new StopLight[4];
		
		public ControlStoplights(StopLight[] stopLights)
		{
			this. stopLights = stopLights;
		}
		@Override
		public void run() {
			boolean running = true;
			boolean verticalTrafic = true;
			int time = 0;
			boolean horizontalPriority = true;
			while(running)
			{
				
				try 
				{
					Thread.sleep(10);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				time++;
				if( time > 2000)
				{
					time = 0;
				}
				
				if(time > 1000)
				{
					horizontalPriority = true;
				}
				else
				{
					horizontalPriority = false;
				}
				

				
				if(horizontalCongestion())
				{
					System.out.println("congestion! ");
					horizontalPriority = true;
				}
				else if(verticalCongestion())
				{
					System.out.println("congestion! ");
					horizontalPriority = false;
				}
				
				
				if(horizontalCop())
				{
					System.out.println("cop! ");
					horizontalPriority = true;
				}
				else if(verticalCop())
				{
					System.out.println("cop! ");
					horizontalPriority = false;
				}
				
				
 
				if(horizontalPriority)
				{
					if(verticalTrafic)
					{
						yellow();
					}
					horizontalTrafic();
					verticalTrafic = false;
					
				}
				else
				{
					if(!verticalTrafic)
					{
						yellow();
					}
					verticalTrafic();
					verticalTrafic = true;
				}
			}
		}
		
		private synchronized void horizontalTrafic()
		{
		stopLights[Direction.NORTH.ordinal()].setColor(Status.RED);
		stopLights[Direction.SOUTH.ordinal()].setColor(Status.RED);
		stopLights[Direction.EAST.ordinal()].setColor(Status.GREEN);
		stopLights[Direction.WEST.ordinal()].setColor(Status.GREEN);
		}
		
		private synchronized void verticalTrafic()
		{
			stopLights[Direction.NORTH.ordinal()].setColor(Status.GREEN);
			stopLights[Direction.SOUTH.ordinal()].setColor(Status.GREEN);
			stopLights[Direction.EAST.ordinal()].setColor(Status.RED);
			stopLights[Direction.WEST.ordinal()].setColor(Status.RED);
		}
		
		private synchronized void yellow()
		{
			stopLights[Direction.NORTH.ordinal()].setColor(Status.YELLOW);
			stopLights[Direction.SOUTH.ordinal()].setColor(Status.YELLOW);
			stopLights[Direction.EAST.ordinal()].setColor(Status.YELLOW);
			stopLights[Direction.WEST.ordinal()].setColor(Status.YELLOW);
			try 
			{
				Thread.sleep(500);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
		}
		
		
		private boolean horizontalCop()
		{
			synchronized(cars)
			{
				for(Car c : cars)
				{
					if(c.isCop() && c.getDy() == 0)
					{
						//System.out.println("horizontal cop!!!");
						return true;
					}
				}
			}
			return false;	
		}
		
		private boolean verticalCop()
		{
			synchronized(cars)
			{
				for(Car c : cars)
				{
					if(c.isCop() && c.getDx() == 0)
					{
						//System.out.println("vertical cop!!!");
						return true;
					}
				}
			}
			return false;	
		}
		
		
		private boolean horizontalCongestion()
		{
			int count = 0;
			synchronized(cars)
			{
				for(Car c : cars)
				{
					if(c.getDx() != 0)
					{
						count++;
					}
				}
			}
			if(count>10)
			{
				return true;
			}
			
			return false;
		}
		
		private boolean verticalCongestion()
		{
			int count = 0;
			synchronized(cars)
			{
				for(Car c : cars)
				{
					if(c.getDy() != 0)
					{
						count++;
					}
				}
			}
			if(count>10)
			{
				return true;
			}
			
			return false;
		}
		
		
		
	}
	
	
	
	
	
	
}
