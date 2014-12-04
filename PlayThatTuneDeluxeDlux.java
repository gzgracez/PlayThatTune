
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

public class PlayThatTuneDeluxeDlux {

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
    public static double[] note(int pitch,int base,short[] ints, double t) {
        double hz1 = 440.0 * Math.pow(2, pitch / 12.0);
        double hzbase = 440.0 * Math.pow(2, base  / 12.0);
        double hzbase2 = 440.0 * Math.pow(2, (base + ints[0]) / 12.0);
        double hzbase3 = 440.0 * Math.pow(2, (base + ints[1]) / 12.0);
        
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


    // read in notes from standard input and play them on standard audio
    public static void main(String[] args) {

        // read in pitch-duration pairs from standard input
        while (!StdIn.isEmpty()) {
            int pitch = StdIn.readInt();
            int basePitch = StdIn.readInt();
            short baseQuality[] = chordIntervals(StdIn.readInt());
            double duration = StdIn.readDouble();
            double[] a = note(pitch,basePitch,baseQuality, duration);
            StdAudio.play(a);
        }

        // needed to terminate program - known Java bug
        System.exit(0);
    } 
} 
