package com.elte.jfirbj.backend.controllers.utils;

import com.elte.jfirbj.backend.payload.response.FileResponse;
import com.elte.jfirbj.backend.payload.response.MessageResponse;
import com.elte.jfirbj.backend.security.services.FileStorageService;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;
import java.util.stream.Collectors;

public class FileUtils {
    @Autowired
    protected FileStorageService storageService;

    public void executeShellScript(String algorithmType) {
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.command("./runAlgorithms.sh", algorithmType);

        try {
            Process process = processBuilder.start();

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            printAllImputLines(reader);

            int exitCode = process.waitFor();
            System.out.println("\nExited with error code : " + exitCode);

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void printAllImputLines(BufferedReader reader) throws IOException {
        String line;
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
        }
    }

    protected ResponseEntity<MessageResponse> saveCurrentFileIfNotExist(String algorithm, long begin, long end, File[] files, int i, BasicFileAttributes attr) {
        if (!storageService.isFileExist(attr.creationTime().toMillis())) {
            try {
                FileInputStream input = new FileInputStream(files[i]);
                MultipartFile multipartFile = new MockMultipartFile("file",
                        files[i].getName(), "text/plain", IOUtils.toByteArray(input));
                storageService.store(algorithm, attr.creationTime().toMillis(), end - begin, multipartFile);
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(
                        new MessageResponse(e.getMessage()));
            }
        }
        return null;
    }

    protected List<FileResponse> collectAllFilteredFiles(String algorithm) {
        return storageService.getAllFiles()
                .filter(file -> file.getAlgorithm().toString().contains(algorithm))
                .map(dbFileModel -> {
                    String fileDownloadUri = ServletUriComponentsBuilder
                            .fromCurrentContextPath()
                            .path("/files/")
                            .path(dbFileModel.getId())
                            .toUriString();


                    return new FileResponse(
                            dbFileModel.getName(),
                            fileDownloadUri,
                            dbFileModel.getTime(),
                            dbFileModel.getCreationTime(),
                            dbFileModel.getAlgorithm()
                    );
                }).collect(Collectors.toList());
    }

    protected File[] searchForNewFiles() {
        File folder = new File(System.getProperty("user.dir") + "/files");
        return folder.listFiles();
    }
}
