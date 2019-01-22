package com.xu.seckill.mapper;

import com.xu.seckill.bean.Order;
import org.apache.ibatis.annotations.*;
import org.springframework.data.repository.query.Param;


@Mapper
public interface OrderMapper {


//    @Select("select * from sk_order where user_id = #{userId} and goods_id = #{goodsId}")
//    Order getByUserIdGoodsId(@Param("userId") long userId, @Param("goodsId") long goodsId);

    @Insert("insert into ms_order(id,user_id, goods_id,goods_count, status, create_date)values("
            + "#{id},#{userId}, #{goodsId}, #{goodsCount}, #{status},#{createDate} )")
    int insert(Order order);

    //
//
//    @Insert("insert into sk_order (user_id, goods_id, order_id)values(#{userId}, #{goodsId}, #{orderId})")
//    public int insertSeckillOrder(SeckillOrder order);
//
    @Select("select * from ms_order where id = #{id}")
    Order getById(@Param("id") String id);

    @Update("UPDATE ms_order SET ms_order.status = #{status} WHERE ms_order.id = #{id}")
    int update(Order order);
}
