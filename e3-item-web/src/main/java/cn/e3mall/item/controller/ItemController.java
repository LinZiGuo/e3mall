package cn.e3mall.item.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.e3mall.common.utils.E3Result;
import cn.e3mall.item.pojo.Item;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbItemDesc;
import cn.e3mall.service.ItemService;

/**
 * 商品详情Controller
 * @author 郭子灵
 *
 */
@Controller
@RequestMapping("/item")
public class ItemController {
	@Autowired
	private ItemService itemService;
	
	@RequestMapping("/{itemId}")
	public String showItemInfo(@PathVariable Long itemId, Model model) {
		//跟据商品id查询商品信息
		TbItem tbItem = itemService.getItemById(itemId);
		//把TbItem转换成Item对象
		Item item = new Item(tbItem);
		//根据商品id查询商品描述
		TbItemDesc desc = itemService.getItemDesc(itemId);
		//把数据传递给页面
		model.addAttribute("item", item);
		model.addAttribute("itemDesc", desc);
		return "item";
	}
}
