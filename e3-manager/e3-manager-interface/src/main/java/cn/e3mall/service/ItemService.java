package cn.e3mall.service;

import cn.e3mall.common.utils.E3Result;
import cn.e3mall.common.pojo.EasyUIDataGridResult;
import cn.e3mall.pojo.TbItem;

public interface ItemService {

    TbItem getItemById(long itemId);

    EasyUIDataGridResult getItemList(int page, int rows);

    E3Result addItem(TbItem tbItem, String desc);

    E3Result getTbItemDesc(long itemDescId);

    E3Result updateTbItem(TbItem tbItem, String desc);
}
