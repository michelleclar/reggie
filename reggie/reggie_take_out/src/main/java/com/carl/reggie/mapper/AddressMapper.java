package com.carl.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.carl.reggie.entity.AddressBook;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AddressMapper extends BaseMapper<AddressBook> {
}
