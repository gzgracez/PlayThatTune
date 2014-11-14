import java.util.*;

public class MusicTest {
	static int notes[] = new int[30];
	static double times[] = new double[30];
	
	public static void main(String[] args) {
		times = selectDuration(60);
		for(int i = 0; i < notes.length; i++) {
			notes[i] = selectNote((short)(3));
			//System.out.println(notes[i] + " "+ times[i]);
			System.out.print(notes[i] + ",");
		}
		System.out.println();
		for(int i = 0; i < notes.length; i++) {
			System.out.print(times[i] + ",");
		}
		
	}
	static short selectNote(short key) {
		short note = (short) (5 * Math.random());
		short interval;
		
		if(note ==  0)
			interval = (short) (12* (short)(1.5 * Math.random()));
		else if (note == 1)
			interval = (short)(2 + (12* (short)(1.4 * Math.random())));
		else if(note == 2)
			interval = (short)(4 + (12* (short)(1.3 * Math.random())));
		else if(note == 3)
			interval = (short)(7 + (12* (short)(1.2 * Math.random())));
		else
			interval = (short)(9 + (12* (short)(1.1 * Math.random())));;
			
		note = (short)(key + interval);
		return note;
	}//selectNote
	
	static double[] selectDuration(double bpm) {
		double rhythms[] = new double[30];
		for(int i = 0; i < rhythms.length; i++) {
			short r = (short)(Math.random()*4+1);
			rhythms[i] = (double)((2^r)/(bpm/7.5));
		}
		
		
		
		return rhythms;
	}
}
