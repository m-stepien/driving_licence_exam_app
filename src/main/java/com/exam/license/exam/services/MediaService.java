package com.exam.license.exam.services;

import com.exam.license.exam.exceptions.NoSuchElementInDatabaseException;
import com.exam.license.exam.models.Media;
import com.exam.license.exam.repository.MediaRepository;
import com.exam.license.exam.utils.FormatConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import java.io.File;
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
        MediaType mediaType;
        if(media.getType().equals("vmw")){
            mediaType = MediaType.IMAGE_JPEG;
        }
        else{
            mediaType = MediaType.parseMediaType("video/mp4");
        }
        return mediaType;
    }

    public InputStream fetchMediaName(long id) throws NoSuchElementInDatabaseException {
        Media media  = this.mediaRepository.findById(id).orElseThrow(NoSuchElementInDatabaseException::new);
        File mediaFile = new File(this.mediaLocation + "\\" + media.getFileName());
        if(media.getType().equals("wmv")){
            mediaFile = FormatConverter.convertWmvToMp4(mediaFile);
        }
        InputStream in = null;
        try {
            FileInputStream file = new FileInputStream(mediaFile.getAbsolutePath());
            in = file;
        }catch(FileNotFoundException e){
            throw new NoSuchElementInDatabaseException();
        }
        return in;
    }



}
