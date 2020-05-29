package com.usian.controller;

import com.usian.feign.ContentServiceFeign;
import com.usian.pojo.TbContent;
import com.usian.pojo.TbContentCategory;
import com.usian.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/backend/contentCategory")
public class ContentCategoryController {

    @Autowired
    private ContentServiceFeign contentServiceFeign;

    /**
     * 根据当前节点 ID 查询子节点
     * @param id
     * @return
     */
    @RequestMapping("/selectContentCategoryByParentId")
    public Result selectContentCategoryByParentId(@RequestParam(defaultValue = "0") Long id){
        List<TbContentCategory> list = contentServiceFeign.selectContentCategoryByParentId(id);
        if(list!=null && list.size()>0){
            return Result.ok(list);
        }
        return Result.error("查无结果");
    }

    /**
     * 分类内容添加
     * @param tbContentCategory
     * @return
     */
    @RequestMapping("insertContentCategory")
    public Result insertContentCategory(TbContentCategory tbContentCategory){
        Integer num = contentServiceFeign.insertContentCategory(tbContentCategory);
        if(num == 1){
            return Result.ok();
        }
        return Result.error("添加失败");
    }

    /**
     * 分类内容删除
     * @param categoryId
     * @return
     */
    @RequestMapping("deleteContentCategoryById")
    public Result deleteContentCategoryById(Long categoryId){
        Integer num = contentServiceFeign.deleteContentCategoryById(categoryId);
        if(num == 1){
            return  Result.ok();
        }
        return Result.error("删除失败");
    }

    /**
     * 分类内容修改
     * @param tbContentCategory
     * @return
     */
    @RequestMapping("updateContentCategory")
    public Result updateContentCategory(TbContentCategory tbContentCategory){
        Integer num = contentServiceFeign.updateContentCategory(tbContentCategory);
        if(num == 1){
            return  Result.ok();
        }
        return Result.error("修改失败");
    }


}
