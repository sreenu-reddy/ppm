package com.sree.ppm.domains;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;

import java.util.Date;

@Data
@Entity
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private String projectName;


    @Column(updatable = false,unique = true)
    private String projectIdentifier;

    @Lob
    private String description;
    @JsonFormat(pattern = "yyyy-mm-dd")
    private Date startDate;
    @JsonFormat(pattern = "yyyy-mm-dd")
    private Date endDate;
    @OneToOne(cascade = CascadeType.ALL,fetch = FetchType.EAGER,mappedBy = "project")
    @JsonIgnore
    private BackLog backLog;

}
