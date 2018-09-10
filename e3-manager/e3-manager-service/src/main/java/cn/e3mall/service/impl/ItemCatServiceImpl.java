package cn.e3mall.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import cn.e3mall.common.jedis.JedisClient;
import cn.e3mall.common.pojo.EasyUITreeNode;
import cn.e3mall.common.utils.JsonUtils;
import cn.e3mall.mapper.TbItemCatMapper;
import cn.e3mall.pojo.TbContentCategory;
import cn.e3mall.pojo.TbItemCat;
import cn.e3mall.pojo.TbItemCatExample;
import cn.e3mall.pojo.TbItemCatExample.Criteria;
import cn.e3mall.service.ItemCatService;
/**
 * 商品類別管理Service
 * @author 郭子灵
 *
 */
@Service
public class ItemCatServiceImpl implements ItemCatService {
	@Autowired
	private TbItemCatMapper itemCatMapper;
	@Autowired
	private JedisClient jedisClient;
	@Value("${REDIS_ITEM_CAT_PRE}")
	private String REDIS_ITEM_CAT_PRE;
	@Value("${ITEM_CACHE_EXPIRE}")
	private Integer ITEM_CACHE_EXPIRE;

	/**
	 * 根據父節點獲取商品類別節點
	 */
	public List<EasyUITreeNode> getItemCatList(long parentId) {
		List<TbItemCat> list = null;
		//查询缓存
		try {
			//如果缓存中有直接响应结果
			String json = jedisClient.get(REDIS_ITEM_CAT_PRE + ":" + parentId + ":BASE");
			if (StringUtils.isNotBlank(json)) {
				list = JsonUtils.jsonToList(json, TbItemCat.class);
			} else {
				//1.根據parentId查詢節點列表
				TbItemCatExample example = new TbItemCatExample();
				//設置查詢條件
				Criteria criteria = example.createCriteria();
				criteria.andParentIdEqualTo(parentId);
				list = itemCatMapper.selectByExample(example);
				//把结果添加到缓存
				try {
					jedisClient.hset(REDIS_ITEM_CAT_PRE, parentId + "", JsonUtils.objectToJson(list));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		//2.轉換成EasyUITreeNode列表
		List<EasyUITreeNode> resultList = new ArrayList<>();
		for (TbItemCat itemCat : list) {
			EasyUITreeNode node = new EasyUITreeNode();
			node.setId(itemCat.getId());
			node.setText(itemCat.getName());
			node.setState(itemCat.getIsParent()?"closed":"open");
			//添加到列表
			resultList.add(node);
		}
		//3.返回
		return resultList;
	}
}
