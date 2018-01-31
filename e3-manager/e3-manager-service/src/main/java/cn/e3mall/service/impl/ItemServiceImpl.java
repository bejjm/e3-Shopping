package cn.e3mall.service.impl;

import cn.e3mall.coomon.pojo.EasyUIDataGridResult;
import cn.e3mall.mapper.TbItemMapper;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbItemExample;
import cn.e3mall.pojo.TbItemExample.Criteria;
import cn.e3mall.service.ItemService;
import com.alibaba.dubbo.container.page.PageHandler;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemServiceImpl implements ItemService{
    @Autowired
    private TbItemMapper itemMapper;

    @Override
    public TbItem getItemById(long itemId) {
        //方法一：根据主键查询
//        TbItem tbItem = itemMapper.selectByPrimaryKey(itemId);

        //方法二：根据设置条件查询
        TbItemExample example =  new TbItemExample();
        Criteria criteria = example.createCriteria();
        criteria.andIdEqualTo(itemId);
        List<TbItem> list = itemMapper.selectByExample(example);
        if(list != null && list.size() > 0){
            return list.get(0);
        }
        return null;
    }

    @Override
    public EasyUIDataGridResult getItemList(int page, int rows) {
        //设置分页信息，设置的分页信息只对下一次的查询有效
        PageHelper.startPage(page,rows);
        //执行查询,上面设置的分页信息只对下一次的
        TbItemExample example =  new TbItemExample();
        List<TbItem> list = itemMapper.selectByExample(example);
        //创建一个返回值对象
        EasyUIDataGridResult result = new EasyUIDataGridResult();
        //取分页结果
        PageInfo<TbItem> pageInfo =  new PageInfo<>(list);
        //取总记录数
        long total = pageInfo.getTotal();
        //设置返回值
        result.setRows(list);
        result.setTotal(total);
        return result;
    }

}
