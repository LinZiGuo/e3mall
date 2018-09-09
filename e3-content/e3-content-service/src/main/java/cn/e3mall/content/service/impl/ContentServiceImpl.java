package cn.e3mall.content.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import cn.e3mall.common.pojo.EasyUIDataGridResult;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.common.utils.IDUtils;
import cn.e3mall.content.service.ContentService;
/**
 * 内容管理Service
 * @author 郭子灵
 *
 */
import cn.e3mall.mapper.TbContentMapper;
import cn.e3mall.pojo.TbContent;
import cn.e3mall.pojo.TbContentExample;
@Service
public class ContentServiceImpl implements ContentService {
	@Autowired
	private TbContentMapper contentMapper;

	/**
	 * 分页查询
	 */
	public EasyUIDataGridResult getContentListByCategoryId(long categoryId, Integer page, Integer rows) {
		PageHelper.startPage(page, rows);
		List<TbContent> list = getContentListByCid(categoryId);
		EasyUIDataGridResult result = new EasyUIDataGridResult();
		result.setRows(list);
		PageInfo<TbContent> pageInfo = new PageInfo<>(list);
		long total = pageInfo.getTotal();
		result.setTotal(total);
		return result;
	}

	/**
	 * 添加内容
	 */
	public E3Result addContent(TbContent content) {
		content.setId(IDUtils.genItemId());
		Date date = new Date();
		content.setCreated(date);
		content.setUpdated(date);
		contentMapper.insert(content);
		return E3Result.ok();
	}

	/**
	 * 编辑内容
	 */
	public E3Result editContent(TbContent content) {
		content.setUpdated(new Date());
		contentMapper.updateByPrimaryKeySelective(content);
		return E3Result.ok();
	}

	/**
	 * 删除内容
	 */
	public E3Result deleteContent(long[] ids) {
		for (long id : ids) {
			contentMapper.deleteByPrimaryKey(id);
		}
		return E3Result.ok();
	}

	/**
	 * 根据分类ID查询内容
	 */
	public List<TbContent> getContentListByCid(long categoryId) {
		TbContentExample example = new TbContentExample();
		example.createCriteria().andCategoryIdEqualTo(categoryId);
		List<TbContent> list = contentMapper.selectByExampleWithBLOBs(example);
		return list;
	}
}
