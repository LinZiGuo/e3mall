package cn.e3mall.content.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import cn.e3mall.common.jedis.JedisClient;
import cn.e3mall.common.pojo.EasyUIDataGridResult;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.common.utils.IDUtils;
import cn.e3mall.common.utils.JsonUtils;
import cn.e3mall.content.service.ContentService;
/**
 * 内容管理Service
 * @author 郭子灵
 *
 */
import cn.e3mall.mapper.TbContentMapper;
import cn.e3mall.pojo.TbContent;
import cn.e3mall.pojo.TbContentExample;
import cn.e3mall.pojo.TbContentExample.Criteria;
@Service
public class ContentServiceImpl implements ContentService {
	@Autowired
	private TbContentMapper contentMapper;
	@Autowired
	private JedisClient jedisClient;
	@Value("${CONTENT_LIST}")
	private String CONTENT_LIST;

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
		//缓存同步,删除缓存中对应的数据。
		jedisClient.hdel(CONTENT_LIST, content.getCategoryId().toString());
		return E3Result.ok();
	}

	/**
	 * 编辑内容
	 */
	public E3Result editContent(TbContent content) {
		content.setUpdated(new Date());
		contentMapper.updateByPrimaryKeySelective(content);
		//缓存同步,删除缓存中对应的数据。
		jedisClient.hdel(CONTENT_LIST, content.getCategoryId().toString());
		return E3Result.ok();
	}

	/**
	 * 删除内容
	 */
	public E3Result deleteContent(long[] ids) {
		for (long id : ids) {
			contentMapper.deleteByPrimaryKey(id);
			//缓存同步,删除缓存中对应的数据。
			jedisClient.hdel(CONTENT_LIST, String.valueOf(id));
		}
		return E3Result.ok();
	}

	/**
	 * 根据分类ID查询内容
	 */
	public List<TbContent> getContentListByCid(long categoryId) {
		//查询缓存
		try {
			//如果缓存中有直接响应结果
			String json = jedisClient.hget(CONTENT_LIST, categoryId + "");
			if (StringUtils.isNotBlank(json)) {
				List<TbContent> list = JsonUtils.jsonToList(json, TbContent.class);
				return list;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		//如果没有查询数据库
		TbContentExample example = new TbContentExample();
		Criteria criteria = example.createCriteria();
		//设置查询条件
		criteria.andCategoryIdEqualTo(categoryId);
		//执行查询
		List<TbContent> list = contentMapper.selectByExampleWithBLOBs(example);
		//把结果添加到缓存
		try {
			jedisClient.hset(CONTENT_LIST, categoryId + "", JsonUtils.objectToJson(list));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
}
