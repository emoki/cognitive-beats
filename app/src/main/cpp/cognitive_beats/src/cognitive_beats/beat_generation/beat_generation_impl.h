#pragma once
#ifndef BEAT_GENERATION_IMPL_H
#define BEAT_GENERATION_IMPL_H

//#include "stk/include/SineWave.h"
//#include "stk/include/FileWvOut.h"
//#include "cognitive_beats/third_party/tonic/src/Tonic.h"
#include "tonic/SineWave.h"
#include "Tonic.h"
#include "tonic/BufferPlayer.h"


class beat_generation_impl {
public:
	beat_generation_impl() {
		Tonic::setSampleRate(sample_rate_);
	}
	
	Tonic::Synth synth_0_;
	Tonic::Synth synth_1_;
	Tonic::TonicFrames combined_;
	std::vector<short> combined_shorts_;

	void initialize(double freq_0, double freq_1) {
		synth_0_.setOutputGen(Tonic::SineWave().freq(freq_0));
		synth_1_.setOutputGen(Tonic::SineWave().freq(freq_1));
	}

	float const* tick(int num_frames, int &total_num_floats) {
		std::vector<TonicFloat> a(num_frames);
		std::vector<TonicFloat> b(num_frames);
		synth_0_.fillBufferOfFloats(a.data(), num_frames, 1);
		synth_1_.fillBufferOfFloats(b.data(), num_frames, 1);
		combined_.resize(num_frames, num_channels());
		combined_.copyToChannel(a.data(), num_frames, 0);
		combined_.copyToChannel(b.data(), num_frames, 1);
		total_num_floats = num_frames * num_channels();
		return &combined_[0];
	}

	short const* tick_shorts(int num_frames, int &num_shorts) {
		auto data = tick(num_frames, num_shorts);
		combined_shorts_.resize(num_shorts);
		for (auto i = 0; i < num_shorts; ++i)
			combined_shorts_[i] = data[i] * 0x7fff;
		return combined_shorts_.data();
	}

	int sample_rate() const { return 44100; }

	int num_channels() const { return num_channels_; }

	static const int sample_rate_ = 44100;

	static const int num_channels_ = 2;
};

#endif // BEAT_GENERATION_IMPL_H

