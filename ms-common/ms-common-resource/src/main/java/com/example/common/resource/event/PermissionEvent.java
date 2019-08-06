package com.example.common.resource.event;

import com.example.common.resource.entity.PermissionEntityVO;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * @Description //TODO $
 * @Date 23:46
 * @Author yzcheng90@qq.com
 **/
@Data
@AllArgsConstructor
public class PermissionEvent {
    private List<PermissionEntityVO> list;
}
