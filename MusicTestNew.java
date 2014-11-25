import java.util.*;

public class MusicTestNew {
	static int notes[] = new int[60];
	static int basenotes[] = new int[60];
	static int qualities[] = new int[60];
	static double time; 
	static short musickey = 3;
	
	public static void main(String[] args) {
		time = selectDuration(60);
		for(int i = 0; i < notes.length; i++) {
			short pattern = (short)(1.5 * Math.random());
			if(pattern > 0 && i > 0)
				notes[i] = notes[i-1];
			else
				notes[i] = selectNoteTune(musickey);
			if(i % 2 == 0) 
				basenotes[i] = selectNoteBase((short)(notes[i]));
			else
				basenotes[i] = basenotes[i-1];
			qualities[i] = chordQuality(basenotes[i]);
			
			
			
			System.out.println(notes[i] + " "+basenotes[i] + " "+qualities[i] +" "+  time);
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
			interval = (short)(7);
		else
			interval = (short)(9);
			
		note = (short)(key + interval);
		return note;
	}//selectNote
	
	static short selectNoteBase(int build) {
		int midindex;
		int lowindex;
		if(build % 12 == (musickey +4) % 12|| build % 12 == (musickey + 9) % 12) {
			midindex = 4;
			lowindex = 3;
		}//3 and 5
		
		else {
			midindex = 3;
			lowindex = 4;
		}//1,2,4
			
		short triad = (short)(3 * Math.random());//chords may vary
		int base = 0;
		if(triad == 0)
			base = build-24;
		else if(triad == 1)
			base = build - midindex -24;
		else if(triad == 2)
			base = build - midindex - lowindex - 24;
		
		
		return (short)(base);
				
	}
	
	static int chordQuality(int b) {//b is always negative
		if(b % 12 == (musickey % 12)-12|| b % 12 == (musickey + 5) % 12-12||b % 12 == (musickey + 7) % 12-12) 
			return 1;
		else if(b % 12 == (musickey -1) % 12-12)
			return 3;
		else 
			return 2;
	}
	
	
	
	
	static double selectDuration(double bpm) {
		double rhythm;
		rhythm = (double)(15/bpm);
		
		return rhythm;
	}
}
