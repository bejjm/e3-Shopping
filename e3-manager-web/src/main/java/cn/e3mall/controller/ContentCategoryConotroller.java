package cn.e3mall.controller;

import cn.e3mall.common.utils.E3Result;
import cn.e3mall.content.service.ContentCategoryService;
import cn.e3mall.coomon.pojo.EasyUITreeNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 内容分类
 */
@Controller
public class ContentCategoryConotroller {
    @Autowired
    private ContentCategoryService contentCategoryService;

    @RequestMapping("/content/category/list")
    @ResponseBody
    public List<EasyUITreeNode> getContentCatList(
            @RequestParam(name = "id", defaultValue = "0") Long parentId) {
        List<EasyUITreeNode> contentCatList = contentCategoryService.getContentCatList(parentId);
        return contentCatList;
    }

    /**
     * 添加内容分类节点
     *
     * @param parentId
     * @param name
     * @return
     */
    @RequestMapping("/content/category/create")
    @ResponseBody
    public E3Result addContentCategory(Long parentId, String name) {
        //调用服务添加节点
        E3Result result = contentCategoryService.addContentCategory(parentId, name);
        return result;
    }

    @RequestMapping("/content/category/delete")
    @ResponseBody
    public E3Result deleteContentCategory(@RequestParam(name = "id") Long nodeId) {
        E3Result result = contentCategoryService.deleteContentCategory(nodeId);
        return result;
    }

    @RequestMapping("/content/category/update")
    @ResponseBody
    public E3Result updateContentCategory(@RequestParam(name = "id") Long nodeId, String name) {
        E3Result result = contentCategoryService.updateContentCategory(nodeId, name);
        return  result;
    }
}
