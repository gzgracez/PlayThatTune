import java.io.*;
import java.util.Scanner;
public class pttdrawonly {

	// take weighted sum of two arrays
	public static double[] sum(double[] a, double[] b, double awt, double bwt) {

		// precondition: arrays have the same length
		assert (a.length == b.length);

		// compute the weighted sum
		double[] c = new double[a.length];
		for (int i = 0; i < a.length; i++) {
			c[i] = a[i]*awt + b[i]*bwt;
		}
		return c;
	} 

	// create a pure tone of the given frequency for the given duration
	public static double[] tone(double hz, double duration) { 
		int N = (int) (StdAudio.SAMPLE_RATE * duration);
		double[] a = new double[N+1];
		for (int i = 0; i <= N; i++) {
			a[i] = Math.sin(2 * Math.PI * i * hz / StdAudio.SAMPLE_RATE);
		}
		return a; 
	} 

	// create a note with harmonics of of the given pitch, where 0 = concert A
	public static double[] note(int pitch, double t) {
		double hz = 440.0 * Math.pow(2, pitch / 12.0);
		double[] a  = tone(hz, t);
		double[] hi = tone(2*hz, t);
		double[] lo = tone(hz/2, t);
		double[] h  = sum(hi, lo, .5, .5);
		return sum(a, h, .5, .5);
	}

	// read in notes from standard input and play them on standard audio
	public static void main(String[] args) {
		int pitch1=0;
		Integer pitch[]={12,5,3,15,19,3,12,5,12,7,15,19,3,3,3,19,15,3,17,22,5,3,15,10,12,10,7,5,12,19};
		Double duration[]={0.375,0.125,0.375,0.0,0.375,0.375,0.75,0.75,0.125,0.75,0.375,0.375,0.0,0.125,0.125,0.375,0.375,0.125,0.125,0.375,0.0,0.75,0.0,0.75,0.375,0.125,0.0,0.0,0.125,0.375};
		//System.out.println(pitch1);
		// read in pitch-duration pairs from standard input
		StdDraw.setCanvasSize(600,900);
		for (int i=0;i<pitch.length;i++){
			double[] a = note(pitch[i], duration[i]);
			//StdAudio.play(a);
			pitch1=pitch[i];
			StdDraw.setXscale(0,200);
			StdDraw.setYscale(-100,200);
			StdDraw.setPenColor((int)(duration[i]*100),168+(int)(duration[i]*50),255);
			StdDraw.filledSquare(100, 100, 120);
			StdDraw.setPenColor(StdDraw.RED); 
			//StdDraw.filledCircle(pitch1*10, 100, (Math.abs(pitch1)/10.0)+40);
			//StdDraw.filledCircle(100+50*Math.sin(0), 100+50*Math.cos(0), 20);
			StdDraw.filledCircle(100+50*Math.sin(pitch1*10), 100+50*Math.cos(pitch1*10), 20);
			StdDraw.show(600); 
			map(2,1,3,1,10);
			//System.out.println((Math.abs(pitch1)/10.0)+40);
		}
		StdDraw.show(); 
		/*
		StdDraw.setXscale();
		StdDraw.setYscale();
		StdDraw.circle(0, 0, pitch1*100);
		StdDraw.show(20); 
		*/
		
		// needed to terminate program - known Java bug
		System.exit(0);
	} 
	
	public static void map(float num, float cmin, float cmax, float nmin, float nmax){
		float prop=(num-cmin)/(cmax-cmin);
		float newProp=nmin+(prop*(nmax-nmin));
		//return newProp;
	}
}
