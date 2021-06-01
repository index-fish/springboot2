package com.example.springboot2.service;

import com.example.springboot2.dao.ProjectMapper;
import com.example.springboot2.model.Project;
import com.example.springboot2.model.User;
import com.example.springboot2.model.UserHub;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProjectService {
    @Autowired
    ProjectMapper projectMapper;
    @Transactional
    public void addProject(Project project, List<User> user){//新建项目同时把自己加为拥有者
        projectMapper.addProjectToProject(project);
        projectMapper.addDevelopersToUserHub(user, project, 0);
    }

    @Transactional
    public int addDevelopers(List<User> users, Project project){//添加开发者
        return projectMapper.addDevelopersToUserHub(users, project, 1);
    }

    @Transactional
    public int deleteProject(Project project){//删除项目同时删除所有开发者
        projectMapper.deleteDevelopersByProject(project);
        return projectMapper.deleteProject(project);
    }

    @Transactional
    public int deleteDevelopers(List<User> users, Project project){//删除项目中的指定开发者
        return projectMapper.deleteDevelopers(users, project);
    }

    @Transactional
    public List<Project> getProjectsByUserName(String userName){//获取参与的所有项目
        return projectMapper.getProjectsByUserName(userName);
    }

    @Transactional
    public List<User> getDevelopersByProject(Project project){//获得项目中的所有开发者
        return projectMapper.getDevelopersByProject(project);
    }

    @Transactional
    public UserHub getUserHubByUserNameAndProjectID(String userName, int projectID){
        return projectMapper.getUserHubByUserNameAndProjectID(userName, projectID);
    }
}
