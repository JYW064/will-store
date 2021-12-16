package com.jyw.service;

import com.jyw.entity.Address;

import java.util.List;

public interface AddressService {
    void addAddress(Address address);
    List<Address> getAddress(int userId);
    void updateAddress(Address address);
    void deleteAddress(Address address);
}
