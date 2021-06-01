package com.example.springboot2.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UserHub {
    String userName;
    int projectID;
    int authority;//用户对项目的权限,0为项目拥有者,1为项目开发者,2只读
}
