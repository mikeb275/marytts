/**
 * Copyright 2007 DFKI GmbH.
 * All Rights Reserved.  Use is subject to license terms.
 * 
 * Permission is hereby granted, free of charge, to use and distribute
 * this software and its documentation without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of this work, and to
 * permit persons to whom this work is furnished to do so, subject to
 * the following conditions:
 * 
 * 1. The code must retain the above copyright notice, this list of
 *    conditions and the following disclaimer.
 * 2. Any modifications must be clearly marked as such.
 * 3. Original authors' names are not deleted.
 * 4. The authors' names are not used to endorse or promote products
 *    derived from this software without specific prior written
 *    permission.
 *
 * DFKI GMBH AND THE CONTRIBUTORS TO THIS WORK DISCLAIM ALL WARRANTIES WITH
 * REGARD TO THIS SOFTWARE, INCLUDING ALL IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS, IN NO EVENT SHALL DFKI GMBH NOR THE
 * CONTRIBUTORS BE LIABLE FOR ANY SPECIAL, INDIRECT OR CONSEQUENTIAL
 * DAMAGES OR ANY DAMAGES WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR
 * PROFITS, WHETHER IN AN ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS
 * ACTION, ARISING OUT OF OR IN CONNECTION WITH THE USE OR PERFORMANCE OF
 * THIS SOFTWARE.
 */

package marytts.signalproc.sinusoidal.hntm.synthesis;

import java.util.Arrays;

import marytts.signalproc.analysis.RegularizedCepstralEnvelopeEstimator;
import marytts.signalproc.sinusoidal.hntm.analysis.HntmSpeechSignal;
import marytts.signalproc.window.Window;
import marytts.util.math.MathUtils;
import marytts.util.signal.SignalProcUtils;

/**
 * @author oytun.turk
 *
 */
public class HarmonicPartLinearPhaseInterpolatorSynthesizer {
    public static double[] synthesize(HntmSpeechSignal hnmSignal, float[] pScales, float[] pScalesTimes)
    {
        double[] harmonicPart = null;
        int trackNoToExamine = 1;

        int i, k, n;
        double t; //Time in seconds
        
        double tsik = 0.0; //Synthesis time in seconds
        double tsikPlusOne = 0.0; //Synthesis time in seconds
        
        double trackStartInSeconds, trackEndInSeconds;
        //double lastPeriodInSeconds = 0.0;
        int trackStartIndex, trackEndIndex;
        double akt;
        int numHarmonicsCurrentFrame;
        int maxNumHarmonics = 0;
        for (i=0; i<hnmSignal.frames.length; i++)
        {
            if (hnmSignal.frames[i].maximumFrequencyOfVoicingInHz>0.0f && hnmSignal.frames[i].h!=null && hnmSignal.frames[i].h.phases!=null)
            {
                numHarmonicsCurrentFrame = hnmSignal.frames[i].h.phases.length;
                if (numHarmonicsCurrentFrame>maxNumHarmonics)
                    maxNumHarmonics = numHarmonicsCurrentFrame;
            }  
        }

        double aksi;
        double aksiPlusOne;
        
        float phaseki;
        float phasekiPlusOne;

        float f0InHzPrev, f0InHz, f0InHzNext;
        float f0av, f0avNext;
        f0InHzPrev = 0.0f;
        double ht;
        float phasekt = 0.0f;

        float phasekiEstimate = 0.0f;
        float phasekiPlusOneEstimate = 0.0f;
        int Mk;
        boolean isPrevVoiced, isVoiced, isNextVoiced;
        boolean isTrackVoiced, isNextTrackVoiced, isPrevTrackVoiced;
        int outputLen = SignalProcUtils.time2sample(hnmSignal.originalDurationInSeconds, hnmSignal.samplingRateInHz);
        
        harmonicPart = new double[outputLen]; //In fact, this should be prosody scaled length when you implement prosody modifications
        Arrays.fill(harmonicPart, 0.0);
        
        //Write separate tracks to output
        double[][] harmonicTracks = null;

        if (maxNumHarmonics>0)
        {
            harmonicTracks = new double[maxNumHarmonics][];
            for (k=0; k<maxNumHarmonics; k++)
            {
                harmonicTracks[k] = new double[outputLen];
                Arrays.fill(harmonicTracks[k], 0.0);
            }
        }
        //
        
        int transitionLen = SignalProcUtils.time2sample(HntmSynthesizer.UNVOICED_VOICED_TRACK_TRANSITION_IN_SECONDS, hnmSignal.samplingRateInHz);
        Window transitionWin = Window.get(Window.HAMMING, transitionLen*2);
        transitionWin.normalizePeakValue(1.0f);
        double[] halfTransitionWinLeft = transitionWin.getCoeffsLeftHalf();

        float[] targetEnergyContour = hnmSignal.getTargetEnergyContour();
        float[] times = hnmSignal.getAnalysisTimes();
        
        for (i=0; i<hnmSignal.frames.length; i++)
        {
            isPrevVoiced = false;
            isVoiced = false;
            isNextVoiced = false;
            
            if (i>0 && hnmSignal.frames[i-1].h!=null && hnmSignal.frames[i-1].h.phases!=null && hnmSignal.frames[i-1].h.phases.length>0)
                isPrevVoiced =  true;
            
            if (hnmSignal.frames[i].h!=null && hnmSignal.frames[i].h.phases!=null && hnmSignal.frames[i].h.phases.length>0)
                isVoiced = true;

            if (i<hnmSignal.frames.length-1 && hnmSignal.frames[i+1].h!=null && hnmSignal.frames[i+1].h.phases!=null && hnmSignal.frames[i+1].h.phases.length>0)
                isNextVoiced = true;
            
            if (isVoiced)
                numHarmonicsCurrentFrame = hnmSignal.frames[i].h.phases.length;
            else if (!isVoiced && isNextVoiced)
                numHarmonicsCurrentFrame = hnmSignal.frames[i+1].h.phases.length;
            else
                numHarmonicsCurrentFrame = 0;
            
            f0InHz = hnmSignal.frames[i].f0InHz;
            
            if (i>0)
                f0InHzPrev = hnmSignal.frames[i-1].f0InHz;
            else
                f0InHzPrev = f0InHz;
            
            if (isNextVoiced)
                f0InHzNext = hnmSignal.frames[i+1].f0InHz;
            else
                f0InHzNext = f0InHz;

            f0av = 0.5f*(f0InHz+f0InHzNext);
            
            for (k=0; k<numHarmonicsCurrentFrame; k++)
            {
                aksi = 0.0;
                aksiPlusOne = 0.0;
                
                phaseki = 0.0f;
                phasekiPlusOne = 0.0f;
                
                isPrevTrackVoiced = false;
                isTrackVoiced = false;
                isNextTrackVoiced = false;
                
                if (i>0 && hnmSignal.frames[i-1].h!=null && hnmSignal.frames[i-1].h.phases!=null && hnmSignal.frames[i-1].h.phases.length>k)
                    isPrevTrackVoiced = true;
                
                if (hnmSignal.frames[i].h!=null && hnmSignal.frames[i].h.phases!=null && hnmSignal.frames[i].h.phases.length>k)
                    isTrackVoiced = true;

                if (i<hnmSignal.frames.length-1 && hnmSignal.frames[i+1].h!=null && hnmSignal.frames[i+1].h.phases!=null && hnmSignal.frames[i+1].h.phases.length>k)
                    isNextTrackVoiced = true;

                tsik = hnmSignal.frames[i].tAnalysisInSeconds;

                if (i==0)
                    trackStartInSeconds = 0.0;
                else
                    trackStartInSeconds = tsik;
                
                if (i==hnmSignal.frames.length-1)
                    tsikPlusOne = hnmSignal.originalDurationInSeconds;
                else
                    tsikPlusOne = hnmSignal.frames[i+1].tAnalysisInSeconds;

                trackEndInSeconds = tsikPlusOne;

                trackStartIndex = SignalProcUtils.time2sample(trackStartInSeconds, hnmSignal.samplingRateInHz);
                trackEndIndex = SignalProcUtils.time2sample(trackEndInSeconds, hnmSignal.samplingRateInHz);

                if (isTrackVoiced && trackEndIndex-trackStartIndex+1>0)
                {
                    //Amplitudes     
                    if (isTrackVoiced)
                    {
                        aksi = RegularizedCepstralEnvelopeEstimator.cepstrum2linearSpectrumValue(hnmSignal.frames[i].h.ceps, k*f0InHz, hnmSignal.samplingRateInHz);
                        //aksi = hnmSignal.frames[i].h.ceps[k]; //Use amplitudes directly without cepstrum method
                    }
                    
                    if (isNextTrackVoiced)
                    {
                        aksiPlusOne = RegularizedCepstralEnvelopeEstimator.cepstrum2linearSpectrumValue(hnmSignal.frames[i+1].h.ceps , k*f0InHzNext, hnmSignal.samplingRateInHz);
                        //aksiPlusOne = hnmSignal.frames[i+1].h.ceps[k]; //Use amplitudes directly without cepstrum method
                    }
                    //

                    //Phases
                    if (isTrackVoiced)
                    {
                        if (k==0)
                            phaseki = 0.0f;
                        else
                            phaseki = hnmSignal.frames[i].h.phases[k];
                    }
                    if (isNextTrackVoiced)
                    {
                        if (k==0)
                            phasekiPlusOne = 0.0f;
                        else
                            phasekiPlusOne = hnmSignal.frames[i+1].h.phases[k];
                    }
                    
                    if (!isTrackVoiced && isNextTrackVoiced)   
                        phaseki = (float)( phasekiPlusOne - k*MathUtils.TWOPI*f0InHzNext*(tsikPlusOne-tsik)); //Equation (3.54)
                    else if (isTrackVoiced && !isNextTrackVoiced)
                        phasekiPlusOne = (float)( phaseki + k*MathUtils.TWOPI*f0InHz*(tsikPlusOne-tsik)); //Equation (3.55)
                    
                    phasekiPlusOneEstimate = (float)( phaseki + k*MathUtils.TWOPI*f0av*(tsikPlusOne-tsik));
                    //phasekiPlusOneEstimate = (float) (MathUtils.TWOPI*(Math.random()-0.5)); //Random phase
                    
                    Mk = (int)Math.floor((phasekiPlusOneEstimate-phasekiPlusOne)/MathUtils.TWOPI + 0.5);
                    //
                    
                    if (!isPrevTrackVoiced)
                        trackStartIndex  = Math.max(0, trackStartIndex-transitionLen);

                    for (n=trackStartIndex; n<=Math.min(trackEndIndex, outputLen-1); n++)
                    {
                        t = SignalProcUtils.sample2time(n, hnmSignal.samplingRateInHz);

                        //if (t>=tsik && t<tsikPlusOne)
                        {
                            //Amplitude estimate
                            akt = MathUtils.interpolatedSample(tsik, t, tsikPlusOne, aksi, aksiPlusOne);
                            //

                            //Phase estimate
                            phasekt = (float)( phaseki + (phasekiPlusOne+MathUtils.TWOPI*Mk-phaseki)*(t-tsik)/(tsikPlusOne-tsik) );
                            //

                            if (!isPrevTrackVoiced && n-trackStartIndex<transitionLen)
                                harmonicTracks[k][n] = halfTransitionWinLeft[n-trackStartIndex]*akt*Math.cos(phasekt);
                            else
                                harmonicTracks[k][n] = akt*Math.cos(phasekt);
                        }
                    } 
                }
            }
        }
        
        if (harmonicTracks!=null)
        {
            for (k=0; k<harmonicTracks.length; k++)
            {
                for (n=0; n<harmonicPart.length; n++)
                    harmonicPart[n] += harmonicTracks[k][n];
            }

            /*
            //Write separate tracks to output
            AudioInputStream inputAudio = null;
            try {
                inputAudio = AudioSystem.getAudioInputStream(new File("d:\\i.wav"));
            } catch (UnsupportedAudioFileException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            if (inputAudio!=null)
            {
                //k=1;
                for (k=0; k<harmonicTracks.length; k++)
                {
                    harmonicTracks[k] = MathUtils.divide(harmonicTracks[k], 32768.0);

                    DDSAudioInputStream outputAudio = new DDSAudioInputStream(new BufferedDoubleDataSource(harmonicTracks[k]), inputAudio.getFormat());
                    String outFileName = "d:\\harmonicTrack" + String.valueOf(k+1) + ".wav";
                    try {
                        AudioSystem.write(outputAudio, AudioFileFormat.Type.WAVE, new File(outFileName));
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
            //
            */
        }
        
        return harmonicPart;
    }
}