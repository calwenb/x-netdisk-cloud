package com.wen.common.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TreeNode {
    private Integer Id;
    private String name;
    private List<TreeNode> childNode;
}