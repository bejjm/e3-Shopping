package cn.e3mall.service;

import cn.e3mall.coomon.pojo.EasyUITreeNode;

import java.util.List;

public interface ItemCatService {
    List<EasyUITreeNode> getCatList(long parentId);
}
