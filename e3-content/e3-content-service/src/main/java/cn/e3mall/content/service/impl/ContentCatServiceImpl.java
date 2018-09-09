package cn.e3mall.content.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.e3mall.common.pojo.EasyUITreeNode;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.common.utils.IDUtils;
import cn.e3mall.content.service.ContentCatService;
import cn.e3mall.mapper.TbContentCategoryMapper;
import cn.e3mall.pojo.TbContentCategory;
import cn.e3mall.pojo.TbContentCategoryExample;
@Service
public class ContentCatServiceImpl implements ContentCatService {
	@Autowired
	private TbContentCategoryMapper contentCategoryMapper;
	
	/**
	 * 获取内容分类列表
	 */
	public List<EasyUITreeNode> getContentCatList(long parentId) {
		TbContentCategoryExample example = new TbContentCategoryExample();
		example.createCriteria().andParentIdEqualTo(parentId);
		List<TbContentCategory> list = contentCategoryMapper.selectByExample(example);
		List<EasyUITreeNode> resultList = new ArrayList<>();
		for (TbContentCategory contentCat : list) {
			EasyUITreeNode node = new EasyUITreeNode();
			node.setId(contentCat.getId());
			node.setText(contentCat.getName());
			node.setState(contentCat.getIsParent()?"closed":"open");
			resultList.add(node);
		}
		return resultList;
	}

	/**
	 * 新增内容类别节点
	 */
	public E3Result addContentCat(long id, String name) {
		//1.接受两个参数：id，name
		//2.向tb_content_category表中插入数据
		//2.1 创建一个TbContentCategory对象
		TbContentCategory category = new TbContentCategory();
		Date date = new Date();
		//2.2 补全TbContentCategory对象的属性
		category.setId(IDUtils.genItemId());
		category.setParentId(id);
		category.setName(name);
		category.setIsParent(false);
		category.setCreated(date);
		category.setUpdated(date);
		//2.3 排列序号，表示同级类目的展现次序，如数值相等则按名称次序排列。取值范围:大于零的整数
		category.setSortOrder(1);
		//2.4 状态。可选值:1(正常),2(删除)
		category.setStatus(1);
		//2.5 向tb_content_category表中插入数据
		contentCategoryMapper.insert(category);
		//3.判断父节点的isparent是否为true，不是true需要改为true。
		TbContentCategory parentNode = contentCategoryMapper.selectByPrimaryKey(id);
		if (!parentNode.getIsParent()) {
			parentNode.setIsParent(true);
			//3.1 更新父节点
			contentCategoryMapper.updateByPrimaryKeySelective(parentNode);
		}
		//4.需要主键返回。
		//5.返回E3Result，其中包装TbContentCategory对象
		return E3Result.ok(category);
	}

	/**
	 * 重命名内容类别节点
	 */
	public E3Result updateContentCat(long id, String name) {
		TbContentCategory category = contentCategoryMapper.selectByPrimaryKey(id);
		Date date = new Date();
		category.setUpdated(date);
		category.setName(name);
		contentCategoryMapper.updateByPrimaryKey(category);
		return E3Result.ok(category);
	}

	/**
	 * 删除内容类别节点
	 */
	public E3Result deleteContentCat(long id) {
		//查询被删节点的全部信息
		TbContentCategory category = contentCategoryMapper.selectByPrimaryKey(id);
		//得到被删节点的父节点ID
		long parentid = category.getParentId();
		//判断是否有子节点
		if (category.getIsParent()) {
			//若被删节点有子节点，则不允许删除
			return E3Result.build(1, "不能删除！");
		} else {
			//若被删节点无子节点，则允许删除
			contentCategoryMapper.deleteByPrimaryKey(id);
			//查询与被删节点同一父节点的全部信息
			TbContentCategoryExample example = new TbContentCategoryExample();
			example.createCriteria().andParentIdEqualTo(parentid);
			List<TbContentCategory> childrenList = contentCategoryMapper.selectByExample(example);
			if (childrenList.size() == 0) {
				//被删节点没有兄弟节点
				TbContentCategory parent = contentCategoryMapper.selectByPrimaryKey(parentid);
				if (parent.getIsParent()) {
					//判断父节点的isParent属性是否为false如果不是就修改为false
					parent.setIsParent(false);
					contentCategoryMapper.updateByPrimaryKey(parent);
				}
			}
		}
		//返回E3Result，其中包装TbContentCategory对象
		return E3Result.ok(category);
	}

}
