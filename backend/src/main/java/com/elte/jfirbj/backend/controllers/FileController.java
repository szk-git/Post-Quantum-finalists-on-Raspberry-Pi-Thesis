package com.elte.jfirbj.backend.controllers;

import com.elte.jfirbj.backend.models.enums.AlgorithmEnum;
import com.elte.jfirbj.backend.payload.response.FileResponse;
import com.elte.jfirbj.backend.payload.response.MessageResponse;
import com.elte.jfirbj.backend.security.services.FileStorageService;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class FileController {
    @Autowired
    private FileStorageService storageService;

    public void runSh(String algorithmType){
        ProcessBuilder processBuilder = new ProcessBuilder();
        // Windows
        processBuilder.command("./runAlgorithms.sh", algorithmType);

        try {

            Process process = processBuilder.start();

            BufferedReader reader =
                    new BufferedReader(new InputStreamReader(process.getInputStream()));

            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

            int exitCode = process.waitFor();
            System.out.println("\nExited with error code : " + exitCode);

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @PostMapping("/run")
    public ResponseEntity<MessageResponse> runAlgorithmTest(@RequestParam("type") String algorithm) throws IOException {
        long begin = System.currentTimeMillis();
        runSh(algorithm);
        long end = System.currentTimeMillis();
        if(AlgorithmEnum.isLabelInEnum(algorithm)){
            File[] files = searchForNewFiles();
            for (int i = 0; i < files.length; i++) {
                BasicFileAttributes attr = Files.readAttributes(files[i].toPath(), BasicFileAttributes.class);
                if(!storageService.isFileExist(files[i].getName(), attr.creationTime().toMillis())){
                    try {
                        FileInputStream input = new FileInputStream(files[i]);
                        MultipartFile multipartFile = new MockMultipartFile("file",
                                files[i].getName(), "text/plain", IOUtils.toByteArray(input));
                        storageService.store(algorithm, attr.creationTime().toMillis(),end - begin, multipartFile);
                    } catch (Exception e) {
                        return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(
                                new MessageResponse(e.getMessage()));
                    }
                }
            }
            return ResponseEntity.status(HttpStatus.OK).body(
                    new MessageResponse("New files successfuly uploaded")
            );
        }else{
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(
                    new MessageResponse("Can't find such a type"));
        }
    }

    @GetMapping("/files")
    public ResponseEntity<List<FileResponse>> getListFiles() {
        List<FileResponse> files = storageService.getAllFiles().map(dbFileModel -> {
            String fileDownloadUri = ServletUriComponentsBuilder
                    .fromCurrentContextPath()
                    .path("/files/")
                    .path(dbFileModel.getId())
                    .toUriString();


            return new FileResponse(
                    dbFileModel.getName(),
                    fileDownloadUri,
                    dbFileModel.getType(),
                    dbFileModel.getTime(),
                    dbFileModel.getCreationTime(),
                    dbFileModel.getAlgorithm(),
                    dbFileModel.getData().length
            );
        }).collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK).body(files);
    }

//    @GetMapping("/files/{id}")
//    public ResponseEntity<byte[]> getFile(@PathVariable String id) {
//        FileModel fileModelDB = storageService.getFile(id);
//
//        return ResponseEntity.ok()
//                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileModelDB.getName() + "\"")
//                .body(fileModelDB.getData());
//    }

    private File[] searchForNewFiles() {
        File folder = new File(System.getProperty("user.dir")+"/files");
        return folder.listFiles();
    }
}
