
#include <memory>
#include "AudioEngine.h"
#include "../oboe/src/common/OboeDebug.h"
#include <sndfile.h>

// This should be called from the activity onResume() method
void AudioEngine::start()
{
    std::lock_guard<std::mutex> lock(m_lock);

    AudioStreamBuilder builder;
    //builder.setCallback(this);
    builder.setPerformanceMode(PerformanceMode::LowLatency);
    builder.setSharingMode(SharingMode::Exclusive);
    builder.setUsage(Usage::Game);
    builder.setFormat(AudioFormat::Float);
    m_channelCount = ChannelCount::Mono;
    builder.setChannelCount(m_channelCount);
    Result result = builder.openStream(m_stream);

    if(result != Result::OK)
    {
        LOGE("Error opening stream: %s", convertToText(result));
    }

    auto setBufferSizeResult = m_stream->setBufferSizeInFrames(m_stream->getFramesPerBurst() * 2);
    if(setBufferSizeResult) {
        LOGD("New buffer size is %d frames", setBufferSizeResult.value());
    }

    result = m_stream->requestStart();
    if(result != Result::OK)
    {
        LOGE("Error starting stream: %s", convertToText(result));
    }
}

// This should be called from the activity onPause() method
void AudioEngine::stop()
{
    // Stop, close and delete the stream with thread safety
    std::lock_guard<std::mutex> lock(m_lock);
    if(m_stream) {
        m_stream->stop();
        Result closeStreamResult = m_stream->close();
        if(closeStreamResult != Result::OK)
        {
            LOGE("Error closing stream %s", convertToText(closeStreamResult));
        }
        m_stream.reset();
    }
}

void AudioEngine::setAudioData(std::vector<float> audioData) {
    __android_log_write(ANDROID_LOG_INFO,"AudioEngineNative","Adding audio data");
    m_audioData = audioData;
}

DataCallbackResult AudioEngine::onAudioReady(AudioStream *oboeStream, void *audioData,
        int32_t numFrames)
{
//    float *floatData = (float *) audioData;
//    for (int i = 0; i < numFrames; ++i) {
//        float sampleValue = kAmplitude * sinf(mPhase);
//        for (int j = 0; j < m_channelCount; j++) {
//            floatData[i * m_channelCount + j] = sampleValue;
//        }
//        mPhase += mPhaseIncrement;
//        if (mPhase >= kTwoPi) mPhase -= kTwoPi;
//    }
//
//    // todo: Implement audio file playback
//    int16_t *outputBuffer = static_cast<int16_t *>(audioData);
//
//    // Check if the audio file data is loaded
//    if (!m_audioData.empty()) {
//        // Assuming mono audio data
//        for (int i = 0; i < numFrames; ++i) {
//            // Get the sample from the audio file data
//            float sample = m_audioData[i % m_audioData.size()];
//
//            // Convert the floating-point sample to 16-bit PCM
//            int16_t pcmSample = static_cast<int16_t>(sample * INT16_MAX);
//
//            // Write the PCM sample to the output buffer
//            outputBuffer[i] = pcmSample;
//        }
//    } else {
//        // Handle case where audio file data is not loaded
//    }

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
