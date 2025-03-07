//
//  DelayUtils.cpp
//  Tonic
//
//  Created by Nick Donaldson on 3/10/13.

//

#include "DelayUtils.h"

namespace Tonic {
  
  DelayLine::DelayLine() :
    readHead_(0),
    lastDelayTime_(0),
    isInitialized_(false),
    writeHead_(0),
    interpolates_(true)
  {
    resize(kSynthesisBlockSize, 1, 0);
  }
  
  void DelayLine::initialize(float maxDelay, unsigned int channels)
  {
    unsigned int nFrames = max(2, maxDelay * Tonic::sampleRate());
    resize(nFrames, channels, 0);
    isInitialized_ = true;
  }
  
  void DelayLine::clear()
  {
    if (isInitialized_){
      memset(data_, 0, size_ * sizeof(TonicFloat));
    }
  }
  
}
