package com.carl.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.carl.reggie.common.BaseContext;
import com.carl.reggie.entity.AddressBook;
import com.carl.reggie.mapper.AddressMapper;
import com.carl.reggie.service.AddressBookService;
import org.springframework.stereotype.Service;

/**
 * @program: reggie
 * @description:
 * @author: Mr.Carl
 * @create: 2022-12-22 09:07
 **/
@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressMapper, AddressBook> implements AddressBookService {
    @Override
    public AddressBook updateDerfalult(AddressBook addressBook) {

        LambdaUpdateWrapper<AddressBook> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(AddressBook::getUserId, BaseContext.getCurrentId());
        wrapper.set(AddressBook::getIsDefault, 0);
        //SQL:update address_book set is_default = 0 where user_id = ?
        this.update(wrapper);

        addressBook.setIsDefault(1);
        //SQL:update address_book set is_default = 1 where id = ?
        this.updateById(addressBook);
        return addressBook;
    }
}
