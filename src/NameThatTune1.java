/* This program reads in a text file that contains the pitch, basePitch, chord type, and duration of different notes
 * and uses these values to both play the note and possibly different chords that go with it. This program also displays
 * a dynamically generated visualization of the music by using the pitch to determine the location (on a polar coordinate system)
 * and shade of green of a note on the screen and uses the note's duration to determine the shade of blue of the background (Uses
 * the map method to make sure that color doesn't exceed 255 and position is within the screen). 
 * A tiger conducts this note in the center of the circle and directs its baton to point in the direction of the note around it (Based
 * on the x-y coordinates of the note around it). The actual sheet music is drawn as the music plays, each note (an image of a note) appearing
 * as it is heard. This is based on determining exactly what position the note should be on the staff, whether it is a sharp, and 
 * whether the note should have a trill symbol on top of it. The sheet music resets after the two lines of music are filled with notes.s
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * <h1>Create Random Music!</h1>
 * The NameThatTune1 program plays randomly generated music from a text file and displays a dynamically generated visualization
 * <p>
 * @author Grace Z. & David B.
 * @version 1.0
 * @since 2014-12-5
 */

public class NameThatTune1 {

	/**
	 * This is the main method which makes use of different music and visualization methods
	 * @param None
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException{
		ArrayList<String> orig=new ArrayList<String>();
		BufferedReader br = new BufferedReader(new FileReader("src/randomSong.txt"));
		String line;
		while ((line=br.readLine()) != null) {
			System.out.println(line);
			orig.add(line);
		}
		br.close();
		int pitch[]=new int[orig.size()];
		int basePitch[]=new int[orig.size()];
		int ints[]=new int[orig.size()];
		double duration[]=new double[orig.size()];
		int trill[]=new int[orig.size()];
		for (int i=0;i<orig.size(); i++){
			String[] chunks = orig.get(i).split(" ");
			pitch[i]=Integer.parseInt(chunks[0]);
			basePitch[i]=Integer.parseInt(chunks[1]);
			ints[i]=Integer.parseInt(chunks[2]);
			duration[i]=Double.parseDouble(chunks[3]);
		}

		//double[]a=delay(pitch,duration,ints,basePitch);
		//StdAudio.play(a);
		StdDraw.setCanvasSize(960,500);
		StdDraw.setXscale(0,430);
		StdDraw.setYscale(-10,200);
		for (int i=0;i<pitch.length;i++){
			short trillVariable = (short)(Math.random()*15+1);
			drawNotes(i,duration[i],pitch[i], 250, 150, trillVariable);
			drawTiger(pitch[i],duration[i]);
			short baseQuality[]=chordIntervals(ints[i]);
			trill[i]=trillVariable;
			if(trillVariable == 1) {
				Trill(pitch[i],basePitch[i],baseQuality, duration[i]);
			}
			else {
				double[] a = note(pitch[i],basePitch[i],baseQuality, duration[i], 1);
				StdAudio.play(a);
			}
		}
		StdDraw.show(); 
		StdDraw.setPenColor(StdDraw.WHITE);
		StdDraw.filledRectangle(215, 95, 450, 120);
		for (int z=0; z<=orig.size()/25; z++){
			StdDraw.picture(215, 95, "background1.png",330,210);
			if(z%4==0) StdDraw.picture(215, 95,  "tigerwavingup.png",80,80);
			if(z%4==1) StdDraw.picture(215, 95,  "tigerwaving.png",80,80);
			if(z%4==2) StdDraw.picture(215, 95,  "tigerwavingdown.png",80,80);
			if(z%4==3) StdDraw.picture(215, 95,  "tigerwaving.png",80,80);
			StdDraw.show(100); 
		}
		int length=0;
		for (int z=0; z<pitch.length;z++){
			for(int y=0; y<StdAudio.SAMPLE_RATE*duration[z];y++){
				length++;
			}
		}
		double finalNotes[]=new double[length];
		int count=0;
		for (int z=0; z<pitch.length;z++){
			short baseQuality[]=chordIntervals(ints[z]);
			if(trill[z] == 1){
				double tempNotes[]=saveTrill(pitch[z],basePitch[z],baseQuality, duration[z]);
				for(int y=0; y<StdAudio.SAMPLE_RATE*duration[z];y++){
					finalNotes[count]=tempNotes[y];
					count++;
				}
			}
			else {
				double tempNotes[]=note(pitch[z],basePitch[z],baseQuality, duration[z], 1);
				for(int y=0; y<StdAudio.SAMPLE_RATE*duration[z];y++){
					finalNotes[count]=tempNotes[y];
					count++;
				}
			}
		}
		StdAudio.save("finalRandomSong.wav",finalNotes);

		System.exit(0);
	} 

	/**
	 * This method is used to map decimal numbers of a certain range to a new range
	 * @param num This is the original number that needs to be mapped
	 * @param cmin This is the minimum value of the original range
	 * @param cmax This is the maximum value of the original range
	 * @param nmin This is the minimum value of the new range
	 * @param nmax This is the maximum value of the new range
	 * @return double This returns the value of the mapped number
	 */
	public static double map(double num, double cmin, double cmax, double nmin, double nmax){
		double prop=(num-cmin)/(cmax-cmin);
		double newProp=nmin+(prop*(nmax-nmin));
		return newProp;
	}

	/**
	 * This method is used to draw the actual notes of the sheet music
	 * @param i This is the ith note of the sequence of notes
	 * @param duration This is the duration of that note
	 * @param pitch This is the pitch of that note
	 * @param startX This is the center X-position of the sheet music
	 * @param startY This is the center Y-position of the sheet music
	 * @return Nothing
	 */
	public static void drawNotes(int i, double duration, int pitch, float startX, float startY, int trill){
		Integer notePos[]={0,0,1,2,2,3,3,4,5,5,6,6,0,0,1,2,2,3,3,4,5,5,6,6,7,7};//26, 0-25
		int topBotDiff=45;
		int dist=12;
		String notePic="quarternote";
		if (duration==0.5) notePic="halfnote";
		else if (duration==1.0) notePic="wholenote";

		if (i%30==0){
			StdDraw.setPenColor(StdDraw.WHITE); 
			StdDraw.filledRectangle(startX+75, startY-12.5, 100, 60);
		}
		StdDraw.setPenColor(StdDraw.BLACK); 
		StdDraw.line(startX-25, startY+12.5, startX-25, startY-7.5);
		for (double a=(startY+12.5);a>(startY-12.5); a-=5){
			StdDraw.line(startX-25, a, startX+175, a);
		}
		StdDraw.picture(startX-16, startY, "treble.png",15,40);

		StdDraw.line(startX-25, startY-32.5, startX-25, startY-52.5);
		for (double a=startY-32.5;a>startY-57.5; a-=5){
			StdDraw.line(startX-25, a, startX+175, a);
		}
		StdDraw.picture(startX-16, startY-45, "treble.png",15,40);

		if (i%30<15){//top
			if (pitch<2){
				if (trill==1){
					StdDraw.picture(startX+dist*(i%15)+1, startY+15, "trill.png",8,4);
				}
				notePic+="up.png";
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
				if (trill==1){
					StdDraw.picture(startX+dist*(i%15)+1, startY+5+2.5*notePos[pitch], "trill.png",8,4);
				}
				notePic+="down.png";
				StdDraw.picture(startX+dist*(i%15), startY-5+2.5*notePos[pitch], notePic,8,14);
			}
			else if (pitch==12 || pitch==14 || pitch==15 || pitch==17 || pitch==19 || 
					pitch==20 || pitch==22){
				if (trill==1){
					StdDraw.picture(startX+dist*(i%15)+1, startY+5+17.5+2.5*notePos[pitch], "trill.png",8,4);
				}
				notePic+="down.png";
				StdDraw.picture(startX+dist*(i%15), startY-5+17.5+2.5*notePos[pitch], notePic,8,14);
				for(double b=startY+12.5;b<=(startY+17.5+2.5*(notePos[pitch-12]));b+=5) StdDraw.line(startX-5+dist*(i%15), b, startX+5+dist*(i%15), b);
			}
			else if (pitch==24 || pitch==25){
				if (trill==1){
					StdDraw.picture(startX+dist*(i%15)+1, startY+5+17.5+2.5*notePos[pitch], "trill.png",8,4);
				}
				notePic+="down.png";
				StdDraw.picture(startX+dist*(i%15), startY-5+17.5+2.5*notePos[pitch], notePic,8,14);
				for(double b=startY+12.5;b<=(startY+35+2.5*(notePos[pitch-24]));b+=5) StdDraw.line(startX-5+dist*(i%15), b, startX+5+dist*(i%15), b);
			}
			else if (pitch==4 || pitch==6 || pitch==9 || pitch==11){
				if (trill==1){
					StdDraw.picture(startX+dist*(i%15)+1, startY+5+2.5*notePos[pitch], "trill.png",8,4);
				}
				notePic+="down.png";
				StdDraw.text(startX-4+dist*(i%15), startY-.5+2.5*notePos[pitch], "\u0023");
				StdDraw.picture(startX+.5+dist*(i%15), startY-5+2.5*notePos[pitch], notePic,8,14);
			}
			else if (pitch==13 || pitch==16 || pitch==18 || pitch==21 || pitch==23){
				if (trill==1){
					StdDraw.picture(startX+dist*(i%15)+1, startY+5+17.5+2.5*notePos[pitch], "trill.png",8,4);
				}
				notePic+="down.png";
				StdDraw.text(startX-4+dist*(i%15), startY-.5+17.5+2.5*notePos[pitch], "\u0023");
				StdDraw.picture(startX+.5+dist*(i%15), startY-5+17.5+2.5*notePos[pitch], notePic,8,14);
				for(double b=startY+12.5;b<=(startY+17.5+2.5*(notePos[pitch-12]));b+=5) StdDraw.line(startX-5+dist*(i%15), b, startX+5+dist*(i%15), b);
			}
		}

		else if (i%30<30){//bottom
			if (pitch<2){
				if (trill==1){
					StdDraw.picture(startX+dist*(i%15)+1, startY-topBotDiff+15, "trill.png",8,4);
				}
				notePic+="up.png";
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
				if (trill==1){
					StdDraw.picture(startX+dist*(i%15)+1, startY+5-topBotDiff+2.5*notePos[pitch], "trill.png",8,4);
				}
				notePic+="down.png";
				StdDraw.picture(startX+dist*(i%15), startY-5-topBotDiff+2.5*notePos[pitch], notePic,8,14);
			}
			else if (pitch==12 || pitch==14 || pitch==15 || pitch==17 || pitch==19 || 
					pitch==20 || pitch==22){
				if (trill==1){
					StdDraw.picture(startX+dist*(i%15)+1, startY+5+17.5-topBotDiff+2.5*notePos[pitch], "trill.png",8,4);
				}
				notePic+="down.png";
				StdDraw.picture(startX+dist*(i%15), startY-5+17.5-topBotDiff+2.5*notePos[pitch], notePic,8,14);
				for(double b=startY-32.5;b<=(startY-27.5+2.5*(notePos[pitch]));b+=5) StdDraw.line(startX-5+dist*(i%15), b, startX+5+dist*(i%15), b);
			}
			else if (pitch==24 || pitch==25){
				if (trill==1){
					StdDraw.picture(startX+dist*(i%15)+1, startY+5+17.5-topBotDiff+2.5*notePos[pitch], "trill.png",8,4);
				}
				notePic+="down.png";
				StdDraw.picture(startX+dist*(i%15), startY-5+17.5-topBotDiff+2.5*notePos[pitch], notePic,8,14);
				for(double b=startY-32.5;b<=(startY-10+2.5*(notePos[pitch-12]));b+=5) StdDraw.line(startX-5+dist*(i%15), b, startX+5+dist*(i%15), b);
			}
			else if (pitch==4 || pitch==6 || pitch==9 || pitch==11){
				if (trill==1){
					StdDraw.picture(startX+dist*(i%15)+1, startY+5-topBotDiff+2.5*notePos[pitch], "trill.png",8,4);
				}
				notePic+="down.png";
				StdDraw.text(startX-4+dist*(i%15), startY-.5-topBotDiff+2.5*notePos[pitch], "\u0023");
				StdDraw.picture(startX+.5+dist*(i%15), startY-5-topBotDiff+2.5*notePos[pitch], notePic,8,14);
			}
			else if (pitch==13 || pitch==16 || pitch==18 || pitch==21 || pitch==23){
				if (trill==1){
					StdDraw.picture(startX+dist*(i%15)+1, startY+5+17.5-topBotDiff+2.5*notePos[pitch], "trill.png",8,4);
				}
				notePic+="down.png";
				StdDraw.text(startX-4+dist*(i%15), startY-.5+17.5-topBotDiff+2.5*notePos[pitch], "\u0023");
				StdDraw.picture(startX+.5+dist*(i%15), startY-5+17.5-topBotDiff+2.5*notePos[pitch], notePic,8,14);
				for(double b=startY-32.5;b<=(startY-27.5+2.5*(notePos[pitch-12]));b+=5) StdDraw.line(startX-5+dist*(i%15), b, startX+5+dist*(i%15), b);
			}
		}
		StdDraw.show(20); 
	}

	/**
	 * This method draws a tiger conducting a note whose location and size relative to the tiger are determined by the pitch and duration of the note
	 * @param pitch This is the pitch of the note that is played
	 * @param duration This is the duration of the note that is played
	 * @return Nothing
	 */
	public static void drawTiger(int pitch,double duration){
		StdDraw.setPenRadius(.002);
		StdDraw.setPenColor((int)(map(duration,0,2,0,255)),168+(int)(map(duration,0.0f,2.0f,0.0f,86.0f)),255);
		StdDraw.filledSquare(100, 100, 110);

		StdDraw.setPenColor(StdDraw.BLACK); 
		for (int r=40; r<=100; r+=20) StdDraw.circle(100, 100, r);

		StdDraw.setPenColor(0,(int)map(pitch,0,22,100,200),100);
		StdDraw.circle(100, 100, (map(pitch,0,22,50,80)));
		StdDraw.filledCircle(100+(map(pitch,0,22,50,80))*Math.cos(pitch*10), 100+(map(pitch,0,22,50,80))*Math.sin(pitch*10), 10);
		StdDraw.setPenRadius(.01);
		StdDraw.line(100+(map(pitch,0,22,50,80))*Math.cos(pitch*10)+8.5, 100+(map(pitch,0,22,50,80))*Math.sin(pitch*10), 100+(map(pitch,0,22,50,80))*Math.cos(pitch*10)+9.5, 100+(map(pitch,0,22,50,80))*Math.sin(pitch*10)+30);
		StdDraw.setPenRadius(.002);
		if (Math.cos(pitch*10)>=0 && Math.sin(pitch*10)>=0) StdDraw.picture(100, 100, "tigerstandright.png");
		else if(Math.cos(pitch*10)<=0 && Math.sin(pitch*10)>=0) StdDraw.picture(100, 100, "tigerstandleft.png");
		else if(Math.cos(pitch*10)<=0 && Math.sin(pitch*10)<=0) StdDraw.picture(100, 100, "tigerstandleftdown.png");
		else StdDraw.picture(100, 100, "tigerstandrightdown.png");

	}

	/**
	 * This method generates and plays the notes in a trill
	 * @param a This is the melody of the note
	 * @param base This is the base of the chord
	 * @param ints This is an array of the intervals of the chord
	 * @param t This is the duration of the note
	 */
	public static void Trill(int a, int base, short[] ints, double t){
		double trill = MusicTestNew.time/4;
		double[] originalNote = note(a,base, ints, trill, 1);
		double[] trillNote = note(a+1,base, ints, trill, 1);

		for(int i = 0; i < (t/MusicTestNew.time)*4;i++) {
			if(i % 2 == 0)
				StdAudio.play(originalNote);
			else
				StdAudio.play(trillNote);
		}
	}

	/**
	 * This method returns all the y-coordinates of the notes in a trill
	 * @param a This is the pitch of the original note
	 * @param base This is the basePitch of the original note
	 * @param ints This is the baseQuality of the original note
	 * @param t This is the duration of the original note
	 * @return
	 */
	public static double[] saveTrill(int a, int base, short[] ints, double t){
		int cnt=0;
		int length=(int)(t*StdAudio.SAMPLE_RATE);
		double notes[]=new double[length+6];
		double trill = 1.0/16;
		double[] originalNote = note(a,base, ints, trill, 1);
		double[] trillNote = note(a+1,base, ints, trill, 1);
		for(int i = 0; i < (t/MusicTestNew.time)*4;i++) {
			if(i % 2 == 0){
				for(int z=0;z<StdAudio.SAMPLE_RATE*trill;z++){
					notes[cnt]=originalNote[z];
					cnt+=1;
				}
			}
			else{
				for(int z=0;z<StdAudio.SAMPLE_RATE*trill;z++){
					notes[cnt]=trillNote[z];
					cnt+=1;
				}
			}
		}
		return notes;
	}

	/**
	 * This method takes the weighted average of the sum of two arrays
	 * @param a This is the first array
	 * @param b This is the second array
	 * @param awt This is the weight of the first array
	 * @param bwt This is the weight of the second array
	 * @return double This is the weighted average of the two arrays
	 */
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

	/**
	 * This method is used to translate the given note into a wave
	 * @param hz This is the frequency of the note
	 * @param duration This is the duration of the note
	 * @return double[] This returns an array of y-values of the wave of the sound
	 */
	public static double[] tone(double hz, double duration) { 
		int N = (int) (StdAudio.SAMPLE_RATE * duration);
		double[] a = new double[N+1];
		for (int i = 0; i <= N; i++) {
			a[i] = Math.sin(2 * Math.PI * i * hz / StdAudio.SAMPLE_RATE);
		}
		return a; 
	} 

	/**
	 * This method is used to change the volume of the wave of the notes
	 * @param hz This is the frequency of the note
	 * @param duration This is the duration of the note
	 * @param vol This is the decimal you want the volume of the note to be multiplied by
	 * @return double[] This returns an array of y-values of the wave of the sound
	 */
	public static double[] volume(double hz, double duration, double vol) { 
		int N = (int) (StdAudio.SAMPLE_RATE * duration);
		double[] a = new double[N+1];
		for (int i = 0; i <= N; i++) {
			a[i] = vol*Math.sin(2 * Math.PI * i * hz / StdAudio.SAMPLE_RATE);
		}
		return a;
	} 

	/**
	 * This method is used to create a note with harmonics of the given pitch, where 0 = concert A
	 * @param pitch This is the pitch of the note
	 * @param t This is the duration of the note
	 * @return double This returns the note with harmonics of the given pitch
	 */
	public static double[] harmonic(int pitch, double t) {
		double hz = 440.0 * Math.pow(2, pitch / 12.0);
		double[] a  = tone(hz, t);
		double[] hi = tone(2*hz, t);
		double[] lo = tone(hz/2, t);
		double[] h  = sum(hi, lo, .5, .5);
		return sum(a, h, .5, .5);
	}

	/**
	 * This method is used to play the note and a major chord that uses a given note as the base note
	 * @param pitch This is the pitch of the desired note
	 * @param base This is the base note of the chord
	 * @param t This is the duration of the note
	 * @return double[] This returns the major chord that goes with the note
	 */
	public static double[] majorChord(int pitch,int base, double t) {
		double hz1 = 440.0 * Math.pow(2, pitch / 12.0);
		double hzbase = 440.0 * Math.pow(2, base  / 12.0);
		double hzbase2 = 440.0 * Math.pow(2, (base + 4) / 12.0);
		double hzbase3 = 440.0 * Math.pow(2, (base + 7) / 12.0);

		double[] a  = tone(hz1, t);
		double[] ahi = tone(2*hz1, t);
		double[] alo = tone(hz1/2, t);
		double[] ah  = sum(ahi, alo, .5, .5);
		double[] suma = sum(a, ah, .5, .5);

		double[] b  = tone(hzbase, t);
		double[] bhi = tone(2*hzbase, t);
		double[] blo = tone(hzbase*1/2, t);
		double[] bh  = sum(bhi, blo, .5, .5);
		double[] sumb = sum(b, bh, .5, .5);

		double[] c  = tone(hzbase2, t);
		double[] chi = tone(2*hzbase2, t);
		double[] clo = tone(hzbase2*1/2, t);
		double[] ch  = sum(chi, clo, .5, .5);
		double[] sumc = sum(c, ch, .5, .5);

		double[] d  = tone(hzbase3, t);
		double[] dhi = tone(2*hzbase3, t);
		double[] dlo = tone(hzbase3*1/2, t);
		double[] dh  = sum(dhi, dlo, .5, .5);
		double[] sumd = sum(d, dh, .5, .5);

		double[] chordp1 = sum(sumb, sumc, .5, .5);
		double[] chordtotal = sum(chordp1, sumd, .67,.33);

		return sum(suma, chordtotal, .4, .6);
	}

	/**
	 * This method is used to play the note and a minor chord that uses a given note as the base note
	 * @param pitch This is the pitch of the desired note
	 * @param base This is the base note of the chord
	 * @param t This is the duration of the note
	 * @return double[] This returns minor chord that goes with the note
	 */
	public static double[] minorChord(int pitch,int base, double t) {
		double hz1 = 440.0 * Math.pow(2, pitch / 12.0);
		double hzbase = 440.0 * Math.pow(2, base  / 12.0);
		double hzbase2 = 440.0 * Math.pow(2, (base + 3) / 12.0);
		double hzbase3 = 440.0 * Math.pow(2, (base + 7) / 12.0);

		double[] a  = tone(hz1, t);
		double[] ahi = tone(2*hz1, t);
		double[] alo = tone(hz1/2, t);
		double[] ah  = sum(ahi, alo, .5, .5);
		double[] suma = sum(a, ah, .5, .5);

		double[] b  = tone(hzbase, t);
		double[] bhi = tone(2*hzbase, t);
		double[] blo = tone(hzbase*1/2, t);
		double[] bh  = sum(bhi, blo, .5, .5);
		double[] sumb = sum(b, bh, .5, .5);

		double[] c  = tone(hzbase2, t);
		double[] chi = tone(2*hzbase2, t);
		double[] clo = tone(hzbase2*1/2, t);
		double[] ch  = sum(chi, clo, .5, .5);
		double[] sumc = sum(c, ch, .5, .5);

		double[] d  = tone(hzbase3, t);
		double[] dhi = tone(2*hzbase3, t);
		double[] dlo = tone(hzbase3*1/2, t);
		double[] dh  = sum(dhi, dlo, .5, .5);
		double[] sumd = sum(d, dh, .5, .5);

		double[] chordp1 = sum(sumb, sumc, .5, .5);
		double[] chordtotal = sum(chordp1, sumd, .67,.33);

		return sum(suma, chordtotal, .4, .6);
	}

	/**
	 * This method determines the intervals in a chord with a given base
	 * @param value This represents the quality of the chord: 1- major, 2- minor, 3- 7th
	 * @return short[] An array on values that contains the intervals of the chord
	 */
	static short[] chordIntervals(int value) {
		short intervals[] = new short[2];
		if(value == 1) {
			intervals[0] = 4;
			intervals[1] = 7;
		}
		else if(value == 2) {
			intervals[0] = 3;
			intervals[1] = 7;
		}
		else {
			intervals[0] = 6;
			intervals[1] = 8;
		}

		return intervals;
	}

	/**
	 * This method creates a wave with harmonics for a melody note and a chord, and it combines these waves
	 * @param pitch This is the melody note
	 * @param base This is the base of the chord
	 * @param ints This is an array of the intervals of the chord
	 * @param t This is the duration of the note
	 * @param vol This is the number the original volume should be multiplied by
	 * @return double[] An array that generates the wave for a given note
	 */
	public static double[] note(int pitch,int base,short[] ints, double t, double vol) {
		double hz1 = 440.0 * Math.pow(2, pitch / 12.0);
		double hzbase = 440.0 * Math.pow(2, base  / 12.0);
		double hzbase2 = 440.0 * Math.pow(2, (base + ints[0]) / 12.0);
		double hzbase3 = 440.0 * Math.pow(2, (base + ints[1]) / 12.0);

		double[] a  = volume(hz1, t, vol);
		double[] ahi = volume(2*hz1, t, vol);
		double[] alo = volume(hz1/2, t, vol);
		double[] ah  = sum(ahi, alo, .5, .5);
		double[] suma = sum(a, ah, .5, .5);

		double[] b  = volume(hzbase, t, vol);
		double[] bhi = volume(2*hzbase, t, vol);
		double[] blo = volume(hzbase*1/2, t, vol);
		double[] bh  = sum(bhi, blo, .5, .5);
		double[] sumb = sum(b, bh, .5, .5);

		double[] c  = volume(hzbase2, t, vol);
		double[] chi = volume(2*hzbase2, t, vol);
		double[] clo = volume(hzbase2*1/2, t, vol);
		double[] ch  = sum(chi, clo, .5, .5);
		double[] sumc = sum(c, ch, .5, .5);

		double[] d  = volume(hzbase3, t, vol);
		double[] dhi = volume(2*hzbase3, t, vol);
		double[] dlo = volume(hzbase3*1/2, t, vol);
		double[] dh  = sum(dhi, dlo, .5, .5);
		double[] sumd = sum(d, dh, .5, .5);

		double[] chordp1 = sum(sumb, sumc, .5, .5);
		double[] chordtotal = sum(chordp1, sumd, .67,.33);

		return sum(suma, chordtotal, .4, .6);
	}

	/**
	 * This method delays the music (creates an echo) that is behind by the duration of the first note.
	 * @param pitch This is an array of the original pitches
	 * @param duration This is an array of the duration of the notes
	 * @param ints This is the the array of their different chords
	 * @param basePitch This is an array of the base pitch
	 * @return double[] an array of the y-values of the original melody and an echo combined
	 */
	public static double[] delay(int[] pitch,double[] duration, int[] ints, int []basePitch) {
		int length=0;
		for (int z=0; z<pitch.length;z++){
			for(int y=0; y<StdAudio.SAMPLE_RATE*duration[z];y++){
				length++;
			}
		}
		length+=StdAudio.SAMPLE_RATE*duration[0];
		double []new1=new double[length];
		double []new2=new double[length];
		int count=0;
		for (int z=0; z<pitch.length;z++){
			short baseQuality[]=chordIntervals(ints[z]);
			double[] b = note(pitch[z],basePitch[z],baseQuality, duration[z], 1);
			for(int y=0; y<StdAudio.SAMPLE_RATE*duration[z];y++){
				if (z==0){
					new1[length-(int)(StdAudio.SAMPLE_RATE*duration[0])+count-1]=0;
					new2[count]=0;
				}
				new1[count]=b[y];
				new2[count+(int)(StdAudio.SAMPLE_RATE*duration[0])]=b[y];
				count++;
			}
		}
		double []a=sum(new1,new2,0.5,0.5);
		return a;
	}
}
