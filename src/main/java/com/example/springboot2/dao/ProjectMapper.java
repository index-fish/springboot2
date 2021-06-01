package com.example.springboot2.dao;

import com.example.springboot2.model.Project;
import com.example.springboot2.model.User;
import com.example.springboot2.model.UserHub;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ProjectMapper {
    List<Project> getProjectsByUserName(String userName);
    List<User> getDevelopersByProject(Project project);
    int addProjectToProject(@Param("project") Project project);
    int deleteProject(Project project);
    int addDevelopersToUserHub(@Param("users")List<User> users, @Param("project")Project project, @Param("authority")int authority);
    int deleteDevelopers(@Param("users") List<User> users, @Param("project")Project project);
    int deleteDevelopersByProject(Project project);
    UserHub getUserHubByUserNameAndProjectID(@Param("userName")String userName, @Param("projectID")int projectID);
}
