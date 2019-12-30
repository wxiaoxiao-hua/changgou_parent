package com.itheima.canal.listener;

import com.alibaba.otter.canal.protocol.CanalEntry;
import com.itheima.canal.config.RabbitMQConfig;
import com.xpand.starter.canal.annotation.CanalEventListener;
import com.xpand.starter.canal.annotation.ListenPoint;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ZJ
 */
@CanalEventListener
public class SpuListener {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * spu 表更新
     * @param eventType
     * @param rowData
     */
    @ListenPoint(schema = "changgou_goods", table = {"tb_spu"} )
    public void goodsUp(CanalEntry.EventType eventType, CanalEntry.RowData rowData) {
        System.err.println("tb_spu表数据发生变化");

        //获取修改之前的数据,并且将这部分数据改为map
        Map<String,String> oldData=new HashMap<>();
        rowData.getBeforeColumnsList().forEach((c)->oldData.put(c.getName(),c.getValue()));

        // 获取改变之后的数据,并且将这部分数据修改成为map
        Map<String,String> newData = new HashMap<>();
        rowData.getBeforeColumnsList().forEach((c)->newData.put(c.getName(),c.getValue()));


        //is_marketable  获取最新上架的商品, 由0改为1表示上架
        if("0".equals(oldData.get("is_marketable")) && "1".equals(newData.get("is_marketable")) ){
            rabbitTemplate.convertAndSend(RabbitMQConfig.GOODS_UP_EXCHANGE,"",newData.get("id")); //发送到mq商品上架交换器上
        }

        //is_marketable  获取最新下架的商品, 由1改为0表示下架
        if("1".equals(oldData.get("is_marketable")) && "0".equals(newData.get("is_marketable")) ){
            rabbitTemplate.convertAndSend(RabbitMQConfig.GOODS_DOWN_EXCHANGE,"",newData.get("id")); //发送到mq商品上架交换器上
        }

        //获取最新被审核通过的商品  status    0->1
        if ("0".equals(oldData.get("status")) && "1".equals(newData.get("status"))){
            //将商品的spuid发送到mq
            rabbitTemplate.convertAndSend(RabbitMQConfig.GOODS_UP_EXCHANGE,"",newData.get("id"));
        }
    }
}
