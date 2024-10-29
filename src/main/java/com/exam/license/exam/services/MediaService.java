package com.exam.license.exam.services;

import com.exam.license.exam.exceptions.NoSuchElementInDatabaseException;
import com.exam.license.exam.models.Media;
import com.exam.license.exam.repository.MediaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

@Service
public class MediaService {
    private final MediaRepository mediaRepository;
    private final String mediaLocation = "C:\\Users\\ms\\Desktop\\media";

    @Autowired
    public MediaService(MediaRepository mediaRepository){
        this.mediaRepository = mediaRepository;
    }

    public MediaType fetchMediaType(long id) throws NoSuchElementInDatabaseException {
        Media media = this.mediaRepository.findById(id).orElseThrow(NoSuchElementInDatabaseException::new);
        String mediaName = media.getFileName();
        MediaType mediaType;
        if(mediaName.endsWith(".jpg")|| mediaName.endsWith(".JPG")){
            mediaType = MediaType.IMAGE_JPEG;
        }
        else{
            mediaType = MediaType.parseMediaType("video/x-ms-wmv");
        }
        return mediaType;
    }

    public InputStream fetchMediaName(long id) throws NoSuchElementInDatabaseException {
        Media media  = this.mediaRepository.findById(id).orElseThrow(NoSuchElementInDatabaseException::new);
        InputStream in = null;
        try {
            FileInputStream file = new FileInputStream(this.mediaLocation + "\\" + media.getFileName());
            in = file;
        }catch(FileNotFoundException e){
            System.out.println("error");
        }
        return in;
    }



}
