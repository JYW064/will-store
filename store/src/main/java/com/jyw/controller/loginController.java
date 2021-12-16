package com.jyw.controller;

import com.jyw.entity.User;
import com.jyw.service.impl.LoginImpl;
import com.jyw.utils.RSAUtils;
import com.jyw.utils.TokenUtil;
import com.jyw.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

@CrossOrigin
@RestController
@RequestMapping("users")
public class loginController {
    @Autowired
    LoginImpl login;

    @PostMapping("/login")
    public Result login(@RequestBody User user) throws InvalidKeySpecException, NoSuchAlgorithmException {
        User loginUser = login.getUserByUsername(user.getUsername());
        Result result = new Result();
        if (loginUser == null) {
            result.setCode("002");
            result.setMsg("用户名不存在，可以注册");
        } else {
            if (RSAUtils.privateDecrypt(loginUser.getPassword(), RSAUtils.getPrivateKey(loginUser.getPrivateKey().trim())).equals(user.getPassword())) {
                String token = TokenUtil.token(loginUser.getUsername().trim());
                result.setCode("001");
                result.setMsg("登录成功");
                loginUser.setToken(token);
                loginUser.setPassword(null);
                loginUser.setPrivateKey(null);
                result.setResult(loginUser);
            } else {
                result.setCode("004");
                result.setMsg("用户名或密码错误");
            }
        }
        return result;
    }
}
