package com.mushan.msenbackend.model.dto.user;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户编辑请求类 - 供当前登录用户编辑自己的信息使用
 */
@Data
public class UserEditRequest implements Serializable {

    /**
     * 用户昵称
     */
    private String userName;

    /**
     * 用户简介
     */
    private String userProfile;

    /**
     * 用户头像
     */
    private String userAvatar;

    private static final long serialVersionUID = 1L;
}