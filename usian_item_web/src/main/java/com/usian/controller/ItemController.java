package com.usian.controller;

import com.netflix.discovery.converters.Auto;
import com.usian.feign.ItemServiceFeign;
import com.usian.pojo.TbItem;
import com.usian.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/frontend/item")
public class ItemController {

    @Autowired
    ItemServiceFeign itemServiceFeign;

    @RequestMapping("/selectItemInfo")
    public Result selectItemInfo(Long itemId){
        TbItem item = itemServiceFeign.selectItemInfo(itemId);
        if(item!=null){
            return Result.ok(item);
        }
        return Result.error("查无结果");
    }

}
