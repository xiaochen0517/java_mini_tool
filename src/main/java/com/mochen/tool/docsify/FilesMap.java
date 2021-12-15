package com.mochen.tool.docsify;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FilesMap {

    private char type;

    private String name;

    private String path;

    private List<FilesMap> childs;

}
