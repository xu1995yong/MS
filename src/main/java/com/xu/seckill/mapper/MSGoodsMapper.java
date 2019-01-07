package com.xu.seckill.mapper;

import com.xu.seckill.bean.MSGoods;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface MSGoodsMapper {

    @Select("select * from  ms_goods")
    List<MSGoods> getMSGoodslist();

    @Select("select * from ms_goods where ms_goods.id = #{mSGoodsId}")
    MSGoods getMSGoodsById(@Param("mSGoodsId") long mSGoodsId);

    // stock_count > 0 和 版本号实现乐观锁 防止超卖
    @Update("update ms_goods set stock = stock - 1, version= version + 1 where id = #{mSGoodsId} and stock > 0")
    int reduceGoodsStock(@Param("mSGoodsId") long mSGoodsId);

}
