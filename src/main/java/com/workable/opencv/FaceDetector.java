package com.workable.opencv;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.objdetect.CascadeClassifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;

@Service
public class FaceDetector {

    @Value("#{environment.opencv_home}")
    private String openCVHome;

    @Value("${cv.scale_factor}")
    private String scaleFactorConfig;

    @Value("${cv.min_neighbors}")
    private String minNeighborsConfig;

    @Value("${cv.flags}")
    private String flagsConfig;

    @Value("${cv.min_size}")
    private String minSizeConfig;

    @Value("${cv.max_size}")
    private String maxSizeConfig;

    private static final Logger LOGGER = LoggerFactory.getLogger(FaceDetector.class);

    private int minNeighbors;
    private int flags;
    private double scaleFactor;
    private Size minSize;
    private Size maxSize;
    private CascadeClassifier facedetector;
    private CloseableHttpClient client;

    @PostConstruct
    public void init() throws URISyntaxException {
        String path = openCVHome + "/../data/haarcascade_frontalface_alt.xml";
        String normalized = new URI(path).normalize().getPath();
        facedetector = new CascadeClassifier(normalized);
        facedetector.load(normalized);

        minNeighbors = Integer.parseInt(minNeighborsConfig);
        scaleFactor = Double.parseDouble(scaleFactorConfig);
        int size = Integer.parseInt(minSizeConfig);
        minSize = new Size(size, size);
        size = Integer.parseInt(maxSizeConfig);
        maxSize = new Size(size, size);
        flags = Integer.parseInt(flagsConfig);
        client = HttpClients.createDefault();
    }

    public synchronized void detect(String imageUrl) throws Exception{
        Mat image = Imgcodecs.imread(downloadImage(imageUrl));
        MatOfRect faceDetections = new MatOfRect();
        LOGGER.info("Testing image {} scaleFactor: {}, minNeighbors: {}, flags {}, minSize: {}, maxSize: {} ", imageUrl, scaleFactor, minNeighbors, flags, minSize.area(), maxSize.area());
        facedetector.detectMultiScale(image, faceDetections, scaleFactor, minNeighbors, flags, minSize, maxSize);
        LOGGER.info("Detected {} faces for img: {} ", faceDetections.toArray().length, imageUrl);
    }

    private String downloadImage(String url) throws Exception{
        String filePath = "image.jpg";
        try (CloseableHttpResponse response = client.execute(new HttpGet(url))) {
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                try (FileOutputStream outstream = new FileOutputStream(filePath)) {
                    entity.writeTo(outstream);
                }
            }
        }
        return filePath;
    }
}
