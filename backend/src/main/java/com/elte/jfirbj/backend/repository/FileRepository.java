package com.elte.jfirbj.backend.repository;

import com.elte.jfirbj.backend.models.FileModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;


@Repository
public interface FileRepository extends JpaRepository<FileModel, String> {
    Boolean existsByCreationTime(long registrationTime);
    Boolean existsByName(String name);
}