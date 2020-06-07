package com.usian.service;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.usian.mapper.TbContentMapper;
import com.usian.pojo.TbContent;
import com.usian.pojo.TbContentExample;
import com.usian.redis.RedisClient;
import com.usian.utils.AdNode;
import com.usian.utils.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class ContentServiceImpl implements ContentService {

    @Value("${AD_CATEGORY_ID}")
    private Long AD_CATEGORY_ID;

    @Value("${AD_HEIGHT}")
    private Integer AD_HEIGHT;

    @Value("${AD_WIDTH}")
    private Integer AD_WIDTH;

    @Value("${AD_HEIGHTB}")
    private Integer AD_HEIGHTB;

    @Value("${AD_WIDTHB}")
    private Integer AD_WIDTHB;

    @Value("${PORTAL_AD_KEY}")
    private String PORTAL_AD_KEY;

    @Autowired
    TbContentMapper tbContentMapper;

    @Autowired
    private RedisClient redisClient;

    @Override
    public PageResult selectTbContentAllByCategoryId(Integer page,Integer rows,Long categoryId) {
        PageHelper.startPage(page,rows);
        TbContentExample example = new TbContentExample();
        example.setOrderByClause("updated desc");
        TbContentExample.Criteria criteria = example.createCriteria();
        criteria.andCategoryIdEqualTo(categoryId);
        List<TbContent> list = tbContentMapper.selectByExampleWithBLOBs(example);
        PageInfo<TbContent> pageInfo = new PageInfo<>(list);
        PageResult pageResult = new PageResult();
        pageResult.setResult(list);
        pageResult.setPageIndex(page);
        pageResult.setTotalPage(Long.valueOf(pageInfo.getPages()));
        return pageResult;
    }

    @Override
    public Integer insertTbContent(TbContent tbContent) {
        Date date = new Date();
        tbContent.setCreated(date);
        tbContent.setUpdated(date);
        int i = tbContentMapper.insertSelective(tbContent);
        deleteRedis();
        return i;
    }

    @Override
    public Integer deleteContentByIds(Long ids) {
        int i = tbContentMapper.deleteByPrimaryKey(ids);
        deleteRedis();
        return i;
    }

    //缓存同步
    private void deleteRedis(){
        redisClient.hdel(PORTAL_AD_KEY,AD_CATEGORY_ID.toString());
    }

    @Override
    public List<AdNode> selectFrontendContentByAD() {
        //查询缓存
        List<AdNode> list = (List<AdNode>) redisClient.hget(PORTAL_AD_KEY, AD_CATEGORY_ID.toString());
        //如果list有值则返回
        if(list!=null){
            return list;
        }
        //没值则查询数据库
        // 查询TbContent
        TbContentExample tbContentExample = new TbContentExample();
        TbContentExample.Criteria criteria = tbContentExample.createCriteria();
        criteria.andCategoryIdEqualTo(AD_CATEGORY_ID);
        List<TbContent> tbContentList =
                tbContentMapper.selectByExample(tbContentExample);
        List<AdNode> adNodeList = new ArrayList<AdNode>();
        for (TbContent tbContent : tbContentList) {
            AdNode adNode = new AdNode();
            adNode.setSrc(tbContent.getPic());
            adNode.setSrcB(tbContent.getPic2());
            adNode.setHref(tbContent.getUrl());
            adNode.setHeight(AD_HEIGHT);
            adNode.setWidth(AD_WIDTH);
            adNode.setHeightB(AD_HEIGHTB);
            adNode.setWidthB(AD_WIDTHB);
            adNodeList.add(adNode);
        }
        //添加到缓存
        redisClient.hset(PORTAL_AD_KEY,AD_CATEGORY_ID.toString(),adNodeList);
        return adNodeList;
    }


}
