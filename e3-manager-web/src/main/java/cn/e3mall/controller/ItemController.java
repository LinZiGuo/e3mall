package cn.e3mall.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.e3mall.common.pojo.EasyUIDataGridResult;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.service.ItemService;

/**
 * 商品管理Controller
 * <p>Title: ItemController</p>
 * <p>Description: </p>
 * <p>Company: www.itcast.cn</p> 
 * @version 1.0
 */
@Controller
@RequestMapping("/item")
public class ItemController {

	@Autowired
	private ItemService itemService;
	
	/**
	 * 根据ID查询商品
	 * @param itemId	商品ID
	 * @return
	 */
	@RequestMapping("/{itemId}")
	@ResponseBody
	public TbItem getItemById(@PathVariable Long itemId) {
		TbItem tbItem = itemService.getItemById(itemId);
		return tbItem;
	}
	
	/**
	 * 查询商品列表
	 * @param page	页码
	 * @param rows	行数
	 * @return
	 */
	@RequestMapping("/list")
	@ResponseBody
	public EasyUIDataGridResult getItemList(Integer page, Integer rows) {
		//调用服务查询商品列表
		EasyUIDataGridResult result = itemService.getItemList(page, rows);
		return result;
	}
	
	/**
	 * 新增商品
	 * @param item	商品
	 * @param desc	商品描述
	 * @return
	 */
	@RequestMapping(value="/save",method=RequestMethod.POST)
	@ResponseBody
	public E3Result addItem(TbItem item,String desc) {
		E3Result result = itemService.addItem(item,desc);
		return result;
	}
	
	/**
	 * 获取商品描述
	 * @param itemId	商品ID
	 * @return
	 */
	@RequestMapping("/desc/{itemId}")
	@ResponseBody
	public E3Result getItemDesc(@PathVariable long itemId) {
		E3Result result = itemService.getItemDescById(itemId);
		return result;
	}
	
	/**
	 * 获取商品规格
	 * @param itemId	商品ID
	 * @return
	 */
	@RequestMapping("/param/{itemId}")
	@ResponseBody
	public E3Result getItemParam(@PathVariable long itemId) {
		E3Result result = itemService.getItemParamItemById(itemId);
		return result;
	}
	
	/**
	 * 编辑商品
	 * @param item	商品
	 * @param desc	商品描述
	 * @return
	 */
	@RequestMapping(value="/update",method=RequestMethod.POST)
	@ResponseBody
	public E3Result editItem(TbItem item,String desc) {
		E3Result result = itemService.editItem(item,desc);
		return result;
	}
	
	/**
	 * 删除商品
	 * @param itemId	商品ID
	 * @return
	 */
	@RequestMapping("/delete")
	@ResponseBody
	public E3Result deleteItems(long[] ids) {
		E3Result result = itemService.updateItemById(ids,(byte) 3);
		return result;
	}
	
	/**
	 * 下架
	 * @param itemId	商品ID
	 * @return
	 */
	@RequestMapping("/instock")
	@ResponseBody
	public E3Result instockItems(long[] ids) {
		E3Result result = itemService.updateItemById(ids,(byte) 2);
		return result;
	}
	
	/**
	 * 上架
	 * @param itemId	商品ID
	 * @return
	 */
	@RequestMapping("/reshelf")
	@ResponseBody
	public E3Result reshelfItems(long[] ids) {
		E3Result result = itemService.updateItemById(ids,(byte) 1);
		return result;
	}
}
