package cn.e3mall.controller;

import cn.e3mall.coomon.pojo.EasyUIDataGridResult;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ItemController {

    @Autowired
    private ItemService itemService;

    @RequestMapping("/item/{itemId}")
    @ResponseBody
    /*
    ResponseBody，返回数据的时候自动将TBItem转化为json数据，
    需要添加jackson依赖，否则会出现406错误
     */
    public TbItem getItemByid(@PathVariable Long itemId){
        TbItem item = itemService.getItemById(itemId);
        return item;
    }

    @RequestMapping("/item/list")
    @ResponseBody
    public EasyUIDataGridResult getItemList(Integer page, Integer rows){
        //调用服务查询商品列表
        EasyUIDataGridResult result = itemService.getItemList(page, rows);
        return result;
    }

}
