package cn.e3mall.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.e3mall.common.pojo.EasyUITreeNode;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.content.service.ContentCatService;

@Controller
@RequestMapping("/content/category")
public class ContentCatController {
	@Autowired
	private ContentCatService contentCatService;
	
	/**
	 * 获取内容分类列表
	 * @param parentId	父节点
	 * @return
	 */
	@RequestMapping("/list")
	@ResponseBody
	public List<EasyUITreeNode> getContentCatList(@RequestParam(value="id",defaultValue="0") long parentId) {
		List<EasyUITreeNode> list = contentCatService.getContentCatList(parentId);
		return list;
	}
	
	/**
	 * 新增内容类别节点
	 * @param id	节点ID
	 * @param name	节点名字
	 * @return
	 */
	@RequestMapping(value="/create",method=RequestMethod.POST)
	@ResponseBody
	public E3Result addContentCat(long parentId,String name) {
		E3Result result = contentCatService.addContentCat(parentId,name);
		return result;
	}
	
	/**
	 * 重命名内容类别节点
	 * @param id	节点ID
	 * @param name	节点名字
	 * @return
	 */
	@RequestMapping(value="/update",method=RequestMethod.POST)
	@ResponseBody
	public E3Result updateContentCat(long id,String name) {
		E3Result result = contentCatService.updateContentCat(id,name);
		return result;
	}
	
	/**
	 * 删除内容类别节点
	 * @param id	节点ID
	 * @return
	 */
	@RequestMapping(value="/delete",method=RequestMethod.POST)
	@ResponseBody
	public E3Result deleteContentCat(long id) {
		E3Result result = contentCatService.deleteContentCat(id);
		return result;
	}
}
