
#include <memory>
#include "AudioEngine.h"
#include "../oboe/src/common/OboeDebug.h"

void AudioEngine::start()
{
    AudioStreamBuilder builder;
    builder.setPerformanceMode(PerformanceMode::LowLatency);
    builder.setSharingMode(SharingMode::Exclusive);

    Result result = builder.openStream(&m_stream);

    if(result != Result::OK)
    {
        LOGE("Error opening stream: %s", convertToText(result));
    }

    auto bufferSizeResult = m_stream->setBufferSizeInFrames(m_stream->getFramesPerBurst());
    if(bufferSizeResult)
    {
        LOGD("New buffer size is %d frames", bufferSizeResult.value());
    }

    result = m_stream->requestStart();

    if(result != Result::OK)
    {
        LOGE("Error starting stream: %s", convertToText(result));
    }
}

void AudioEngine::stop()
{
    Result result = m_stream->close();

    if(result != Result::OK)
    {
        LOGE("Error closing stream %s", convertToText(result));
    }

    delete m_stream;
}

DataCallbackResult AudioEngine::onAudioReady(AudioStream *oboeStream, void *audioData,
        int32_t numFrames)
{
    return DataCallbackResult::Continue;
}

void AudioEngine::onErrorBeforeClose(AudioStream *stream, Result result)
{
   // AudioStreamCallback::onErrorBeforeClose(stream, result);
}

void AudioEngine::onErrorAfterClose(AudioStream *stream, Result result)
{
   // AudioStreamCallback::onErrorAfterClose(stream, result);
}
