package com.rcbook.controller;

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

/**
 * Created by vctran on 12/05/16.
 */
@RestController
public class FileController {

    @RequestMapping(value = "/upload", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public void handleFileUpload(@RequestParam("file") MultipartFile file) {
        System.out.println("REST request to handleFileUpload");
        try {
            BufferedOutputStream stream =
                    new BufferedOutputStream(new FileOutputStream(new File("/home/vctran/Images/" + file.getOriginalFilename())));
            stream.write(file.getBytes());
            stream.close();
            System.out.println("You successfully uploaded " + file.getName() + "!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
