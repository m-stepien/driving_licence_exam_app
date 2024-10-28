package com.exam.license.exam.services;

import com.exam.license.exam.repository.MediaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MediaService {
    private final MediaRepository mediaRepository;

    @Autowired
    public MediaService(MediaRepository mediaRepository){
        this.mediaRepository = mediaRepository;
    }

}
