package com.rcbook.controller;

import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by vctran on 12/05/16.
 */
@RestController
public class FileController {

    private String BUCKET_NAME = "rcbook.bucket";
    private String FILES_TEMP_PATH = "/home/vctran/Images/";

    @RequestMapping(value = "/upload", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public Test handleFileUpload(@RequestParam("file") MultipartFile file, @RequestParam("userId") String userId) throws Exception {
        System.out.println("REST request to handleFileUpload");

        AmazonS3 s3Client = new AmazonS3Client(new ProfileCredentialsProvider());
        try {
            Path path = Paths.get(FILES_TEMP_PATH + file.getOriginalFilename());
            File fileToUpload = path.toFile();
            file.transferTo(fileToUpload);
            s3Client.putObject(new PutObjectRequest(BUCKET_NAME, userId+"-"+file.getOriginalFilename(), fileToUpload));
            URL url = s3Client.getUrl(BUCKET_NAME, userId+"-"+file.getOriginalFilename());
            System.out.println("You successfully uploaded " + file.getName() + "!");
            Files.delete(path);
            Test test = new Test();
            test.setUrl(url.toURI().toString());
            return test;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    class Test {
        private String url;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
