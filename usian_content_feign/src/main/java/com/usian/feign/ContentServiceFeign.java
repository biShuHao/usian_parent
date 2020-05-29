package com.usian.feign;

import com.usian.pojo.TbContent;
import com.usian.pojo.TbContentCategory;
import com.usian.utils.AdNode;
import com.usian.utils.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient("usian-content-service")
public interface ContentServiceFeign {

    /**
     * 分类内容管理查询
     * @param id
     * @return
     */
    @RequestMapping("/service/contentCategory/selectContentCategoryByParentId")
    List<TbContentCategory> selectContentCategoryByParentId(@RequestParam Long id);

    /**
     * 分类内容管理添加
     * @param tbContentCategory
     * @return
     */
    @RequestMapping("/service/contentCategory/insertContentCategory")
    Integer insertContentCategory(TbContentCategory tbContentCategory);

    /**
     * 分类内容管理删除
     * @param categoryId
     * @return
     */
    @RequestMapping("/service/contentCategory/deleteContentCategoryById")
    Integer deleteContentCategoryById(@RequestParam Long categoryId);

    /**
     * 分类内容管理修改
     * @param tbContentCategory
     * @return
     */
    @RequestMapping("/service/contentCategory/updateContentCategory")
    Integer updateContentCategory(TbContentCategory tbContentCategory);

    /**
     * 内容管理查询
     * @param categoryId
     * @return
     */
    @RequestMapping("/service/content/selectTbContentAllByCategoryId")
    PageResult selectTbContentAllByCategoryId(@RequestParam Integer page,@RequestParam Integer rows,@RequestParam Long categoryId);

    /**
     * 内容管理添加
     * @param tbContent
     * @return
     */
    @RequestMapping("/service/content/insertTbContent")
    Integer insertTbContent(TbContent tbContent);

    /**
     * 内容管理删除
     * @param ids
     * @return
     */
    @RequestMapping("/service/content/deleteContentByIds")
    Integer deleteContentByIds(@RequestParam Long ids);

    /**
     * 大广告
     * @return
     */
    @RequestMapping("/service/content/selectFrontendContentByAD")
    List<AdNode> selectFrontendContentByAD();
}
