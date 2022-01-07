package com.experimentality.clothesstorelatamapi.external.firebase.controller;

import com.experimentality.clothesstorelatamapi.external.firebase.service.FirebaseService;
import com.google.common.base.Preconditions;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@RequestMapping("/firebase")
public class FirebaseController {

    private FirebaseService firebaseService;

    @Autowired
    public FirebaseController(FirebaseService firebaseService) {
        this.firebaseService = firebaseService;
    }

    @PostMapping("/image")
    public ResponseEntity<Object> upload(@RequestPart("file") MultipartFile multipartFile) throws IOException {
        Preconditions.checkArgument(multipartFile.getBytes().length != 0, "No se encontró imagen para subir");
        return ResponseEntity.ok(firebaseService.uploadFile(multipartFile));
    }

    @GetMapping("/image/{fileName}")
    public ResponseEntity<Object> download(@PathVariable String fileName) throws Exception {
        Preconditions.checkArgument(StringUtils.isNotBlank(fileName), "Se debe ingresar el nombre de la imagen");

        byte[] content = firebaseService.downloadFile(fileName);
        final ByteArrayResource byteArrayResource = new ByteArrayResource(content);

        return ResponseEntity
                .ok()
                .contentLength(content.length)
                .header("Content-type", "application/octet-stream")
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .body(byteArrayResource);
    }

    @GetMapping("/hello-world")
    public ResponseEntity<?> helloWorld() {
        return ResponseEntity.ok("Hello World");
    }
}
