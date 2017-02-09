// dllmain.cpp : Defines the entry point for the DLL application.
#include "stdafx.h"
#include "ipp.h"

BOOL APIENTRY DllMain( HMODULE hModule,
                       DWORD  ul_reason_for_call,
                       LPVOID lpReserved
					 )
{
	int status;

	switch (ul_reason_for_call)
	{
	case DLL_PROCESS_ATTACH:
		status = ippInit();
		status = ippSetFlushToZero(1, NULL);
		break;
	case DLL_THREAD_ATTACH:
	case DLL_THREAD_DETACH:
	case DLL_PROCESS_DETACH:
		status = ippSetFlushToZero(0, NULL);
		break;
	}
	return TRUE;
}

