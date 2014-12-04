import java.io.PrintWriter;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class MusicTestNew {
	public static int noteLength;
	public static double time =selectDuration(120); 
	static double[] rhythms = genRhythms(time);
	static int notes[] = new int[noteLength];
	static int basenotes[] = new int[noteLength];
	static int qualities[] = new int[noteLength];
	
	static short musickey = 9;
	static short keyQuality = 0;
	static short relmaj = (short)(musickey + keyQuality);

	static short lengthofChorus = 60;
	static int chorusNotes[] = genChorus(lengthofChorus);
	public static void main(String[] args) throws IOException{
		try{
			File file = new File("src/randomSong.txt");
			file.getParentFile().mkdirs();
			PrintWriter bw = new PrintWriter(file);
			
			for(int i = 0; i < notes.length; i++) {
				short pattern1 = (short)(1.5 * Math.random());
				short basepattern = (short)(1.5 * Math.random());
				int noteCounter = 0;
				
				if(i < lengthofChorus)
					notes[i] = chorusNotes[i];
				else if(i >= notes.length - lengthofChorus)
					notes[i] = chorusNotes[i - lengthofChorus- (notes.length - 2*lengthofChorus)];
				else if(pattern1 > 0 && i > 0 && noteCounter < 4) {
					notes[i] = notes[i-1];
					noteCounter = 0;
				}
				else {
					notes[i] = selectNoteTune(musickey,keyQuality);
					noteCounter = noteCounter + 1;
				}
				if(i >= basenotes.length - lengthofChorus)
					basenotes[i] = basenotes[i - lengthofChorus- (basenotes.length - 2*lengthofChorus)];
				else if(basepattern == 0 || i == 0) 
					basenotes[i] = selectNoteBase((short)(notes[i]));
				else
					basenotes[i] = basenotes[i-1];
				qualities[i] = chordQuality(basenotes[i]);

				System.out.println(notes[i] + " "+basenotes[i] + " "+qualities[i] +" "+  rhythms[i]);
				bw.println(notes[i] + " "+basenotes[i] + " "+qualities[i] +" "+  rhythms[i]);
			}
			bw.close();
		}catch (IOException e) {
			e.printStackTrace();
		}
	}
	static short selectNoteTune(short key, short qual) {
		short note = (short) (5 * Math.random());
		short interval;
		if(qual == 0) {
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
		
		}//major
		else { 
			if(note ==  0)
				interval = (short) (12* (short)(1.5 * Math.random()));

			else if (note == 1)
				interval = (short)(3 + (12* (short)(1.2 * Math.random())));
			else if(note == 2)
				interval = (short)(5 + (12* (short)(1.1 * Math.random())));
			else if(note == 3)
				interval = (short)(7);
			else
				interval = (short)(10);

			note = (short)(key + interval);
		}//minor
		return note;
	}//selectNote

	static short selectNoteBase(int build) {
		int midindex;
		int lowindex;
		if(build % 12 == (relmaj +4) % 12|| build % 12 == (relmaj + 9) % 12) {
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
		if(b % 12 == (relmaj % 12)-12|| b % 12 == (relmaj + 5) % 12-12||b % 12 == (musickey + 7) % 12-12) 
			return 1;
		else if(b % 12 == (relmaj -1) % 12-12)
			return 3;
		else 
			return 2;
	}

	static double selectDuration(double bpm) {
		double rhythm;
		rhythm = (double)(15/bpm);
		//System.out.println(rhythm);
		return rhythm;
	}
	static double[] genRhythms(double quarterNote) {
		double lengths[] = new double[(int)(120/quarterNote)];
		double totalBeat = 0.0;
		for(int i = 0; i < lengths.length; i++) {
			int value = (int)(Math.random() * 3);
			if(value == 0) 
				lengths[i] = quarterNote * 2;
			else if(value == 1)
				lengths[i] = quarterNote * 4;
			else
				lengths[i] = quarterNote;
			totalBeat =totalBeat+ lengths[i];
			if(totalBeat >= 120 && totalBeat <= 124) {
				noteLength = i;
				//System.out.println(i);
				return lengths;
			}
				
		}
		return lengths;
	}
	
	static int[] genChorus(short chorusLength) {
		int chorus[] = new int[chorusLength];
		int noteCounter = 0;
		for(int i = 0; i < chorus.length; i++) {
			short pattern = (short)(1.5 * Math.random());
			if(pattern > 0 && i > 0 && noteCounter < 4)
				chorus[i] = chorus[i-1];
			else
				chorus[i] = selectNoteTune(musickey,keyQuality);
		}
		return chorus;
	}//chorus
	
}//allthings
