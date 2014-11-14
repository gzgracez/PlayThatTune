
/*************************************************************************
 *  Compilation:  javac PlayThatTuneDeluxe.java
 *  Execution:    java PlayThatTuneDeluxe < data.txt
 *
 *  % java PlayThatTuneDeluxe 0.5 < elise.txt
 *
 *  Data files
 *  ----------
 *  http://www.cs.princeton.edu/introcs/21function/elise.txt
 *  http://www.cs.princeton.edu/introcs/21function/freebird.txt
 *  http://www.cs.princeton.edu/introcs/21function/Ascale.txt
 *  http://www.cs.princeton.edu/introcs/21function/National_Anthem.txt
 *  http://www.cs.princeton.edu/introcs/21function/looney.txt
 *  http://www.cs.princeton.edu/introcs/21function/StairwayToHeaven.txt
 *  http://www.cs.princeton.edu/introcs/21function/entertainer.txt
 *  http://www.cs.princeton.edu/introcs/21function/old-nassau.txt
 *  http://www.cs.princeton.edu/introcs/21function/arabesque.txt
 *  http://www.cs.princeton.edu/introcs/21function/firstcut.txt
 *  http://www.cs.princeton.edu/introcs/21function/tomsdiner.txt
 *  http://www.cs.princeton.edu/introcs/21function/portal.txt
 *
 *************************************************************************/

public class PlayThatTuneDeluxeDeluxe {

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
		System.out.println(pitch1);
		// read in pitch-duration pairs from standard input
		while (!StdIn.isEmpty()) {
			int pitch = StdIn.readInt();
			double duration = StdIn.readDouble();
			double[] a = note(pitch, duration);
			StdAudio.play(a);
			pitch1=pitch;
			StdDraw.setXscale(0,200);
			StdDraw.setYscale(0,200);
			StdDraw.setPenColor((int)(duration*100),168+(int)(duration*50),255);
			StdDraw.filledSquare(100, 100, 120);
			StdDraw.setPenColor(StdDraw.RED); 
			//StdDraw.filledCircle(pitch1*10, 100, (Math.abs(pitch1)/10.0)+40);
			//StdDraw.filledCircle(100+50*Math.sin(0), 100+50*Math.cos(0), 20);
			StdDraw.filledCircle(100+50*Math.sin(pitch1*10), 100+50*Math.cos(pitch1*10), 20);
			StdDraw.show(); 
			map(2,1,3,1,10);
			//System.out.println((Math.abs(pitch1)/10.0)+40);
		}
		
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