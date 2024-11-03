package com.exam.license.exam.controlers;

import com.exam.license.exam.exceptions.NoSuchElementInDatabaseException;
import com.exam.license.exam.services.MediaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.InputStream;

@RestController
@RequestMapping("/media")
public class MediaController {
    private final MediaService mediaService;

    @Autowired
    public MediaController(MediaService mediaService) {
        this.mediaService = mediaService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<InputStreamResource> fetchMedia(@PathVariable("id") long id) throws NoSuchElementInDatabaseException {
        MediaType contentType = this.mediaService.fetchMediaType(id);
        InputStream in = this.mediaService.fetchMediaName(id);
        return ResponseEntity.ok()
                .contentType(contentType)
                .body(new InputStreamResource(in));
    }
}
