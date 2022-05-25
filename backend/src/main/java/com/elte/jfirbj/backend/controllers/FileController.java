package com.elte.jfirbj.backend.controllers;

import com.elte.jfirbj.backend.controllers.utils.FileUtils;
import com.elte.jfirbj.backend.models.FileModel;
import com.elte.jfirbj.backend.models.enums.AlgorithmEnum;
import com.elte.jfirbj.backend.payload.response.FileResponse;
import com.elte.jfirbj.backend.payload.response.MessageResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;

@Controller
@RequestMapping("/api/v1")
public class FileController extends FileUtils {


    @PostMapping("/run")
    public ResponseEntity<MessageResponse> runAlgorithm(@RequestParam("type") String algorithm) throws IOException {
        long begin = System.currentTimeMillis();
        executeShellScript(algorithm);
        long end = System.currentTimeMillis();
        if (AlgorithmEnum.isLabelInEnum(algorithm)) {
            File[] files = searchForNewFiles();
            for (int i = 0; i < files.length; i++) {
                BasicFileAttributes attr = Files.readAttributes(files[i].toPath(), BasicFileAttributes.class);
                ResponseEntity<MessageResponse> EXPECTATION_FAILED = saveCurrentFileIfNotExist(algorithm, begin, end, files, i, attr);
                if (EXPECTATION_FAILED != null) return EXPECTATION_FAILED;
            }
            return ResponseEntity.status(HttpStatus.OK).body(
                    new MessageResponse("New files successfuly uploaded")
            );
        } else {
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(
                    new MessageResponse("Can't find such a type"));
        }
    }

    @GetMapping("/files")
    public ResponseEntity<List<FileResponse>> getListFiles(@RequestParam("type") String algorithm) {
        List<FileResponse> files = collectAllFilteredFiles(algorithm);

        return ResponseEntity.status(HttpStatus.OK).body(files);
    }

    @GetMapping("/files/{id}")
    public ResponseEntity<byte[]> getFile(@PathVariable String id) {
        FileModel fileModelDB = storageService.getFile(id);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileModelDB.getName() + "\"")
                .body(fileModelDB.getData());
    }

}