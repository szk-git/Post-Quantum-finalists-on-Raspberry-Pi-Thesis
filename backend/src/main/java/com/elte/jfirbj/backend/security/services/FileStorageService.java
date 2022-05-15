package com.elte.jfirbj.backend.security.services;

import com.elte.jfirbj.backend.models.FileModel;
import com.elte.jfirbj.backend.models.enums.AlgorithmEnum;
import com.elte.jfirbj.backend.repository.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.attribute.FileTime;
import java.util.Objects;
import java.util.stream.Stream;

@Service
public class FileStorageService {
    @Autowired
    private FileRepository fileRepository;

    public FileModel store(String algorithmType, long creation ,long time, MultipartFile file) throws IOException {
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));

        FileModel fileModelObj = new FileModel(fileName, file.getContentType(), AlgorithmEnum.valueOf(algorithmType), creation, time, file.getBytes());

        return fileRepository.save(fileModelObj);
    }

    public boolean isFileExist(String name, long creationTime){
        return fileRepository.existsByCreationTime(creationTime);
    }

    public FileModel getFile(String id) {
        return fileRepository.findById(id).get();
    }

    public Stream<FileModel> getAllFiles() {
        return fileRepository.findAll().stream();
    }
}
