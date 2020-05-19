package com.usian.controller;

import com.netflix.discovery.converters.Auto;
import com.usian.feign.ItemServiceFeign;
import com.usian.pojo.TbItem;
import com.usian.utils.PageResult;
import com.usian.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/backend/item")
public class ItemController {

    @Autowired
    ItemServiceFeign itemServiceFeign;

    /**
     * 根据Id查询商品信息
     * @param itemId
     * @return
     */
    @RequestMapping("/selectItemInfo")
    public Result selectItemInfo(Long itemId){
        TbItem item = itemServiceFeign.selectItemInfo(itemId);
        if(item!=null){
            return Result.ok(item);
        }
        return Result.error("查无结果");
    }

    /**
     * 商品分页信息
     * @param page
     * @param rows
     * @return
     */
    @RequestMapping("selectTbItemAllByPage")
    public Result selectTbItemAllByPage(@RequestParam(defaultValue = "1")Integer page,
                                        @RequestParam(defaultValue = "2")Long rows){
        PageResult pageResult = itemServiceFeign.selectTbItemAllByPage(page,rows);
        if(pageResult.getResult()!=null&&pageResult.getResult().size()>0){
            return Result.ok(pageResult);
        }
        return Result.error("查无结果");
    }

    /**
     * 添加商品
     * @param tbItem
     * @param desc
     * @param itemParams
     * @return
     */
    @RequestMapping("insertTbItem")
    public Result insertTbItem(TbItem tbItem,String desc,String itemParams){
        Integer insertTbItemNum = itemServiceFeign.insertTbItem(tbItem,desc,itemParams);
        if(insertTbItemNum==3){
            return Result.ok();
        }
        return Result.error("添加失败");
    };

    /**
     * 删除商品
     * @param itemId
     * @return
     */
    @RequestMapping("deleteItemById")
    public Result deleteItemById(Long itemId){
        Integer flag = itemServiceFeign.deleteItemById(itemId);
        if(flag==1){
            return Result.ok();
        }
        return Result.error("删除失败");
    }

}
