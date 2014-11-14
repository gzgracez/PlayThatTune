import java.util.*;

public class MusicTestNew {
	static int notes[] = new int[60];
	static double time; 
	
	public static void main(String[] args) {
		time = selectDuration(60);
		for(int i = 0; i < notes.length; i++) {
			short pattern = (short)(2 * Math.random());
			if(pattern > 0 && i > 0)
				notes[i] = notes[i-1];
			else
				notes[i] = selectNoteTune((short)(3));
			System.out.println(notes[i] + " "+ time);
		}
		
	}
	static short selectNoteTune(short key) {
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
	
	static short selectNoteBase(short key) {
		int base = (short)(3 * Math.random());//chords may vary
		short triad;
		if(base == 0)
			triad = (short)(0);
		else if(base == 1)
			triad = (short)(5);
		else if(base == 2)
			triad = (short)(7);
		
		
		return (short)(key-12 + base);
				
	}
	
	static double selectDuration(double bpm) {
		double rhythm;
		rhythm = (double)(15/bpm);
		
		return rhythm;
	}
}
