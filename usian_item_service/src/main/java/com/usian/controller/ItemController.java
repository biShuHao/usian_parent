package com.usian.controller;


import com.usian.pojo.TbItem;
import com.usian.service.ItemService;
import com.usian.utils.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/service/item")
public class ItemController {

    @Autowired
    private ItemService itemService;


    /*
        根据商品id查询商品信息
     */
    @RequestMapping("selectItemInfo")
    public TbItem selectItemInfo(Long itemId){
        return itemService.selectItemInfo(itemId);
    }

    /**
     * 查询商品列表分页
     * @param page
     * @param rows
     * @return
     */
    @RequestMapping("selectTbItemAllByPage")
    public PageResult selectTbItemAllByPage(Integer page,Long rows){
        return itemService.selectTbItemAllByPage(page,rows);
    }

    /**
     * 添加商品
     * @param tbItem
     * @return
     */
    @RequestMapping("insertTbItem")
    public Integer insertTbItem(@RequestBody TbItem tbItem,String desc,String itemParams){
        return itemService.insertTbItem(tbItem,desc,itemParams);
    }

    /**
     * 删除商品
     * @param itemId
     * @return
     */
    @RequestMapping("deleteItemById")
    public Integer deleteItemById(Long itemId){
        return itemService.deleteItemById(itemId);
    }

    /**
     * 根据itemId查询商品信息
     * @param itemId
     * @return
     */
    @RequestMapping("/preUpdateItem")
    Map<String, Object> preUpdateItem(Long itemId){
        return itemService.preUpdateItem(itemId);
    }

    /**
     * 修改商品
     * @param tbItem
     * @param desc
     * @param itemParams
     * @return
     */
    @RequestMapping("/updateTbItem")
    Integer updateTbItem(@RequestBody TbItem tbItem,String desc,String itemParams){
        return itemService.updateTbItem(tbItem,desc,itemParams);
    }


}
