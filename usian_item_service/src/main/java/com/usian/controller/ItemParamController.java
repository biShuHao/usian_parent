package com.usian.controller;

import com.usian.pojo.TbItemParam;
import com.usian.service.ItemParamService;
import com.usian.utils.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/service/itemParam")
@RestController
public class ItemParamController {

    @Autowired
    private ItemParamService itemParamService;

    /**
     * 根据商品分类ID查询规格参数模板
     */
    @RequestMapping("/selectItemParamByItemCatId/{itemCatId}")
    public TbItemParam selectItemParamByItemCatId(@PathVariable Long itemCatId){
        return itemParamService.selectItemParamByItemCatId(itemCatId);
    }

    /**
     * 查询商品规格模板
     * @param page
     * @param rows
     * @return
     */
    @RequestMapping("selectItemParamAll")
    public PageResult selectItemParamAll(@RequestParam Integer page,
                                         @RequestParam Integer rows){
        return itemParamService.selectItemParamAll(page,rows);
    };

    /**
     * 添加商品规格模板
     * @param itemCatId
     * @param paramData
     * @return
     */
    @RequestMapping("insertItemParam")
    public Integer insertItemParam(Long itemCatId,String paramData){
        return itemParamService.insertItemParam(itemCatId,paramData);
    }

    @RequestMapping("deleteItemParamById")
    public Integer deleteItemParamById(@RequestParam Long id){
        return itemParamService.deleteItemParamById(id);
    }

}
