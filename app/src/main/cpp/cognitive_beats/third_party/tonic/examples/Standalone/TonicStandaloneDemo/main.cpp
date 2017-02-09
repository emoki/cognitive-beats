//
//  main.cpp
//  TonicStandaloneDemo
//
//  Created by Nick Donaldson on 5/16/13.
//
//

// This is a super-simple demo showing a very basic command-line C++ program to play a Tonic synth

#include <iostream>
#include "Tonic.h"
#include "RtAudio.h"

using namespace Tonic;

const unsigned int nChannels = 2;

// Static smart pointer for our Synth
static Synth synth;
static Tonic::Tonic_::SynthesisContext_ context_;
static Tonic::Synth synth_0_;
static Tonic::Synth synth_1_;

int renderCallback( void *outputBuffer, void *inputBuffer, unsigned int nBufferFrames,
        double streamTime, RtAudioStreamStatus status, void *userData )
{
	Tonic::TonicFrames /*a(nBufferFrames, 2), b(nBufferFrames, 2),*/ combined(nBufferFrames, 2);
	std::vector<TonicFloat> a(nBufferFrames * nChannels);
	std::vector<TonicFloat> b(nBufferFrames * nChannels);
	std::vector<TonicFloat> t(nBufferFrames * nChannels);
	//synth_0_.tick(a, context_);
	//synth_1_.tick(b, context_);
	synth_0_.fillBufferOfFloats(a.data(), nBufferFrames, 1);
	synth_1_.fillBufferOfFloats(b.data(), nBufferFrames, 1);
	//combined.copyChannel(a, 0, 0);
	//combined.copyChannel(b, 0, 1);
	combined.copyToChannel(a.data(), nBufferFrames, 0);
	combined.copyToChannel(b.data(), nBufferFrames, 1);
	memcpy(outputBuffer, &combined[0], sizeof(float) * nBufferFrames * nChannels);
    //synth.fillBufferOfFloats((float*)&t[0], nBufferFrames, 2);
	synth.fillBufferOfFloats((float*)a.data(), nBufferFrames, nChannels);
	//synth.fillBufferOfFloats((float*)outputBuffer, nBufferFrames, nChannels);
	return 0;
}

int main(int argc, const char * argv[])
{
    // Configure RtAudio
    RtAudio dac;
    RtAudio::StreamParameters rtParams;
    rtParams.deviceId = dac.getDefaultOutputDevice();
    rtParams.nChannels = nChannels;
    unsigned int sampleRate = 44100;
    unsigned int bufferFrames = 512; // 512 sample frames
    
    // You don't necessarily have to do this - it will default to 44100 if not set.
    Tonic::setSampleRate(sampleRate);
    
    // --------- MAKE A SYNTH HERE -----------
    //    
    //ControlMetro metro = ControlMetro().bpm(100);
    //ControlGenerator freq = ControlRandom().trigger(metro).min(0).max(1);
    //
    //Generator tone = SquareWaveBL().freq(
    //                                 freq * 0.25 + 100
    //                                 + 400
    //                                 ) * SineWave().freq(50);
    //
    //ADSR env = ADSR()
    //.attack(0.01)
    //.decay( 0.4 )
    //.sustain(0)
    //.release(0)
    //.doesSustain(false)
    //.trigger(metro);
    //
    //StereoDelay delay = StereoDelay(3.0f,3.0f)
    //.delayTimeLeft( 0.5 + SineWave().freq(0.2) * 0.01)
    //.delayTimeRight(0.55 + SineWave().freq(0.23) * 0.01)
    //.feedback(0.3)
    //.dryLevel(0.8)
    //.wetLevel(0.2);
    //
    //Generator filterFreq = (SineWave().freq(0.01) + 1) * 200 + 225;
    //
    //LPF24 filter = LPF24().Q(2).cutoff( filterFreq );
    //
    //Generator output = (( tone * env ) >> filter >> delay) * 0.3;

 //   synth.setOutputGen(output);
	float f0 = 400.0;
	float f1 = 540.0;
	synth.setOutputGen(Tonic::SineWave().freq(f0)/* + Tonic::SineWave().freq(f1)*/);
	synth_0_.setOutputGen(Tonic::SineWave().freq(f0));
	synth_1_.setOutputGen(Tonic::SineWave().freq(f1));


    // ---------------------------------------
    
    
    // open rtaudio stream
    try {
        dac.openStream( &rtParams, NULL, RTAUDIO_FLOAT32, sampleRate, &bufferFrames, &renderCallback, NULL, NULL );
        
        dac.startStream();
        
        // hacky, yes, but let's just hang out for awhile until someone presses a key
        printf("\n\nPress Enter to stop\n\n");
        cin.get();
        
        dac.stopStream();
    }
    catch ( RtError& e ) {
        std::cout << '\n' << e.getMessage() << '\n' << std::endl;
        exit( 0 );
    }
    
    return 0;
}

