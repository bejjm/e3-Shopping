package cn.e3mall.common.pojo;

import java.io.Serializable;

/**
 * 搜素商品pojo
 */
public class SearchItem implements Serializable{
    //solr索引库id为String类型
    private String id;
    private String title;
    private String sell_point;
    private long price;
    private String image;
    private String category_name;

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getSell_point() {
        return sell_point;
    }

    public long getPrice() {
        return price;
    }

    public String getImage() {
        return image;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setSell_point(String sell_point) {
        this.sell_point = sell_point;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }
    public String[] getImages() {
        if (image != null && !"".equals(image)) {
            String[] strings = image.split(",");
            return strings;
        }
        return null;
    }
}
