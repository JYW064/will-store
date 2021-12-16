package com.jyw.service.impl;

import com.jyw.entity.Address;
import com.jyw.entity.AddressExample;
import com.jyw.mapper.AddressMapper;
import com.jyw.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AddressServiceImpl implements AddressService {
    @Autowired
    AddressMapper addressMapper;

    @Override
    public void addAddress(Address address) {
        addressMapper.insertSelective(address);
    }

    @Override
    public List<Address> getAddress(int userId) {
        AddressExample example = new AddressExample();
        AddressExample.Criteria criteria = example.createCriteria();
        criteria.andUserIdEqualTo(userId);
        return addressMapper.selectByExample(example);
    }

    @Override
    public void updateAddress(Address address) {
        AddressExample example = new AddressExample();
        AddressExample.Criteria criteria = example.createCriteria();
        criteria.andIdEqualTo(address.getId());
        addressMapper.updateByExampleSelective(address,example);
    }

    @Override
    public void deleteAddress(Address address) {
        AddressExample example = new AddressExample();
        AddressExample.Criteria criteria = example.createCriteria();
        criteria.andIdEqualTo(address.getId());
        addressMapper.deleteByExample(example);
    }
}
