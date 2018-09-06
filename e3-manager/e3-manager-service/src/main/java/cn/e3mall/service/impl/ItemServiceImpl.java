package cn.e3mall.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.e3mall.mapper.TbItemMapper;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbItemExample;
import cn.e3mall.pojo.TbItemExample.Criteria;
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
	/**
	 * 根据Id查询商品
	 */
	public TbItem getItemById(long itemId) {
		TbItemExample example = new TbItemExample();
		Criteria criteria = example.createCriteria();
		
		//设置查询条件
		criteria.andIdEqualTo(itemId);
		
		//执行查询
		List<TbItem> list = itemMapper.selectByExample(example);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

}
