#ifndef BEAT_GENERATION_H
#define BEAT_GENERATION_H

#include <stdint.h>

class beat_generation_impl;

class beat_generation {
public:
	beat_generation();
	~beat_generation();
	void initialize(double freq_0, double freq_1);
	float const* tick(int num_frames, int &total_num_floats);
	short const* tick_shorts(int num_frames, int &total_num_shorts);
	int sample_rate() const;
	int num_channels() const;
private:
	beat_generation_impl *impl_;
};

#endif // BEAT_GENERATION_H
