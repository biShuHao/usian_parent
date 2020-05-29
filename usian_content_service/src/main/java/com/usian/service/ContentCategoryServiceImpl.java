package com.usian.service;

import com.usian.mapper.TbContentCategoryMapper;
import com.usian.mapper.TbContentMapper;
import com.usian.pojo.TbContent;
import com.usian.pojo.TbContentCategory;
import com.usian.pojo.TbContentCategoryExample;
import com.usian.pojo.TbContentExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional
public class ContentCategoryServiceImpl implements ContentCategoryService{

    @Autowired
    TbContentCategoryMapper tbContentCategoryMapper;

    @Override
    public List<TbContentCategory> selectContentCategoryByParentId(Long id) {
        TbContentCategoryExample example = new TbContentCategoryExample();
        TbContentCategoryExample.Criteria criteria = example.createCriteria();
        criteria.andParentIdEqualTo(id);
        List<TbContentCategory> tbContentCategoryList = tbContentCategoryMapper.selectByExample(example);
        System.out.println("------------------------------------------------------------------------------------------------------------" +
                "------------------------------------" +
                "------------------------------------------------------------------------------------------------" +
                "------------------------------------" +
                tbContentCategoryList);
        return tbContentCategoryList;
    }

    @Override
    public Integer insertContentCategory(TbContentCategory tbContentCategory) {
        //进行添加
        Date date = new Date();
        tbContentCategory.setIsParent(false);
        tbContentCategory.setCreated(date);
        tbContentCategory.setUpdated(date);
        tbContentCategory.setSortOrder(1);
        tbContentCategory.setIsParent(false);
        tbContentCategory.setStatus(1);
        int i = tbContentCategoryMapper.insertSelective(tbContentCategory);

        //将父节点isParent设为1
        TbContentCategory tbContentCategory1 = tbContentCategoryMapper.selectByPrimaryKey(tbContentCategory.getParentId());
        if(!tbContentCategory1.getIsParent()){
            tbContentCategory1.setIsParent(true);
            tbContentCategory1.setUpdated(date);
            tbContentCategoryMapper.updateByPrimaryKeySelective(tbContentCategory1);
        }
        return i;
    }


    @Override
    public Integer deleteContentCategoryById(Long categoryId) {
        //先判断该节点是否是父节点
        TbContentCategory tbContentCategory = tbContentCategoryMapper.selectByPrimaryKey(categoryId);
        if(tbContentCategory.getIsParent()){
            return 0;
        }
        //不是父节点就删除
        tbContentCategoryMapper.deleteByPrimaryKey(categoryId);
        //当删除该节点后，还需要判断该节点的父节点还有子节点吗
        TbContentCategoryExample example = new TbContentCategoryExample();
        TbContentCategoryExample.Criteria criteria = example.createCriteria();
        criteria.andParentIdEqualTo(tbContentCategory.getParentId());
        //先查询父ID相同的兄弟节点
        List<TbContentCategory> list = tbContentCategoryMapper.selectByExample(example);
        //如果集合为空，代表父节点没有孩子了，修改isParent为false
        if(list == null || list.size()==0){
            TbContentCategory parenttbContentCategory = new TbContentCategory();
            parenttbContentCategory.setId(tbContentCategory.getParentId());
            Date date = new Date();
            parenttbContentCategory.setUpdated(date);
            parenttbContentCategory.setIsParent(false);
            tbContentCategoryMapper.updateByPrimaryKeySelective(parenttbContentCategory);
        }
        return 1;
    }

    @Override
    public Integer updateContentCategory(TbContentCategory tbContentCategory) {
        tbContentCategory.setUpdated(new Date());
        return tbContentCategoryMapper.updateByPrimaryKeySelective(tbContentCategory);
    }


}
