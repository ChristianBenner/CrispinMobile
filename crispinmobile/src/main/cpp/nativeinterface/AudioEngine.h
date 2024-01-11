#ifndef CRISPIN_AUDIO_ENGINE_H__
#define CRISPIN_AUDIO_ENGINE_H__

#include <oboe/Oboe.h>
#include <vector>
#include <jni.h>

using namespace oboe;

class AudioEngine : public AudioStreamCallback
{
public:
    AudioEngine() : m_stream(nullptr)
    {

    }

    void testRender();
    void start();
    void stop();

    DataCallbackResult onAudioReady(AudioStream *oboeStream, void *audioData,
            int32_t numFrames) override;
    void onErrorBeforeClose(AudioStream *stream, Result result) override;
    void onErrorAfterClose(AudioStream *stream, Result result) override;
    void setAudioData(std::vector<float> audioData);

    std::shared_ptr<AudioStream> m_stream;
private:
    std::mutex m_lock;
    std::vector<float> m_audioData;

    // Stream params
    int m_channelCount;
    static int constexpr kSampleRate = 48000;
    // Wave params, these could be instance variables in order to modify at runtime
    static float constexpr kAmplitude = 0.5f;
    static float constexpr kFrequency = 440;
    static float constexpr kPI = M_PI;
    static float constexpr kTwoPi = kPI * 2;
    static double constexpr mPhaseIncrement = kFrequency * kTwoPi / (double) kSampleRate;
    // Keeps track of where the wave is
    float mPhase = 0.0;
};

#endif // CRISPIN_AUDIO_ENGINE_H__