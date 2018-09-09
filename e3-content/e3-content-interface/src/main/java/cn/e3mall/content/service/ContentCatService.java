package cn.e3mall.content.service;

import java.util.List;

import cn.e3mall.common.pojo.EasyUITreeNode;
import cn.e3mall.common.utils.E3Result;

public interface ContentCatService {

	public List<EasyUITreeNode> getContentCatList(long parentId);

	public E3Result addContentCat(long id, String name);

	public E3Result updateContentCat(long id, String name);

	public E3Result deleteContentCat(long id);

}
