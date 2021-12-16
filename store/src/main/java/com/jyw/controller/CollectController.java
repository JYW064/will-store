package com.jyw.controller;

import com.jyw.common.annotation.Token;
import com.jyw.entity.Collect;
import com.jyw.entity.User;
import com.jyw.mapper.CollectMapper;
import com.jyw.service.CollectService;
import com.jyw.service.impl.CollectServiceImpl;
import com.jyw.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("collect")
public class CollectController {
    @Autowired
    CollectServiceImpl collectService;

    @Token
    @GetMapping("/addCollect")
    public Result addCollect(@RequestParam("userId") int userId,
                             @RequestParam("productId") int productId) {
        Result result = new Result();
            collectService.addCollect(userId, productId);
            result.setCode("001");
            result.setMsg("添加收藏成功");
        return result;
    }

    @Token
    @GetMapping("/deleteCollect")
    public Result deleteCollect(@RequestParam("userId") int userId,
                             @RequestParam("productId") int productId) {
        Result result = new Result();
        collectService.deleteCollect(userId, productId);
        result.setCode("001");
        result.setMsg("取消收藏");
        return result;
    }


    @Token
    @GetMapping("/isCollected")
    public Result isCollected(@RequestParam("userId") int userId,
                                @RequestParam("productId") int productId) {
        Result result = new Result();
        if(collectService.isCollected(userId,productId))
            result.setCode("001");
        else
            result.setCode("004");
        return result;
    }

    @Token
    @PostMapping("/getCollect")
    public Result getCollect(@RequestBody User user) {
        Result result = new Result();
        List<Collect> collectList = collectService.getAllCollects(user.getUserId());
        result.setCode("001");
        result.setResult(collectList);
        return result;
    }
}
