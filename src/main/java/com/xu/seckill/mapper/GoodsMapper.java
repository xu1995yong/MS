package com.xu.seckill.mapper;

import com.xu.seckill.bean.Goods;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface GoodsMapper {

    @Select("select * from  ms_goods")
    List<Goods> getGoodslist();

    @Select("select * from ms_goods where ms_goods.id = #{goodsId}")
    Goods getGoodsById(@Param("goodsId") long goodsId);

    // stock_count > 0 和 版本号实现乐观锁 防止超卖
    @Update("update ms_goods set stock = stock - 1, version= version + 1 where id = #{goodsId} and stock > 0")
    int reduceGoodsStock(@Param("mSGoodsId") long goodsId);

}
