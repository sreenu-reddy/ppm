package com.sree.ppm.domains;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Date;

@Data
@Entity
public class ProjectTask {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(updatable = false)
    private Integer projectSequence;
    @NotBlank(message = "Please provide the summary of the projectTask")
    private String summary;
    private String acceptanceCriteria;
    private String status;
    private Integer priority;
    private Date dueDate;
    @Column(updatable = false)
    private String projectIdentifier;
//    many to one with backLog
    @ManyToOne(fetch = FetchType.EAGER,cascade = CascadeType.REFRESH)
    @JoinColumn(name = "backLogId",updatable = false,nullable = false)
    @JsonIgnore
    private BackLog backLog;

}
