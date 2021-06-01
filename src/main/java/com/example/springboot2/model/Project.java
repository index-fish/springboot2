package com.example.springboot2.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
public class Project {
    String projectName, projectPath;
    int projectID;
    String lastUpdateTime;

    public Project(String projectName, String projectPath, String date){
        this.projectName = projectName;
        this.projectPath = projectPath;
        this.lastUpdateTime = date;
    }
}
