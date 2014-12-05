/* This program generates the sheet music for a piece that is approximately 2 minutes long. The melody notes are generated from 
 * the major pentatonic of a certain key. The chords are created off of the melody notes so that a chord generated off of a melody 
 * note contains the same pitch as the melody note. The rhythms are generated beforehand so that all rhythms add up to 2 minutes,
 * and then each rhythm is assigned to a melody note and chord. The program outputs a line for each note, and each line contains 
 * values for the melody note, the chord's base, the chord's quality(major, minor, or 7th), and the note's duration. 
 */

 


import java.io.PrintWriter;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class MusicTestNew {
	public static int noteLength;
	public static double time =selectDuration(60); 
	static double[] rhythms = genRhythms(time);
	static int notes[] = new int[noteLength];
	static int basenotes[] = new int[noteLength];
	static int qualities[] = new int[noteLength];
	
	static short musickey = genKey();
	static short keyQuality = 0;
	static short relmaj = (short)(musickey + keyQuality);

	static short lengthofChorus = 64;
	static int chorusNotes[] = genChorus(lengthofChorus);
	
	/**
	 * This is the main method of our program - generates the "sheet music"
	 * @param None
	 * @throws IOException
	 */
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
	/**
	 * This method generates the key of the music from a select number of keys
	 * @return short This is the pitch value of the key of the music
	 */
	static short genKey() {
	short[] keys = {5, 7, 10, 12};
	short keyselect = 0;
	for(int i = 0; i < keys.length; i++) {
		short numGen = (short)(Math.random()*keys.length);	
		keyselect = keys[i];
	
	}
	return keyselect;
	}
	/**
	 * This method generates a note from the major pentatonic of a certain key
	 * @param key This is the key of the melody
	 * @param qual This value represents the quality of the key, either major or minor. Major is represented by 0 and minor by 3
	 * @return short A note from the major pentatonic within an octave of the key value
	 */
	static short selectNoteTune(short key, short qual) {
		short note = (short) (6 * Math.random());
		short interval;
		if(qual == 0) {
			if(note ==  0)
				
				interval = (short)(9);
			else if (note == 1)
				interval = (short)(2);
			else if(note == 2)
				interval = (short)(4);
			else if(note == 3)
				interval = (short)(7);
			else
				interval = (short) (12* (short)(1.5 * Math.random()));

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
	/**
	 * This method generates the base note of a chord
	 * @param build A melody note that the chord on the base will accompany.
	 * @return short A base note of a chord that accompanies build
	 */
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
	/**
	 * This method generates the quality of a chord with a given base. 1 is major, 2 is minor, 3 is a 7th chord. Depends on the key.
	 * @param b This is the base of the chord
	 * @return int Determines quality of chord: 1-major, 2-minor, 3-7th
	 */

	static int chordQuality(int b) {//b is always negative
		if((b+36) % 12 == (relmaj % 12)|| (b+36) % 12 == (relmaj + 5) % 12||(b+36) % 12 == (relmaj + 7) % 12) 
			return 1;
		else if((b+36) % 12 == (relmaj +11) % 12)
			return 3;
		else 
			return 2;
	}

	/**
	 * This method creates a duration(in seconds) of a quarter note
	 * @param bpm The beats-per-minute of the song
	 * @return double the length in seconds of a quarter note
	 */
	static double selectDuration(double bpm) {
		double rhythm;
		rhythm = (double)(15/bpm);
		//System.out.println(rhythm);
		return rhythm;
	}
	/**
	 * This method creates an array of doubles that contains the duration of each note of the piece. It generates enough rhythms to fill 2 minutes. It also sets the number of notes in the piece(noteLength)
	 * @param quarterNote This is the length of 1 quarter beat
	 * @return double[] An array of doubles containing the rhythms of each note. 
	 */
	static double[] genRhythms(double quarterNote) {
		double lengths[] = new double[(int)(130/quarterNote)];
		double totalBeat = 0.0;
		for(int i = 0; i < lengths.length; i++) {
			int value = (int)(Math.random() * 2);
			if(value == 0) 
				lengths[i] = quarterNote * 2;
			else if(value == 1)
				lengths[i] = quarterNote;
			else
				lengths[i] = quarterNote;
			totalBeat =totalBeat+ lengths[i];
			if(totalBeat >= 130 && totalBeat <= 134) {
				noteLength = i;
				//System.out.println(i);
				return lengths;
			}
				
		}
		return lengths;
	}
	/**
	 * This method generates the melody notes of the chorus of the piece.
	 * @param chorusLength The number of notes in the chorus
	 * @return int[] An array of melody notes
	 */
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
