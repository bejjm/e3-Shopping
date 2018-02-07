package cn.e3mall.service.impl;

import cn.e3mall.common.utils.E3Result;
import cn.e3mall.common.utils.IDUtils;
import cn.e3mall.coomon.pojo.EasyUIDataGridResult;
import cn.e3mall.mapper.TbItemDescMapper;
import cn.e3mall.mapper.TbItemMapper;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbItemDesc;
import cn.e3mall.pojo.TbItemExample;
import cn.e3mall.pojo.TbItemExample.Criteria;
import cn.e3mall.service.ItemService;
import com.alibaba.dubbo.container.page.PageHandler;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ItemServiceImpl implements ItemService {
    @Autowired
    private TbItemMapper itemMapper;

    @Autowired
    private TbItemDescMapper tbItemDescMapper;

    @Override
    public TbItem getItemById(long itemId) {
        //方法一：根据主键查询
//        TbItem tbItem = itemMapper.selectByPrimaryKey(itemId);

        //方法二：根据设置条件查询
        TbItemExample example = new TbItemExample();
        Criteria criteria = example.createCriteria();
        criteria.andIdEqualTo(itemId);
        List<TbItem> list = itemMapper.selectByExample(example);
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    @Override
    public EasyUIDataGridResult getItemList(int page, int rows) {
        //设置分页信息，设置的分页信息只对下一次的查询有效
        PageHelper.startPage(page, rows);
        //执行查询,上面设置的分页信息只对下一次的
        TbItemExample example = new TbItemExample();
        List<TbItem> list = itemMapper.selectByExample(example);
        //创建一个返回值对象
        EasyUIDataGridResult result = new EasyUIDataGridResult();
        //取分页结果
        PageInfo<TbItem> pageInfo = new PageInfo<>(list);
        //取总记录数
        long total = pageInfo.getTotal();
        //设置返回值
        result.setRows(list);
        result.setTotal(total);
        return result;
    }

    /**
     * 添加商品，商品表id和商品描述表id一样
     * @param tbItem
     * @param desc
     * @return
     */
    @Override
    public E3Result addItem(TbItem tbItem, String desc) {
        //生成商品id
        long itemId = IDUtils.genItemId();
        //补全商品信息
        tbItem.setId(itemId);
        //1-正常，2-下架，3-删除
        tbItem.setStatus((byte) 1);
        tbItem.setCreated(new Date());
        tbItem.setUpdated(new Date());
        //插入商品数据
        itemMapper.insert(tbItem);
        //创建一个商品描述表的pojo
        TbItemDesc tbItemDesc = new TbItemDesc();
        //设置商品描述信息
        tbItemDesc.setItemId(itemId);
        tbItemDesc.setCreated(new Date());
        tbItemDesc.setUpdated(new Date());
        tbItemDesc.setItemDesc(desc);
        //执行插入商品描述表
        tbItemDescMapper.insert(tbItemDesc);
        //返回成功
        return E3Result.ok();
    }

    /**
     * 获得商品描述信息
     * @param itemDescId
     * @return
     */
    @Override
    public E3Result getTbItemDesc(long itemDescId) {
        TbItemDesc tbItemDesc = tbItemDescMapper.selectByPrimaryKey(itemDescId);
        return E3Result.ok(tbItemDesc);
    }

    /**
     * 更新商品，商品表id和商品描述表id一样
     * @param tbItem
     * @param desc
     * @return
     */
    @Override
    public E3Result updateTbItem(TbItem tbItem, String desc) {
        //补全商品信息
        tbItem.setUpdated(new Date());
        //更新商品数据
        itemMapper.updateByPrimaryKey(tbItem);
        //创建一个商品描述表的pojo
        TbItemDesc tbItemDesc = new TbItemDesc();
        //更新商品描述信息
        tbItemDesc.setItemId(tbItem.getId());
        tbItemDesc.setUpdated(new Date());
        tbItemDesc.setItemDesc(desc);
        //更新插入商品描述表
        tbItemDescMapper.updateByPrimaryKey(tbItemDesc);
        //返回成功
        return E3Result.ok();
    }


}
