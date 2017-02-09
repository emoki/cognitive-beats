#include "gtest/gtest.h"
#include "cognitive_beats/beat_generation/beat_generation.h"

TEST(ChannelConversion, TestArfcnConversion) {
	beat_generation bg;
	bg.initialize(440.0, 480.0);
	int num_floats = 0;
	auto t = bg.tick(512, num_floats);
}