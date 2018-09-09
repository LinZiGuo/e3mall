package cn.e3mall.content.service;

import java.util.List;

import cn.e3mall.common.pojo.EasyUIDataGridResult;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.pojo.TbContent;

public interface ContentService {

	public EasyUIDataGridResult getContentListByCategoryId(long categoryId, Integer page, Integer rows);

	public E3Result addContent(TbContent content);

	public E3Result editContent(TbContent content);

	public E3Result deleteContent(long[] ids);
	
	public List<TbContent> getContentListByCid(long categoryId);

}
