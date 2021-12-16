package com.mochen.tool.docsify;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 用于储存文件或文件夹信息
 *
 * @author LiXingChen
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FilesMap {

    /**
     * 类型 0 文件，1 文件夹
     */
    private char type;

    private String name;

    private String path;

    private List<FilesMap> childs;

}
