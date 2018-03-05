package cn.e3mall.search.mapper;

import cn.e3mall.common.pojo.SearchItem;

import java.util.List;

public interface ItemMapper {
    /**
     * 导入商品数据到solr索引库的dao方法
     * @return
     */
    List<SearchItem> getItemList();
}
