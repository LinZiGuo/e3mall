package cn.e3mall.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.e3mall.common.utils.E3Result;
import cn.e3mall.search.service.SearchItemService;
/**
 * 导入商品数据到索引库
 * @author 郭子灵
 *
 */
@Controller
@RequestMapping("/index/item")
public class SearchItemController {
	@Autowired
	private SearchItemService searchItemService;
	
	@RequestMapping(value="/import",method=RequestMethod.POST)
	@ResponseBody
	public E3Result importItemList() {
		E3Result result = searchItemService.importAllItems();
		return result;
	}
}
