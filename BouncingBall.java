/*************************************************************************
 *  Compilation:  javac BouncingBall.java
 *  Execution:    java BouncingBall
 *  Dependencies: StdDraw.java
 *
 *  Implementation of a 2-d bouncing ball in the box from (-1, -1) to (1, 1).
 *
 *  % java BouncingBall
 *
 *************************************************************************/

public class BouncingBall { 
	public static void main(String[] args) {

		// set the scale of the coordinate system
		StdDraw.setXscale(-1.0, 1.0);
		StdDraw.setYscale(-1.0, 1.0);

		// initial values
		double rx = 0.480, ry = 0.860;     // position
		double vx = 0.015, vy = 0.023;     // velocity
		double radius = 0.1;              // radius
		double rX[]=new double [3];
		double rY[]=new double [3];
		// main animation loop
		while (true)  { 

			// bounce off wall according to law of elastic collision
			if (Math.abs(rx + vx) > 1.0 - radius) vx = -vx;
			if (Math.abs(ry + vy) > 1.0 - radius) vy = -vy;

			// update position
			rx = rx + vx; 
			ry = ry + vy; 

			// clear the background
			StdDraw.setPenColor(StdDraw.BOOK_LIGHT_BLUE);
			StdDraw.filledSquare(0, 0, 1.2);

			// draw ball on the screen
			rX[0]=rx-radius;
			rY[0]=ry+radius;
			rX[1]=rx+radius;
			rY[1]=ry+radius;
			rX[2]=rx;
			rY[2]=ry+3*radius-.05;
			StdDraw.setPenColor(StdDraw.RED); 
			StdDraw.filledPolygon(rX,rY); 
			//StdDraw.picture(rx, ry, "house.png");
			StdDraw.setPenColor(StdDraw.YELLOW); 
			StdDraw.filledRectangle(rx, ry, radius, radius); 
			StdDraw.setPenColor(StdDraw.BOOK_BLUE);
			StdDraw.filledRectangle(rx, ry-radius/3-.01, radius/4, radius/2); 
			StdDraw.filledRectangle(rx-2*radius/5, ry+3*radius/5, radius/4, radius/5); 
			StdDraw.filledRectangle(rx+2*radius/5, ry+3*radius/5, radius/4, radius/5); 
			
			

			// display and pause for 20 ms
			StdDraw.show(20); 
		} 
	} 
} 