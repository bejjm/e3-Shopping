package cn.e3mall.item.pojo;

import cn.e3mall.pojo.TbItem;

import java.util.Date;

//不直接在TbItem中添加getImages方法，是为了减低耦合
public class Item extends TbItem {

    public Item(TbItem tbItem) {

        this.setId(tbItem.getId());
        this.setTitle(tbItem.getTitle());
        this.setSellPoint(tbItem.getSellPoint());
        this.setPrice(tbItem.getPrice());
        this.setNum(tbItem.getNum());
        this.setBarcode(tbItem.getBarcode());
        this.setImage(tbItem.getImage());
        this.setCid(tbItem.getCid());
        this.setStatus(tbItem.getStatus());
        this.setCreated(tbItem.getCreated());
        this.setUpdated(tbItem.getUpdated());
    }

    public String[] getImages() {
        String strings = this.getImage();
        if (strings != null && !"".equals(strings)) {
            String[] images = strings.split(",");
            return images;
        }
        return null;
    }
}
