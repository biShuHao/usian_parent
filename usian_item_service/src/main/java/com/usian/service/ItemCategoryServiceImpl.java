package com.usian.service;

import com.usian.mapper.TbItemCatMapper;
import com.usian.pojo.TbItemCat;
import com.usian.pojo.TbItemCatExample;
import com.usian.redis.RedisClient;
import com.usian.utils.CatNode;
import com.usian.utils.CatResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.StyledEditorKit;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class ItemCategoryServiceImpl implements ItemCategoryService{

    @Autowired
    TbItemCatMapper tbItemCatMapper;

    @Autowired
    RedisClient redisClient;

    @Value("${PROTAL_CATRESULT_KEY}")
    private String PROTAL_CATRESULT_KEY;

    @Override
    public List<TbItemCat> selectItemCategoryByParentId(Long parentId) {
        TbItemCatExample example = new TbItemCatExample();
        TbItemCatExample.Criteria criteria = example.createCriteria();
        criteria.andParentIdEqualTo(parentId);
        criteria.andStatusEqualTo(1);
        return tbItemCatMapper.selectByExample(example);
    }

    //查询首页商品分类
    @Override
    public CatResult selectItemCategoryAll() {
        //查询缓存
        CatResult catResultResdis = (CatResult) redisClient.get(PROTAL_CATRESULT_KEY);
        //如果有缓存就直接返回
        if(catResultResdis!=null){
            return catResultResdis;
        }
        //如果没有则进行查询
        CatResult catResult = new CatResult();
        catResult.setData(getCatList(0L));
        //存入缓存
        redisClient.set(PROTAL_CATRESULT_KEY,catResult);
        return catResult;
    }

    /**
     * 私有方法，查询商品分类
     */
    private List<?> getCatList(Long parentId){
        TbItemCatExample example = new TbItemCatExample();
        TbItemCatExample.Criteria criteria = example.createCriteria();
        criteria.andParentIdEqualTo(parentId);
        List<TbItemCat> tbItemCatList = tbItemCatMapper.selectByExample(example);
        List list = new ArrayList();
        int count = 0;
        for (TbItemCat tbItemCat: tbItemCatList) {
            //判断是否是父节点
            if(tbItemCat.getIsParent()){
                CatNode catNode = new CatNode();
                catNode.setName(tbItemCat.getName());
                catNode.setItem(getCatList(tbItemCat.getId()));
                list.add(catNode);
                count++;
                if(count == 18){
                    break;
                }
            }else {
                list.add(tbItemCat.getName());
            }
        }
        return list;
    }

}
