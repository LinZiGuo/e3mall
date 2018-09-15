package cn.e3mall.service;

import cn.e3mall.common.pojo.EasyUIDataGridResult;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbItemDesc;

public interface ItemService {

	public TbItem getItemById(long itemId);

	public EasyUIDataGridResult getItemList(Integer page, Integer rows);

	public E3Result addItem(TbItem item, String desc);

	public E3Result getItemDescById(long itemId);

	public E3Result getItemParamItemById(long itemId);

	public E3Result editItem(TbItem item, String desc);

	public E3Result updateItemById(long[] itemIds, byte status);
	
	public TbItemDesc getItemDesc(long itemId);
}
