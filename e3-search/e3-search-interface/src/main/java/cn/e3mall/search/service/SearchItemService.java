package cn.e3mall.search.service;

import cn.e3mall.common.utils.E3Result;

public interface SearchItemService {

    //导入商品数据到solr索引库的方法
    E3Result importAllItems();

}
