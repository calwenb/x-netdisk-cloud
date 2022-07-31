package com.wen.netdisc.filesystem.api.util;

import com.wen.netdisc.common.pojo.FileFolder;
import com.wen.netdisc.common.pojo.TreeNode;
import com.wen.netdisc.filesystem.api.mapper.FolderMapper;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * FolderUtil类
 *
 * @author calwen
 */
@Component
public class FolderUtil {


    @Resource
    FolderMapper folderMapper;

    /**
     * 获得文件目录树
     *
     * @author calwen
     * @since 2022/7/23
     */
    public static TreeNode getTree(List<FileFolder> list) {
        HashMap<Integer, FileFolder> map = new HashMap<>();
        for (FileFolder folder : list) {
            map.put(folder.getFileFolderId(), folder);
        }
        TreeNode root = new TreeNode(0, "root", new ArrayList<>());
        HashMap<Integer, List<TreeNode>> nodeMap = new HashMap<>();
        nodeMap.put(0, new ArrayList<>());
        //构建 节点map，存放节点和节点孩子的指针
        for (FileFolder folder : list) {
            if (folder.getFileFolderId() == 0) {
                continue;
            }
            TreeNode node = new TreeNode(folder.getFileFolderId(), folder.getFileFolderName(), new ArrayList<>());
            if (folder.getParentFolderId() == 0) {
                root.getChildNode().add(node);
                nodeMap.get(0).add(node);
                continue;
            }
            if (nodeMap.containsKey(folder.getParentFolderId())) {
                nodeMap.get(folder.getParentFolderId()).add(node);
            } else {
                ArrayList<TreeNode> childNode = new ArrayList<>();
                childNode.add(node);
                nodeMap.put(folder.getParentFolderId(), childNode);
            }
        }

        System.out.println(root);
        System.out.println(nodeMap);
        dfs(root, nodeMap);
        return root;
    }

    //将节点map 由生成root为根节点的树，深度深度优先搜索
    private static void dfs(TreeNode node, Map<Integer, List<TreeNode>> nodeMap) {
        if (node.getChildNode() == null || node.getChildNode().size() == 0) {
            return;
        }

        for (TreeNode childNode : node.getChildNode()) {
            childNode.setChildNode(nodeMap.get(childNode.getId()));
            dfs(childNode, nodeMap);
        }

    }

    /**
     * 不存在，自动创建文件夹
     *
     * @author calwen
     * @since 2022/7/14
     */
    public static boolean autoFolder(String folderPath) {
        //自动创建父文件夹
        File f = new File(folderPath);
        if (!f.exists()) {
            return f.mkdirs();
        }
        return false;
    }

}