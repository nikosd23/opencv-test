package com.workable.opencv.controllers;

import com.workable.opencv.FaceDetector;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;

@RestController
@RequestMapping("/api/v1/detect")
public class FaceDetectorController {

    @Inject
    private FaceDetector faceDetector;

    @RequestMapping(
            method = RequestMethod.GET
    )
    public void detect(@RequestParam("url") String url) throws Exception {
        faceDetector.detect(url);
    }
}
