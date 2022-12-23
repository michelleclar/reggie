package com.carl.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.carl.reggie.entity.AddressBook;

/**
 * @program: reggie
 * @description:
 * @author: Mr.Carl
 * @create: 2022-12-22 09:06
 **/
public interface AddressBookService extends IService<AddressBook> {


    AddressBook updateDerfalult(AddressBook addressBook);
}
