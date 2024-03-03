package dev.magadiflo.projections.app.persistence.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "posts")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private LocalDate createdOn;
    private String createdBy;
    private LocalDate updatedOn;
    private String updatedBy;
    private Integer version;
}
