package com.example.common.core.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
@AllArgsConstructor
@NoArgsConstructor
public class StoreUser implements Serializable {
    private Object userId;
    private int limitLevel;
    private String userName;
}
