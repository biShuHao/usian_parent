package com.usian.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.usian.mapper.*;
import com.usian.pojo.*;
import com.usian.utils.IDUtils;
import com.usian.utils.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class ItemServiceImpl implements ItemService{

    @Autowired
    TbItemMapper itemMapper;

    @Autowired
    TbItemDescMapper itemDescMapper;

    @Autowired
    TbItemParamItemMapper itemParamItemMapper;

    @Autowired
    TbItemCatMapper itemCatMapper;

    @Override
    public TbItem selectItemInfo(Long itemId) {
        return itemMapper.selectByPrimaryKey(itemId);
    }

    @Override
    public PageResult selectTbItemAllByPage(Integer page, Long rows) {
        PageHelper.startPage(page,rows.intValue());
        TbItemExample example = new TbItemExample();
        example.setOrderByClause("updated desc");
        TbItemExample.Criteria criteria = example.createCriteria();
        criteria.andStatusEqualTo((byte)1);
        List<TbItem> tbItems = itemMapper.selectByExample(example);
        PageInfo<TbItem> pageInfo = new PageInfo<>(tbItems);
        PageResult pageResult = new PageResult();
        pageResult.setPageIndex(pageInfo.getPageNum());
        pageResult.setResult(pageInfo.getList());
        pageResult.setTotalPage(Long.valueOf(pageInfo.getPages()));
        return pageResult;
    }

    @Override
    public Integer insertTbItem(TbItem tbItem, String desc, String itemParams) {
        long itemId = IDUtils.genItemId();
        Date date = new Date();
        //补充TbItem数据
        tbItem.setId(itemId);
        tbItem.setStatus((byte)1);
        tbItem.setUpdated(date);
        tbItem.setCreated(date);
        tbItem.setImage(tbItem.getImage());
        tbItem.setPrice(tbItem.getPrice()*100);
        int tbItemNum = itemMapper.insertSelective(tbItem);
        //补齐商品描述对象
        TbItemDesc itemDesc = new TbItemDesc();
        itemDesc.setItemId(itemId);
        itemDesc.setItemDesc(desc);
        itemDesc.setCreated(date);
        itemDesc.setUpdated(date);
        int tbitemDescNum = itemDescMapper.insertSelective(itemDesc);
        //补齐商品规格参数
        TbItemParamItem tbItemParamItem = new TbItemParamItem();
        tbItemParamItem.setCreated(date);
        tbItemParamItem.setUpdated(date);
        tbItemParamItem.setItemId(itemId);
        tbItemParamItem.setParamData(itemParams);
        int itemParamItmeNum = itemParamItemMapper.insertSelective(tbItemParamItem);
        return tbItemNum+tbitemDescNum+itemParamItmeNum;
    }

    @Override
    public Integer deleteItemById(Long itemId) {
        return itemMapper.deleteByPrimaryKey(itemId);
    }

    @Override
    public Map<String, Object> preUpdateItem(Long itemId) {
        Map<String, Object> map = new HashMap<>();
        //根据商品 ID 查询商品
        TbItem item = this.itemMapper.selectByPrimaryKey(itemId);
        map.put("item", item);
        //根据商品 ID 查询商品描述
        TbItemDesc itemDesc = this.itemDescMapper.selectByPrimaryKey(itemId);
        map.put("itemDesc", itemDesc.getItemDesc());
        //根据商品 ID 查询商品类目
        TbItemCat itemCat = this.itemCatMapper.selectByPrimaryKey(item.getCid());
        map.put("itemCat", itemCat.getName());
        //根据商品 ID 查询商品规格参数
        TbItemParamItemExample example = new TbItemParamItemExample();
        TbItemParamItemExample.Criteria criteria = example.createCriteria();
        criteria.andItemIdEqualTo(itemId);
        List<TbItemParamItem> list =
                this.itemParamItemMapper.selectByExampleWithBLOBs(example);
        if (list != null && list.size() > 0) {
            map.put("itemParamItem", list.get(0).getParamData());
        }
        return map;
    }

    @Override
    public Integer updateTbItem(TbItem tbItem, String desc, String itemParams) {
        Date date = new Date();
        tbItem.setUpdated(date);
        int tbItemNum = itemMapper.updateByPrimaryKeySelective(tbItem);
        //补齐商品描述对象
        TbItemDesc itemDesc = new TbItemDesc();
        itemDesc.setItemId(tbItem.getId());
        itemDesc.setItemDesc(desc);
        itemDesc.setUpdated(date);
        int tbitemDescNum = itemDescMapper.updateByPrimaryKeySelective(itemDesc);
        //补齐商品规格参数
        TbItemParamItemExample example = new TbItemParamItemExample();
        TbItemParamItemExample.Criteria criteria = example.createCriteria();
        criteria.andItemIdEqualTo(tbItem.getId());
        List<TbItemParamItem> itemParamItems = itemParamItemMapper.selectByExampleWithBLOBs(example);

        TbItemParamItem tbItemParamItem = new TbItemParamItem();
        tbItemParamItem.setId(itemParamItems.get(0).getId());
        tbItemParamItem.setUpdated(date);
        tbItemParamItem.setItemId(tbItem.getId());
        tbItemParamItem.setParamData(itemParams);
        int itemParamItmeNum = itemParamItemMapper.updateByPrimaryKeySelective(tbItemParamItem);
        return tbItemNum+tbitemDescNum+itemParamItmeNum;
    }


}
