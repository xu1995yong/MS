package com.xu.seckill.mapper;

import com.xu.seckill.bean.Order;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;
import org.springframework.data.repository.query.Param;


@Mapper
public interface OrderMapper {


    @Select("select * from sk_order where user_id = #{userId} and goods_id = #{goodsId}")
    Order getByUserIdGoodsId(@Param("userId") long userId, @Param("goodsId") long goodsId);
//
//

    /**
     * 通过@SelectKey使insert成功后返回主键id，也就是订单id
     */
    @Insert("insert into ms_order(user_id, goods_id,goods_count, status, create_date)values("
            + "#{userId}, #{goodsId}, #{goodsCount}, #{status},#{createDate} )")
    @SelectKey(keyColumn = "id", keyProperty = "id", resultType = long.class, before = false, statement = "select last_insert_id()")
    long insert(Order order);

    //
//
//    @Insert("insert into sk_order (user_id, goods_id, order_id)values(#{userId}, #{goodsId}, #{orderId})")
//    public int insertSeckillOrder(SeckillOrder order);
//
    @Select("select * from ms_order where id = #{id}")
    Order getById(@Param("id") long id);

}
