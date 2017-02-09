#include "cognitive_beats/beat_generation/beat_generation.h"
#include "cognitive_beats/beat_generation/beat_generation_impl.h"

beat_generation::beat_generation() 
	: impl_(new beat_generation_impl())
{
}

beat_generation::~beat_generation() {
	delete impl_;
}

void beat_generation::initialize(double freq_0, double freq_1) {
	impl_->initialize(freq_0, freq_1);
}

float const* beat_generation::tick(int num_frames, int &num_floats) {
	return impl_->tick(num_frames, num_floats);
}

short const* beat_generation::tick_shorts(int num_frames, int &num_shorts) {
	return impl_->tick_shorts(num_frames, num_shorts);
}

int beat_generation::sample_rate() const {
	return impl_->sample_rate();
}

int beat_generation::num_channels() const {
	return impl_->num_channels();
}