package cn.e3mall.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import cn.e3mall.common.jedis.JedisClient;
import cn.e3mall.common.pojo.EasyUIDataGridResult;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.common.utils.IDUtils;
import cn.e3mall.common.utils.JsonUtils;
import cn.e3mall.mapper.TbItemDescMapper;
import cn.e3mall.mapper.TbItemMapper;
import cn.e3mall.mapper.TbItemParamItemMapper;
import cn.e3mall.pojo.TbContent;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbItemDesc;
import cn.e3mall.pojo.TbItemDescExample;
import cn.e3mall.pojo.TbItemExample;
import cn.e3mall.pojo.TbItemExample.Criteria;
import cn.e3mall.pojo.TbItemParamItem;
import cn.e3mall.pojo.TbItemParamItemExample;
import cn.e3mall.service.ItemService;
/**
 * 商品管理Service
 * @author 郭子灵
 *
 */
@Service
public class ItemServiceImpl implements ItemService {
	@Autowired
	private TbItemMapper itemMapper;
	@Autowired
	private TbItemDescMapper itemDescMapper;
	@Autowired
	private TbItemParamItemMapper itemParamItemMapper;
	@Autowired
	private JedisClient jedisClient;
	@Value("${REDIS_ITEM_PRE}")
	private String REDIS_ITEM_PRE;
	@Value("${ITEM_CACHE_EXPIRE}")
	private Integer ITEM_CACHE_EXPIRE;
	
	/**
	 * 根据Id查询商品
	 */
	public TbItem getItemById(long itemId) {
		// 查询缓存
		try {
			String json = jedisClient.get(REDIS_ITEM_PRE + ":" + itemId + ":BASE");
			if (StringUtils.isNotBlank(json)) {
				TbItem tbItem = JsonUtils.jsonToPojo(json, TbItem.class);
				return tbItem;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		TbItemExample example = new TbItemExample();
		Criteria criteria = example.createCriteria();
		
		//设置查询条件
		criteria.andIdEqualTo(itemId);
		
		//执行查询
		List<TbItem> list = itemMapper.selectByExample(example);
		if (list != null && list.size() > 0) {
			// 把结果添加到缓存
			try {
				jedisClient.set(REDIS_ITEM_PRE + ":" + itemId + ":BASE", JsonUtils.objectToJson(list.get(0)));
				// 设置过期时间
				jedisClient.expire(REDIS_ITEM_PRE + ":" + itemId + ":BASE", ITEM_CACHE_EXPIRE);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return list.get(0);
		}
		return null;
	}
	
	/**
	 * 获取商品列表
	 */
	public EasyUIDataGridResult getItemList(Integer page, Integer rows) {
		//设置分页信息
		PageHelper.startPage(page, rows);
		//执行查询
		TbItemExample example = new TbItemExample();
		example.createCriteria().andStatusNotEqualTo((byte) 3);
		List<TbItem> list = itemMapper.selectByExample(example);
		//创建一个返回值对象
		EasyUIDataGridResult result = new EasyUIDataGridResult();
		result.setRows(list);
		//取分页结果
		PageInfo<TbItem> pageInfo = new PageInfo<>(list);
		//取总记录数
		long total = pageInfo.getTotal();
		result.setTotal(total);
		return result;
	}

	/**
	 * 添加商品
	 */
	public E3Result addItem(TbItem item, String desc) {
		//1 生成商品id
		long itemid = IDUtils.genItemId();
		//2 补全TbItem对象的属性
		item.setId(itemid);
		//商品状态，1-正常，2-下架，3-删除
		item.setStatus((byte)1);
		Date date = new Date();
		item.setCreated(date);
		item.setUpdated(date);
		//3 向商品表插入数据
		itemMapper.insert(item);
		//4 创建一个TbItemDesc对象
		TbItemDesc itemDesc = new TbItemDesc();
		//5 补全TbItemDesc对象的属性
		itemDesc.setItemId(itemid);
		itemDesc.setItemDesc(desc);
		itemDesc.setCreated(date);
		itemDesc.setUpdated(date);
		//6 向商品描述表插入数据
		itemDescMapper.insert(itemDesc);
		//缓存同步,删除缓存中对应的数据。
		jedisClient.del(REDIS_ITEM_PRE + ":" + itemid + ":BASE");
		//7 返回成功，E3Result.ok()
		return E3Result.ok();
	}

	/**
	 * 根据ID查询商品描述
	 */
	public E3Result getItemDescById(long itemId) {
		// 查询缓存
		try {
			String json = jedisClient.get(REDIS_ITEM_PRE + ":" + itemId + ":DESC");
			if (StringUtils.isNotBlank(json)) {
				TbItemDesc tbItemDesc = JsonUtils.jsonToPojo(json, TbItemDesc.class);
				return E3Result.ok(tbItemDesc);
			} else {
				TbItemDescExample example = new TbItemDescExample();
				example.createCriteria().andItemIdEqualTo(itemId);
				List<TbItemDesc> list = itemDescMapper.selectByExampleWithBLOBs(example);
				if (list != null && list.size() > 0) {
					// 把结果添加到缓存
					try {
						jedisClient.set(REDIS_ITEM_PRE + ":" + itemId + ":DESC", JsonUtils.objectToJson(list.get(0)));
						// 设置过期时间
						jedisClient.expire(REDIS_ITEM_PRE + ":" + itemId + ":DESC", ITEM_CACHE_EXPIRE);
					} catch (Exception e) {
						e.printStackTrace();
					}
					return E3Result.ok(list.get(0));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 根据ID查询商品规格
	 */
	public E3Result getItemParamItemById(long itemId) {
		// 查询缓存
		try {
			String json = jedisClient.get(REDIS_ITEM_PRE + ":" + itemId + ":ItemParamItem");
			if (StringUtils.isNotBlank(json)) {
				TbItemParamItem tbItemParamItem = JsonUtils.jsonToPojo(json, TbItemParamItem.class);
				return E3Result.ok(tbItemParamItem);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		TbItemParamItemExample example = new TbItemParamItemExample();
		example.createCriteria().andIdEqualTo(itemId);
		List<TbItemParamItem> list = itemParamItemMapper.selectByExampleWithBLOBs(example);
		if (list != null && list.size() > 0) {
			// 把结果添加到缓存
			try {
				jedisClient.set(REDIS_ITEM_PRE + ":" + itemId + ":ItemParamItem", JsonUtils.objectToJson(list.get(0)));
				// 设置过期时间
				jedisClient.expire(REDIS_ITEM_PRE + ":" + itemId + ":ItemParamItem", ITEM_CACHE_EXPIRE);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return E3Result.ok(list.get(0));
		}
		return null;
	}

	/**
	 * 修改商品
	 */
	public E3Result editItem(TbItem item, String desc) {
		Date date = new Date();
		item.setUpdated(date);
		itemMapper.updateByPrimaryKeySelective(item);
		TbItemDesc itemDesc = new TbItemDesc();
		itemDesc.setItemId(item.getId());
		itemDesc.setItemDesc(desc);
		itemDesc.setUpdated(date);
		itemDescMapper.updateByPrimaryKeySelective(itemDesc);
		//缓存同步,删除缓存中对应的数据。
		jedisClient.del(REDIS_ITEM_PRE + ":" + item.getId() + ":BASE");
		return E3Result.ok();
	}

	/**
	 * 根据商品ID修改商品状态，1-正常，2-下架，3-删除
	 */
	public E3Result updateItemById(long[] itemIds, byte status) {
		for (long id : itemIds) {
			TbItem item = itemMapper.selectByPrimaryKey(id);
			item.setStatus(status);
			item.setUpdated(new Date());
			itemMapper.updateByPrimaryKey(item);
			//缓存同步,删除缓存中对应的数据。
			jedisClient.del(REDIS_ITEM_PRE + ":" + id + ":BASE");
		}
		return E3Result.ok();
	}

}
