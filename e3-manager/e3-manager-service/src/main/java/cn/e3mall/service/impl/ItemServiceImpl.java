package cn.e3mall.service.impl;

import cn.e3mall.common.jedis.JedisClient;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.common.utils.IDUtils;
import cn.e3mall.common.pojo.EasyUIDataGridResult;
import cn.e3mall.common.utils.JsonUtils;
import cn.e3mall.mapper.TbItemDescMapper;
import cn.e3mall.mapper.TbItemMapper;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbItemDesc;
import cn.e3mall.pojo.TbItemExample;
import cn.e3mall.pojo.TbItemExample.Criteria;
import cn.e3mall.service.ItemService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.jms.*;
import java.util.Date;
import java.util.List;

@Service
public class ItemServiceImpl implements ItemService {
    @Autowired
    private TbItemMapper itemMapper;

    @Autowired
    private TbItemDescMapper tbItemDescMapper;

    @Autowired
    private JedisClient jedisClient;

    @Autowired
    private JmsTemplate jmsTemplate;

    @Resource
    private Destination topicDestination;

    @Value("${REDIS_ITEM_PRE}")
    private String REDIS_ITEM_PRE;

    @Value("${ITEM_CACHE_EXPIRE}")
    private Integer ITEM_CACHE_EXPIRE;

    @Override
    public TbItem getItemById(long itemId) {

        //1.先向redis缓存中查询
        try {
            String json = jedisClient.get(REDIS_ITEM_PRE + ":" + itemId + ":BASE");
            if (StringUtils.isNotBlank(json)) {
                TbItem tbItem = JsonUtils.jsonToPojo(json, TbItem.class);
                return tbItem;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //2.缓存中没有再查数据库
        //方法一：根据主键查询
//        TbItem tbItem = itemMapper.selectByPrimaryKey(itemId);

        //方法二：根据设置条件查询
        TbItemExample example = new TbItemExample();
        Criteria criteria = example.createCriteria();
        criteria.andIdEqualTo(itemId);
        List<TbItem> list = itemMapper.selectByExample(example);
        if (list != null && list.size() > 0) {
            TbItem tbItem = list.get(0);

            //将数据加入redis缓存
            try {
                jedisClient.set(REDIS_ITEM_PRE + ":" + itemId + ":BASE", JsonUtils.objectToJson(tbItem));
                //设置缓存的有效期
                jedisClient.expire(ITEM_CACHE_EXPIRE + ":" + itemId + ":BASE", ITEM_CACHE_EXPIRE);
            } catch (Exception e) {
                e.printStackTrace();
            }

            //返回数据
            return tbItem;
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
        final long itemId = IDUtils.genItemId();
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

        //发送一个商品添加activemq 消息
        jmsTemplate.send(topicDestination, new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                TextMessage textMessage = session.createTextMessage(itemId + "");
                return textMessage;
            }
        });


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

    @Override
    public TbItemDesc getItemDesc(long itemId) {
        //1.先向redis缓存中查询
        try {
            String json = jedisClient.get(REDIS_ITEM_PRE + ":" + itemId + ":DESC");
            if (StringUtils.isNotBlank(json)) {
                TbItemDesc tbItemDesc = JsonUtils.jsonToPojo(json, TbItemDesc.class);
                return tbItemDesc;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //2.缓存中没有再查数据库
        TbItemDesc tbItemDesc = tbItemDescMapper.selectByPrimaryKey(itemId);

        //将数据加入redis缓存
        try {
            jedisClient.set(REDIS_ITEM_PRE + ":" + itemId + ":DESC", JsonUtils.objectToJson(tbItemDesc));
            //设置缓存的有效期
            jedisClient.expire(ITEM_CACHE_EXPIRE + ":" + itemId + ":DESC", ITEM_CACHE_EXPIRE);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //返回结果
        return tbItemDesc;
    }


}
