package cn.e3mall.controller;

import cn.e3mall.common.utils.E3Result;
import cn.e3mall.coomon.pojo.EasyUIDataGridResult;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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
    public TbItem getItemByid(@PathVariable Long itemId) {
        TbItem item = itemService.getItemById(itemId);
        return item;
    }

    /**
     * 商品列表，(分页)
     * @param page 页数
     * @param rows 一页多少条记录
     * @return
     */
    @RequestMapping("/item/list")
    @ResponseBody
    public EasyUIDataGridResult getItemList(Integer page, Integer rows) {
        //调用服务查询商品列表
        EasyUIDataGridResult result = itemService.getItemList(page, rows);
        return result;
    }

    /**
     * 商品添加功能
     *
     * @param tbItem
     * @param desc
     * @return
     */
    @RequestMapping(value = "/item/save", method = RequestMethod.POST)
    @ResponseBody
    public E3Result addItem(TbItem tbItem, String desc) {
        E3Result result = itemService.addItem(tbItem, desc);
        return result;
    }

    /**
     * 更新商品
     *
     * @return
     */
    @RequestMapping(value = "/item/update", method = RequestMethod.POST)
    @ResponseBody
    public E3Result updateItem(TbItem tbItem, String desc) {
        E3Result result = itemService.updateTbItem(tbItem, desc);
        return result;
    }

    /**
     * 获得商品描述信息（商品描述表id和商品表id一样）
     *
     * @param itemDescId
     * @return
     */
    @RequestMapping("/item/desc/{itemDescId}")
    @ResponseBody
    public E3Result getTbItemDesc(@PathVariable Long itemDescId) {
        E3Result result = itemService.getTbItemDesc(itemDescId);
        return result;
    }

}
