package com.elte.jfirbj.backend.models;

import com.elte.jfirbj.backend.models.enums.AlgorithmEnum;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "files")
@NoArgsConstructor
public class FileModel {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    private String name;

    private String type;

    @Enumerated(EnumType.STRING)
    private AlgorithmEnum algorithm;

    private long time;

    private long creationTime;
    @Lob
    private byte[] data;

    public FileModel(String name, String type, AlgorithmEnum algorithm, long creation, long time, byte[] data) {
        this.name = name;
        this.type = type;
        this.algorithm = algorithm;
        this.creationTime = creation;
        this.time = time;
        this.data = data;
    }
}