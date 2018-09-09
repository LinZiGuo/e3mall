package cn.e3mall.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.e3mall.common.pojo.EasyUIDataGridResult;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.content.service.ContentService;
import cn.e3mall.pojo.TbContent;
/**
 * 内容管理Controller
 * @author 郭子灵
 *
 */
@Controller
@RequestMapping("/content")
public class ContentController {
	@Autowired
	private ContentService contentService;
	
	/**
	 * 查询该分类下内容列表
	 * @param categoryId
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping("/list")
	@ResponseBody
	public EasyUIDataGridResult getContentList(long categoryId,Integer page, Integer rows) {
		EasyUIDataGridResult list = contentService.getContentListByCategoryId(categoryId,page,rows);
		return list;
	}
	
	/**
	 * 添加内容
	 * @param content	内容
	 * @return
	 */
	@RequestMapping(value="/save",method=RequestMethod.POST)
	@ResponseBody
	public E3Result addContent(TbContent content) {
		E3Result result = contentService.addContent(content);
		return result;
	}
	
	/**
	 * 编辑内容
	 * @param content	内容
	 * @return
	 */
	@RequestMapping(value="/edit",method=RequestMethod.POST)
	@ResponseBody
	public E3Result editContent(TbContent content) {
		E3Result result = contentService.editContent(content);
		return result;
	}
	
	/**
	 * 删除内容
	 * @param content	内容
	 * @return
	 */
	@RequestMapping(value="/delete",method=RequestMethod.POST)
	@ResponseBody
	public E3Result deleteContent(long[] ids) {
		E3Result result = contentService.deleteContent(ids);
		return result;
	}
}
