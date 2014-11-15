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
		Integer pitch[]={0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,22,0,1,2,3,4,5,6,7,8,9,10};
		//Integer pitch[]={12,5,3,15,19,3,12,5,12,7,15,19,3,3,3,19,15,3,17,22,5,3,15,10,12,10,7,5,12,19};
		Double duration[]={0.375,0.125,0.375,0.0,0.375,0.375,0.75,0.75,0.125,0.75,0.375,0.375,0.0,0.125,0.125,0.375,0.375,0.125,0.125,0.375,0.0,0.75,0.0,0.75,0.375,0.125,0.0,0.0,0.125,0.375,0.375,0.375,0.375,0.375,0.375};
		Integer notePos[]={0,0,1,2,2,3,3,4,5,5,6,6,0,0,1,2,2,3,3,4,5,5,6};//23, 0-22
		//System.out.println(pitch1);
		// read in pitch-duration pairs from standard input
		StdDraw.setCanvasSize(600,900);
		for (int i=0;i<pitch.length;i++){
			//double[] a = note(pitch[i], duration[i]);
			//StdAudio.play(a);
			StdDraw.setXscale(0,200);
			StdDraw.setYscale(-100,200);
			StdDraw.setPenColor((int)(duration[i]*100),168+(int)(duration[i]*50),255);
			StdDraw.filledSquare(100, 100, 110);
			if (i%30==0){
				StdDraw.setPenColor(StdDraw.WHITE); 
				StdDraw.filledRectangle(100, -55, 100, 45);
			}
			StdDraw.setPenColor(StdDraw.BLACK); 
			StdDraw.line(0, -30, 0, -50);
			for (int a=-30;a>-55; a-=5){
				StdDraw.line(0, a, 200, a);
			}
			int dist=12;
			int start=25;
			int topBotDiff=45;
			String notePic="quarternote.png";
			StdDraw.picture(9, -42.5, "treble.png",15,40);

			StdDraw.line(0, -75, 0, -95);
			for (int a=-75;a>-100; a-=5){
				StdDraw.line(0, a, 200, a);
			}
			StdDraw.picture(9, -87.5, "treble.png",15,40);

			if (i%30<15){//top
				if (pitch[i]<2){
					notePic="quarternote.png";
					if (pitch[i]==0){
						StdDraw.picture(start+dist*(i%15), -42.5+5, notePic,8,14);
					}
					else if (pitch[i]==1){
						StdDraw.text(start-4+dist*(i%15), -43, "\u0023");
						StdDraw.picture(start+.5+dist*(i%15), -42.5+5, notePic,8,14);
					}
				}
				else if (pitch[i]==2 || pitch[i]==3 || pitch[i]==5 || pitch[i]==7 || 
						pitch[i]==8 || pitch[i]==10){
					notePic="quarternotedown.png";
					StdDraw.picture(start+dist*(i%15), -42.5-5+2.5*notePos[pitch[i]], notePic,8,14);
				}
				else if (pitch[i]==12 || pitch[i]==14 || pitch[i]==15 || pitch[i]==17 || pitch[i]==19 || 
						pitch[i]==20 || pitch[i]==22){
					notePic="quarternotedown.png";
					for(int b=-30;b<=(-25+2.5*(notePos[pitch[i]-12]));b+=5) StdDraw.line(start-5+dist*(i%15), b, start+5+dist*(i%15), b);
					StdDraw.picture(start+dist*(i%15), -42.5-5+17.5+2.5*notePos[pitch[i]], notePic,8,14);
				}
				else if (pitch[i]==4 || pitch[i]==6 || pitch[i]==9 || pitch[i]==11){
					notePic="quarternotedown.png";
					StdDraw.text(start-4+dist*(i%15), -43+2.5*notePos[pitch[i]], "\u0023");
					StdDraw.picture(start+.5+dist*(i%15), -42.5-5+2.5*notePos[pitch[i]], notePic,8,14);
				}
				else if (pitch[i]==13 || pitch[i]==16 || pitch[i]==18 || pitch[i]==21 || pitch[i]==23){
					notePic="quarternotedown.png";
					for(int b=-30;b<=(-25+2.5*(notePos[pitch[i]-12]));b+=5) StdDraw.line(start-5+dist*(i%15), b, start+5+dist*(i%15), b);
					StdDraw.text(start-4+dist*(i%15), -43+17.5+2.5*notePos[pitch[i]], "\u0023");
					StdDraw.picture(start+.5+dist*(i%15), -42.5-5+17.5+2.5*notePos[pitch[i]], notePic,8,14);
				}
			}

			else if (i%30<30){//bottom
				if (pitch[i]<2){
					notePic="quarternote.png";
					if (pitch[i]==0){
						StdDraw.picture(start+dist*(i%15), -42.5-topBotDiff+5, notePic,8,14);
					}
					else if (pitch[i]==1){
						StdDraw.text(start-4+dist*(i%15), -43-topBotDiff, "\u0023");
						StdDraw.picture(start+.5+dist*(i%15), -42.5-topBotDiff+5, notePic,8,14);
					}
				}
				else if (pitch[i]==2 || pitch[i]==3 || pitch[i]==5 || pitch[i]==7 || 
						pitch[i]==8 || pitch[i]==10){
					notePic="quarternotedown.png";
					StdDraw.picture(start+dist*(i%15), -42.5-5-topBotDiff+2.5*notePos[pitch[i]], notePic,8,14);
				}
				else if (pitch[i]==12 || pitch[i]==14 || pitch[i]==15 || pitch[i]==17 || pitch[i]==19 || 
						pitch[i]==20 || pitch[i]==22){
					notePic="quarternotedown.png";
					for(int b=-75;b<=(-70+2.5*(notePos[pitch[i]-12]));b+=5) StdDraw.line(start-5+dist*(i%15), b, start+5+dist*(i%15), b);
					StdDraw.picture(start+dist*(i%15), -42.5-5+17.5-topBotDiff+2.5*notePos[pitch[i]], notePic,8,14);
				}
				else if (pitch[i]==4 || pitch[i]==6 || pitch[i]==9 || pitch[i]==11){
					notePic="quarternotedown.png";
					StdDraw.text(start-4+dist*(i%15), -43-topBotDiff+2.5*notePos[pitch[i]], "\u0023");
					StdDraw.picture(start+.5+dist*(i%15), -42.5-5-topBotDiff+2.5*notePos[pitch[i]], notePic,8,14);
				}
				else if (pitch[i]==13 || pitch[i]==16 || pitch[i]==18 || pitch[i]==21 || pitch[i]==23){
					notePic="quarternotedown.png";
					for(int b=-75;b<=(-70+2.5*(notePos[pitch[i]-12]));b+=5) StdDraw.line(start-5+dist*(i%15), b, start+5+dist*(i%15), b);
					StdDraw.text(start-4+dist*(i%15), -43+17.5-topBotDiff+2.5*notePos[pitch[i]], "\u0023");
					StdDraw.picture(start+.5+dist*(i%15), -42.5-5+17.5-topBotDiff+2.5*notePos[pitch[i]], notePic,8,14);
				}
			}
			//			StdDraw.line(0, -75, 0, -95);
			//			for (int a=-75;a>-100; a-=5){
			//				StdDraw.line(0, a, 200, a);
			//			}
			//			StdDraw.picture(10, -85, "bass.png",15,20);
			StdDraw.setPenColor(StdDraw.RED); 
			//StdDraw.filledCircle(pitch1*10, 100, (Math.abs(pitch1)/10.0)+40);
			//StdDraw.filledCircle(100+50*Math.sin(0), 100+50*Math.cos(0), 20);
			StdDraw.filledCircle(100+50*Math.sin(pitch[i]*10), 100+50*Math.cos(pitch[i]*10), 20);
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
