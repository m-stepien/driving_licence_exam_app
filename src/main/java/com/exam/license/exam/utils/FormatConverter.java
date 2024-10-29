package com.exam.license.exam.utils;

import ws.schild.jave.*;


import java.io.File;

public class FormatConverter {
    public static File convertWmvToMp4(File input){
        File output = new File("out.mp4");
        AudioAttributes audio = new AudioAttributes();
        audio.setCodec("aac");
        audio.setBitRate(64000);
        audio.setChannels(2);
        audio.setSamplingRate(44100);
        VideoAttributes video = new VideoAttributes();
        video.setCodec("h264");
        video.setX264Profile(VideoAttributes.X264_PROFILE.BASELINE);
        video.setBitRate(160000);
        video.setFrameRate(15);
        video.setSize(new VideoSize(400, 300));
        EncodingAttributes attrs = new EncodingAttributes();
        attrs.setFormat("mp4");
        attrs.setAudioAttributes(audio);
        attrs.setVideoAttributes(video);
        try {
            Encoder encoder = new Encoder();
            encoder.encode(new MultimediaObject(input), output, attrs);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return output;
    }
}
