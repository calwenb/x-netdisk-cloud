package com.wen.netdisc.filesystem.api.util;

import com.wen.netdisc.common.pojo.FileFolder;
import com.wen.netdisc.common.pojo.TreeNode;
import org.springframework.stereotype.Component;

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



    /**
     * 获得文件目录树
     *
     * @author calwen
     * @since 2022/7/23
     */
    public static TreeNode getTree(List<FileFolder> list) {
        int listSize = list.size();
        TreeNode root = new TreeNode(0, "root", new ArrayList<>());
        HashMap<Integer, List<TreeNode>> childMap = new HashMap<>(listSize);
        childMap.put(0, new ArrayList<>());
        //构建 节点map，存放节点和节点孩子的指针。构建root节点
        for (FileFolder folder : list) {
            if (folder.getFileFolderId() == 0) {
                continue;
            }
            TreeNode node = new TreeNode(folder.getFileFolderId(), folder.getFileFolderName(), new ArrayList<>());
            if (folder.getParentFolderId() == 0) {
                root.getChildNode().add(node);
                childMap.get(0).add(node);
                continue;
            }

            if (childMap.containsKey(folder.getParentFolderId())) {
                childMap.get(folder.getParentFolderId()).add(node);
            } else {
                ArrayList<TreeNode> childNode = new ArrayList<>();
                childNode.add(node);
                childMap.put(folder.getParentFolderId(), childNode);
            }
        }
        dfs(root, childMap);
        return root;
    }


    /**
     * 将节点map 由生成root为根节点的树，
     * dfs
     */
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
