package com.example.springboot2.controller;

import com.baidu.ueditor.ActionEnter;
import com.example.springboot2.model.BasePath;
import com.example.springboot2.model.Project;
import com.example.springboot2.model.User;
import com.example.springboot2.model.UserHub;
import com.example.springboot2.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/project")
public class ProjectController {
    @Autowired
    ProjectService projectService;
    @Autowired
    BasePath basePath;
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//转化为数据库可以接受的日期格式

    @RequestMapping("/")
    public String index() {
        return "index";
    }

    /**
     * 新建项目
     *
     * @param httpServletRequest
     * @return
     */
    @RequestMapping("/addProject")
    public String addProject(HttpServletRequest httpServletRequest) throws IOException {
        String userName = httpServletRequest.getParameter("userName");
        String password = httpServletRequest.getParameter("password");
        String projectName = httpServletRequest.getParameter("projectName");
        String lastUpdateTime = simpleDateFormat.format(new Date());
        String projectPath = basePath.getBasePath() + '/' + userName + '/' + projectName;
        File file = new File(projectPath);
        if (!file.mkdir()) {
            return projectPath;
            //return "项目名重复";
        }
        Files.copy(new File("src/main/resources/config.json").toPath(), new File(file.getPath() + "/config.json").toPath());//将配置文件复制到项目目录下以支持在线预览编辑

        User user = new User(userName, password);
        List<User> users = new ArrayList<>();
        users.add(user);
        Project project = new Project(projectName, projectPath, lastUpdateTime);
        projectService.addProject(project, users);//新增项目并把自己设置为拥有者
        return "success";
    }

    /**
     * 删除项目
     *
     * @param httpServletRequest
     * @return
     */
    @RequestMapping("/deleteProject")
    public String deleteProject(HttpServletRequest httpServletRequest) {
        String userName = httpServletRequest.getParameter("userName");
        String projectName = httpServletRequest.getParameter("projectName");
        Project p = getProject(userName, projectName);
        if (getAuthority(userName, projectName) >= 1) {
            return "没有删除权限";
        }
        if (p != null) {
            File file = new File(basePath.getBasePath() + '/' + userName + '/' +projectName);
            file.delete();
            projectService.deleteProject(p);
            return "success";
        }
        return "找不到项目";
    }

    /**
     * 上传文件
     *
     * @param httpServletRequest
     * @param multipartFiles     上传的文件数组
     * @return
     */
    @RequestMapping("/addFiles")
    public String addFile(HttpServletRequest httpServletRequest, MultipartFile[] multipartFiles) {
        String userName = httpServletRequest.getParameter("userName");
        String projectName = httpServletRequest.getParameter("projectName");
        String relativePath = httpServletRequest.getParameter("relativePath");
        Project p = getProject(userName, projectName);
        UserHub userHub = projectService.getUserHubByUserNameAndProjectID(projectName, p.getProjectID());
        if (userHub.getAuthority() >= 2) {
            return "没有上传权限";
        }
        for (MultipartFile multipartFile : multipartFiles) {
            String fileName = multipartFile.getOriginalFilename();
            String filePath = basePath.getBasePath() + '/' + userName + '/' + projectName + relativePath;
            File file = new File(filePath + '/' + fileName);
            FileOutputStream fileOutputStream = null;
            try {
                fileOutputStream = new FileOutputStream(file);
                fileOutputStream.write(multipartFile.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
                return "上传失败";
            }
        }
        return "success";
    }

    @RequestMapping("/addDevelopers")
    public String addDevelopers(HttpServletRequest httpServletRequest, List<User> users) {
        String userName = httpServletRequest.getParameter("userName");
        String projectName = httpServletRequest.getParameter("projectName");
        Project project = getProject(userName, projectName);
        int authority = getAuthority(userName, projectName);
        if(authority == -1){
            return "项目不存在";
        }else if (authority >= 1){
            return "没有操作权限";
        }
        projectService.addDevelopers(users, project);
        return "success";
    }

    @RequestMapping("/deleteDevelopers")
    public String deleteDevelopers(HttpServletRequest httpServletRequest, List<User> users) {
        String userName = httpServletRequest.getParameter("userName");
        String projectName = httpServletRequest.getParameter("projectName");
        Project project = getProject(userName, projectName);
        int authority = getAuthority(userName, projectName);
        if(authority == -1){
            return "项目不存在";
        }else if (authority >= 1){
            return "没有操作权限";
        }
        projectService.deleteDevelopers(users, project);
        return "success";
    }

    // 读取配置文件中配置的上传文件保存地址


    /**
     * 接收和获取百度编辑插件的文件，需要注意以下几点
     * 1，需要在 ueditorUrl 目录下创建 ueditor 目录，将 config.json 拷贝到该路径下
     * 2，必须将 ueditorUrl 目录设置为静态资源路径，否则上传的文件可能无法访问
     * 3，注意访问配置文件的方式 ueditor.config.js 请求的路径就是 config.json 放置的同级路径
     */
    @RequestMapping("upload")
    public String upload(HttpServletRequest request, String action) {
        String result = new ActionEnter(request, basePath.getBasePath()).exec();
        basePath.setBasePath(basePath.getBasePath().replaceAll("\\\\", "/"));
        if (action != null && (action.equals("listfile") || action.equals("listimage"))) {
            return result.replaceAll(basePath.getBasePath(), "");
        }
        return result;
    }

    public int getAuthority(String userName, String projectName){
        Project project = getProject(userName, projectName);
        if (project == null) {
            return -1;
        }
        UserHub userHub = projectService.getUserHubByUserNameAndProjectID(userName, project.getProjectID());
        return userHub.getAuthority();
    }

    /**
     * 获取用户名下指定的项目
     * @param userName    用户名
     * @param projectName 项目名
     * @return 不存在指定则返回空
     */
    Project getProject(String userName, String projectName) {
        Project p = null;
        for (Project project : projectService.getProjectsByUserName(userName)) {
            if (project.getProjectName().equals(projectName)) {
                p = project;
            }
        }
        return p;
    }
}
