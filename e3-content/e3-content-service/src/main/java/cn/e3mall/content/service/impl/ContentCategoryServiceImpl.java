package cn.e3mall.content.service.impl;

import cn.e3mall.common.utils.E3Result;
import cn.e3mall.content.service.ContentCategoryService;
import cn.e3mall.coomon.pojo.EasyUITreeNode;
import cn.e3mall.mapper.TbContentCategoryMapper;
import cn.e3mall.pojo.TbContentCategory;
import cn.e3mall.pojo.TbContentCategoryExample;
import cn.e3mall.pojo.TbContentCategoryExample.Criteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ContentCategoryServiceImpl implements ContentCategoryService {

    @Autowired
    private TbContentCategoryMapper tbContentCategoryMapper;

    @Override
    public List<EasyUITreeNode> getContentCatList(long parentId) {
        TbContentCategoryExample example = new TbContentCategoryExample();
        Criteria criteria = example.createCriteria();
        //设置查询条件
        criteria.andParentIdEqualTo(parentId);
        List<TbContentCategory> ContentCatList = tbContentCategoryMapper.selectByExample(example);
        //转换成EasyUITreeNode list
        List<EasyUITreeNode> nodeList = new ArrayList<>();
        for (TbContentCategory tbContentCategory : ContentCatList) {
            EasyUITreeNode node = new EasyUITreeNode();
            node.setId(tbContentCategory.getId());
            node.setState(tbContentCategory.getIsParent() ? "closed" : "open");
            node.setText(tbContentCategory.getName());
            nodeList.add(node);
        }
        return nodeList;
    }

    /**
     * 添加内容分类节点
     *
     * @param parentId
     * @return
     */
    @Override
    public E3Result addContentCategory(long parentId, String name) {
        //创建一个tbContentCategory表对应的pojo对象
        TbContentCategory tbContentCategory = new TbContentCategory();
        //补全属性
        tbContentCategory.setParentId(parentId);
        tbContentCategory.setName(name);

        //可选值:1(正常),2(删除)
        tbContentCategory.setStatus(1);
        //默认排序为1
        tbContentCategory.setSortOrder(1);
        //新添加的节点一定是也只节点
        tbContentCategory.setIsParent(false);
        tbContentCategory.setCreated(new Date());
        tbContentCategory.setUpdated(new Date());
        //插入到数据库，会返回主键id,因为TbContentCategoryMapper的insert设置了selectKey
        //插入成功后返回的主键id,设置在pojo中，即tbContentCategory
        tbContentCategoryMapper.insert(tbContentCategory);
        //判断父节点isParent属性，如果不是true 改为true
        TbContentCategory parent = tbContentCategoryMapper.selectByPrimaryKey(parentId);
        if (!parent.getIsParent()) {
            parent.setIsParent(true);
            tbContentCategoryMapper.updateByPrimaryKey(parent);
        }
        //返回结果，返回E3Result,包含pojo
        return E3Result.ok(tbContentCategory);
    }

    /**
     * 删除分类，如果内容分类下有子分类，需要先删除子分类
     *
     * @param nodeId
     * @return
     */
    @Override
    public E3Result deleteContentCategory(long nodeId) {
        //判断该节点下是否还有子节点
        TbContentCategory tbContentCategory = tbContentCategoryMapper.selectByPrimaryKey(nodeId);
        Boolean isParent = tbContentCategory.getIsParent();
        if (isParent) {
            //如果是父节点就不能删除
            TbContentCategoryExample nodeExample = new TbContentCategoryExample();
            Criteria criteria = nodeExample.createCriteria();
            criteria.andParentIdEqualTo(nodeId);
            //获得子节点总数 ，并返回
            int count = tbContentCategoryMapper.countByExample(nodeExample);

            // 返回还有子节点的结果，不能删除
            return E3Result.build(409,String.valueOf(count));
        } else {
            //如果是叶子节点就可以删除
            tbContentCategoryMapper.deleteByPrimaryKey(nodeId);

            //判断父节点是否还有子节点
            TbContentCategoryExample parentExample = new TbContentCategoryExample();
            Criteria criteria = parentExample.createCriteria();
            criteria.andParentIdEqualTo(tbContentCategory.getParentId());
            int count = tbContentCategoryMapper.countByExample(parentExample);

            //如果父节点没有子节点，把is_parent改为false
            if(count <= 0){
                TbContentCategory parentNode = tbContentCategoryMapper.selectByPrimaryKey(tbContentCategory.getParentId());
                parentNode.setIsParent(false);
                tbContentCategoryMapper.updateByPrimaryKey(parentNode);
            }

            //返回删除节点结果,正常删除选中的节点
            return E3Result.ok();
        }
    }

    /**
     * 更新内容分类,对应内容分类页面重命名功能
     * @param nodeId
     * @param name
     * @return
     */
    @Override
    public E3Result updateContentCategory(long nodeId, String name) {
        TbContentCategory tbContentCategory = tbContentCategoryMapper.selectByPrimaryKey(nodeId);
        tbContentCategory.setName(name);
        tbContentCategoryMapper.updateByPrimaryKey(tbContentCategory);
        return E3Result.ok();
    }
}
