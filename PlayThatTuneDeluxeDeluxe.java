
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

	// read in notes from standard input and play them on standard audio
	public static void main(String[] args) {
		int i=-1;
		StdDraw.setCanvasSize(600,900);
		StdDraw.setXscale(0,200);
		StdDraw.setYscale(-100,200);
		while (!StdIn.isEmpty()) {
			int pitch = StdIn.readInt();
			double duration = StdIn.readDouble();
			double[] a = note(pitch, duration);
			StdAudio.play(a);
			i++;
			//double [] a = note(pitch, duration);
			//StdAudio.play(a);
			StdDraw.setPenColor((int)(map((float)duration,0,2,0,255)),168+(int)(map((float)duration,0.0f,2.0f,0.0f,86.0f)),255);
			StdDraw.filledSquare(100, 100, 110);
			
			StdDraw.setPenColor(StdDraw.BLACK); 
			for (int r=40; r<=100; r+=20) StdDraw.circle(100, 100, r);
			
			StdDraw.setPenColor(0,(int)map(pitch,0,22,100,255),100);
			StdDraw.circle(100, 100, (map(pitch,0,22,50,80)));
			//StdDraw.setPenColor(StdDraw.RED); 
			//StdDraw.filledCircle(100+50*Math.cos(pitch*10), 100+50*Math.sin(pitch*10), 20);
			StdDraw.filledCircle(100+(map(pitch,0,22,50,80))*Math.cos(pitch*10), 100+(map(pitch,0,22,50,80))*Math.sin(pitch*10), 20);
			if (Math.cos(pitch*10)>=0) StdDraw.picture(100, 100, "tigerstandright.png");
			else StdDraw.picture(100, 100, "tigerstandleft.png");
			drawNotes(i,duration,pitch);
		}
		StdDraw.setPenColor(StdDraw.BOOK_LIGHT_BLUE);
		StdDraw.filledSquare(100, 100, 110);
		StdDraw.setPenColor(StdDraw.BLACK); 
		for (int r=40; r<=100; r+=20) StdDraw.circle(100, 100, r);
		StdDraw.picture(100, 100, "tigerwaving.png");
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

	public static float map(float num, float cmin, float cmax, float nmin, float nmax){
		float prop=(num-cmin)/(cmax-cmin);
		float newProp=nmin+(prop*(nmax-nmin));
		return newProp;
	}

	public static void drawNotes(int i, double duration, int pitch){
		Integer notePos[]={0,0,1,2,2,3,3,4,5,5,6,6,0,0,1,2,2,3,3,4,5,5,6};//23, 0-22
		int startX=25;
		float startY=(float)-42.5;
		int topBotDiff=45;
		int dist=12;
		String notePic="quarternote.png";

		if (i%30==0){
			StdDraw.setPenColor(StdDraw.WHITE); 
			StdDraw.filledRectangle(100, startY-12.5, 100, 45);
		}
		StdDraw.setPenColor(StdDraw.BLACK); 
		StdDraw.line(0, startY+12.5, 0, startY-7.5);
		for (int a=-30;a>-55; a-=5){
			StdDraw.line(0, a, 200, a);
		}
		StdDraw.picture(9, startY, "treble.png",15,40);

		StdDraw.line(0, startY-32.5, 0, startY-52.5);
		for (int a=-75;a>-100; a-=5){
			StdDraw.line(0, a, 200, a);
		}
		StdDraw.picture(9, startY-45, "treble.png",15,40);

		if (i%30<15){//top
			if (pitch<2){
				notePic="quarternote.png";
				if (pitch==0){
					StdDraw.picture(startX+dist*(i%15), startY+5, notePic,8,14);
				}
				else if (pitch==1){
					StdDraw.text(startX-4+dist*(i%15), startY-.5, "\u0023");
					StdDraw.picture(startX+.5+dist*(i%15), startY+5, notePic,8,14);
				}
			}
			else if (pitch==2 || pitch==3 || pitch==5 || pitch==7 || 
					pitch==8 || pitch==10){
				notePic="quarternotedown.png";
				StdDraw.picture(startX+dist*(i%15), startY-5+2.5*notePos[pitch], notePic,8,14);
			}
			else if (pitch==12 || pitch==14 || pitch==15 || pitch==17 || pitch==19 || 
					pitch==20 || pitch==22){
				notePic="quarternotedown.png";
				for(int b=-30;b<=(-25+2.5*(notePos[pitch-12]));b+=5) StdDraw.line(startX-5+dist*(i%15), b, startX+5+dist*(i%15), b);
				StdDraw.picture(startX+dist*(i%15), startY-5+17.5+2.5*notePos[pitch], notePic,8,14);
			}
			else if (pitch==4 || pitch==6 || pitch==9 || pitch==11){
				notePic="quarternotedown.png";
				StdDraw.text(startX-4+dist*(i%15), -43+2.5*notePos[pitch], "\u0023");
				StdDraw.picture(startX+.5+dist*(i%15), startY-5+2.5*notePos[pitch], notePic,8,14);
			}
			else if (pitch==13 || pitch==16 || pitch==18 || pitch==21 || pitch==23){
				notePic="quarternotedown.png";
				for(int b=-30;b<=(-25+2.5*(notePos[pitch-12]));b+=5) StdDraw.line(startX-5+dist*(i%15), b, startX+5+dist*(i%15), b);
				StdDraw.text(startX-4+dist*(i%15), startY-.5+17.5+2.5*notePos[pitch], "\u0023");
				StdDraw.picture(startX+.5+dist*(i%15), startY-5+17.5+2.5*notePos[pitch], notePic,8,14);
			}
		}

		else if (i%30<30){//bottom
			if (pitch<2){
				notePic="quarternote.png";
				if (pitch==0){
					StdDraw.picture(startX+dist*(i%15), startY-topBotDiff+5, notePic,8,14);
				}
				else if (pitch==1){
					StdDraw.text(startX-4+dist*(i%15), startY-.5-topBotDiff, "\u0023");
					StdDraw.picture(startX+.5+dist*(i%15), startY-topBotDiff+5, notePic,8,14);
				}
			}
			else if (pitch==2 || pitch==3 || pitch==5 || pitch==7 || 
					pitch==8 || pitch==10){
				notePic="quarternotedown.png";
				StdDraw.picture(startX+dist*(i%15), startY-5-topBotDiff+2.5*notePos[pitch], notePic,8,14);
			}
			else if (pitch==12 || pitch==14 || pitch==15 || pitch==17 || pitch==19 || 
					pitch==20 || pitch==22){
				notePic="quarternotedown.png";
				for(int b=-75;b<=(-70+2.5*(notePos[pitch-12]));b+=5) StdDraw.line(startX-5+dist*(i%15), b, startX+5+dist*(i%15), b);
				StdDraw.picture(startX+dist*(i%15), startY-5+17.5-topBotDiff+2.5*notePos[pitch], notePic,8,14);
			}
			else if (pitch==4 || pitch==6 || pitch==9 || pitch==11){
				notePic="quarternotedown.png";
				StdDraw.text(startX-4+dist*(i%15), startY-.5-topBotDiff+2.5*notePos[pitch], "\u0023");
				StdDraw.picture(startX+.5+dist*(i%15), startY-5-topBotDiff+2.5*notePos[pitch], notePic,8,14);
			}
			else if (pitch==13 || pitch==16 || pitch==18 || pitch==21 || pitch==23){
				notePic="quarternotedown.png";
				for(int b=-75;b<=(-70+2.5*(notePos[pitch-12]));b+=5) StdDraw.line(startX-5+dist*(i%15), b, startX+5+dist*(i%15), b);
				StdDraw.text(startX-4+dist*(i%15), startY-.5+17.5-topBotDiff+2.5*notePos[pitch], "\u0023");
				StdDraw.picture(startX+.5+dist*(i%15), startY-5+17.5-topBotDiff+2.5*notePos[pitch], notePic,8,14);
			}
		}
		//			StdDraw.line(0, -75, 0, -95);
		//			for (int a=-75;a>-100; a-=5){
		//				StdDraw.line(0, a, 200, a);
		//			}
		//			StdDraw.picture(10, -85, "bass.png",15,20);
		StdDraw.show(20); 
	}

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

} 