package com.example.common.core.entity;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * @author Administrator
 * @title: StoreUser
 * @projectName demo1
 * @description: TODO
 * @date 2019/7/1111:15
 */
@Data
@Builder
public class StoreUser implements Serializable {
    private int userId;
    private String userName;
}
