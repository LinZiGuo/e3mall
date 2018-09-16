package cn.e3mall.cart.service;

import java.util.List;

import cn.e3mall.common.utils.E3Result;
import cn.e3mall.pojo.TbItem;

public interface CartService {

	E3Result addCart(Long id, Long itemId, Integer num);

	E3Result mergeCart(Long id, List<TbItem> cartList);

	List<TbItem> getCartList(Long id);

	E3Result updateCartNum(Long id, Long itemId, Integer num);

	E3Result deleteCartItem(Long id, Long itemId);

}
