#include <jni.h>
#include <string>
#include "cognitive_beats/src/cognitive_beats/beat_generation/beat_generation.h"

jfieldID getHandleField(JNIEnv *env, jobject obj) {
    jclass c = env->GetObjectClass(obj);
    // J is the type signature for long:
    return env->GetFieldID(c, "nativeHandle", "J");
}

template<typename T>
T *getHandle(JNIEnv *env, jobject obj) {
    jlong handle = env->GetLongField(obj, getHandleField(env, obj));
    return reinterpret_cast<T*>(handle);
}

template<typename T>
void setHandle(JNIEnv *env, jobject obj, T *t) {
    jlong handle = reinterpret_cast<jlong>(t);
    env->SetLongField(obj, getHandleField(env, obj), handle);
}

extern "C"
void
Java_com_etek_cognitivebeats_CognitiveBeats_construct(
        JNIEnv *env,
        jobject obj) {

    beat_generation *bg = new beat_generation;

    setHandle(env, obj, bg);
}

extern "C"
void
Java_com_etek_cognitivebeats_CognitiveBeats_destroy(
        JNIEnv *env,
        jobject obj) {

    beat_generation *bg = getHandle<beat_generation>(env, obj);

    delete bg;
}

extern "C"
void
Java_com_etek_cognitivebeats_CognitiveBeats_initialize(
        JNIEnv *env,
        jobject obj,
        double freq0,
        double freq1) {
    beat_generation *bg = getHandle<beat_generation>(env, obj);

    bg->initialize(freq0, freq1);
}

extern "C"
jfloatArray
Java_com_etek_cognitivebeats_CognitiveBeats_tick(
        JNIEnv *env,
        jobject obj,
        int numFrames) {
    beat_generation *bg = getHandle<beat_generation>(env, obj);

    int numFloats = 0;

    float const *data = bg->tick(numFrames, numFloats);

    jfloatArray audioData = env->NewFloatArray(numFloats);

    env->SetFloatArrayRegion(audioData, 0, numFloats, data);

    return audioData;
}

extern "C"
jshortArray
Java_com_etek_cognitivebeats_CognitiveBeats_tickShorts(
        JNIEnv *env,
        jobject obj,
        int numFrames) {
    beat_generation *bg = getHandle<beat_generation>(env, obj);

    int numShorts = 0;

    short const *data = bg->tick_shorts(numFrames, numShorts);

    jshortArray audioData = env->NewShortArray(numShorts);

    env->SetShortArrayRegion(audioData, 0, numShorts, data);

    return audioData;
}

extern "C"
int
Java_com_etek_cognitivebeats_CognitiveBeats_sampleRate(
        JNIEnv *env,
        jobject obj) {

    beat_generation *bg = getHandle<beat_generation>(env, obj);

    return bg->sample_rate();
}
