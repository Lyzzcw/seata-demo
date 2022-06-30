package com.lzy.seata.mapper;

import com.lzy.seata.entity.Store;
import org.apache.ibatis.annotations.Mapper;


public interface StoreMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Store record);

    int insertSelective(Store record);

    Store selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Store record);

    int updateByPrimaryKey(Store record);
}