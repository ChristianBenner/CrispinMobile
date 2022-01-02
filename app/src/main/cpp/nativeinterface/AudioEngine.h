#ifndef CRISPIN_AUDIO_ENGINE_H__
#define CRISPIN_AUDIO_ENGINE_H__

#include <oboe/Oboe.h>

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

private:
    AudioStream* m_stream;
};

#endif // CRISPIN_AUDIO_ENGINE_H__