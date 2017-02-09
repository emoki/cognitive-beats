// dllmain.cpp : Defines the entry point for the DLL application.
#include "ipp.h"

auto status = ippInit();
auto status1 = ippSetFlushToZero(1, 0);
