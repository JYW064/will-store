package com.jyw.controller;

import com.jyw.common.annotation.Token;
import com.jyw.entity.User;
import com.jyw.service.Register;
import com.jyw.service.impl.RegisterImpl;
import com.jyw.utils.DateUtil;
import com.jyw.utils.RSAUtils;
import com.jyw.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("users")
public class RegisterController {
    @Autowired
    RegisterImpl register;

    @PostMapping("/register")
    public Result register(@RequestBody User user) throws InvalidKeySpecException, NoSuchAlgorithmException {
        Result result = new Result();
        User registerUser = register.getUserByName(user.getUsername());

        if (registerUser != null) {
            result.setCode("004");
            result.setMsg("账号已存在，注册失败!");
        }else{
            registerUser = new User();
            Map<String, String> keyMap = RSAUtils.createKeys(512);
            String publicKey = keyMap.get("publicKey");
            String privateKey = keyMap.get("privateKey");
            String date = DateUtil.ptfDate();
            //公钥加密
            String encodedData = RSAUtils.publicEncrypt(user.getPassword(), RSAUtils.getPublicKey(publicKey));

            registerUser.setUsername(user.getUsername());
            registerUser.setPassword(encodedData);
            registerUser.setTel(user.getTel());
            registerUser.setDate(date);
            registerUser.setStatus(1);
            registerUser.setPrivateKey(privateKey);
            register.insertUser(registerUser);
            result.setCode("001");
            result.setMsg("注册成功");
        }
        return result;
    }
}
