package com.jyw.controller;

import com.jyw.common.annotation.Token;
import com.jyw.entity.Address;
import com.jyw.entity.User;
import com.jyw.service.AddressService;
import com.jyw.service.impl.AddressServiceImpl;
import com.jyw.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("address")
public class AddressController {
    @Autowired
    AddressServiceImpl addressService;

    @Token
    @PostMapping("/addAddress")
    public Result addAddress(@RequestBody Address address){
        Result result = new Result();
        addressService.addAddress(address);
        result.setCode("001");
        result.setMsg("添加地址成功！");
        return result;
    }

    @Token
    @PostMapping("/getAddress")
    public Result getAddress(@RequestBody User user){
        Result result = new Result();
        List<Address> addressList = addressService.getAddress(user.getUserId());
        result.setCode("001");
        result.setResult(addressList);
        return result;
    }

    @Token
    @PostMapping("/updateAddress")
    public Result updateAddress(@RequestBody Address address){
        Result result = new Result();
        addressService.updateAddress(address);
        result.setCode("001");
        result.setMsg("编辑地址成功！");
        return result;
    }

    @Token
    @PostMapping("/deleteAddress")
    public Result deleteAddress(@RequestBody Address address){
        Result result = new Result();
        addressService.deleteAddress(address);
        result.setCode("001");
        result.setMsg("删除地址成功！");
        return result;
    }
}
