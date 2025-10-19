package com.imooc.mumushengxian.Service.Impl;

import com.imooc.mumushengxian.Service.CartService;
import com.imooc.mumushengxian.exception.ImoocMallException;
import com.imooc.mumushengxian.exception.ImoocMallExceptionEnum;
import com.imooc.mumushengxian.model.dao.CartMapper;
import com.imooc.mumushengxian.model.dao.ProductMapper;
import com.imooc.mumushengxian.model.pojo.Cart;
import com.imooc.mumushengxian.model.pojo.Product;
import com.imooc.mumushengxian.model.vo.CartVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sound.midi.MidiFileFormat;
import java.util.List;

@Service
public class CartServiceImpl implements CartService {
    @Autowired
    private CartMapper cartMapper;
    @Autowired
    private ProductMapper productMapper;

    @Override
    public List<CartVO> cartList(Integer userId) throws ImoocMallException {
        List<CartVO> cartVOList = cartMapper.selectList(userId);
        //用于存储cart转化为cartVO后的列表
        if (cartVOList == null || cartVOList.isEmpty()) {
            throw new ImoocMallException(ImoocMallExceptionEnum.CART_EMPTY);
        }
        for (CartVO cartVO : cartVOList) {
            cartVO.setTotalPrice(cartVO.getPrice() * cartVO.getQuantity());
        }
        return cartVOList;
    }

    @Override
    public List<CartVO> addCart(Integer productId, Integer count, Integer userId) throws ImoocMallException {
        //1.检查商品是否在售、是否有库存
        Product product = productMapper.selectByPrimaryKey(productId);
        boolean result = isStockAndStatus(product.getId(), count);
        if (result) {
            //2.检查商品之前是否在购物车里：是，则在原有基础上添加数量；否，添加新商品；
            Cart cart = cartMapper.selectByPidAndUid(productId, userId);
            if (cart == null) {
                //添加新商品
                Cart newCart = new Cart();
                newCart.setQuantity(count);
                newCart.setUserId(userId);
                newCart.setProductId(productId);
                newCart.setSelected(1);
                cartMapper.insertSelective(newCart);
            } else {
                //更新商品的数量
                Cart newCart = new Cart();
                //在原有的商品数量基础上相加
                newCart.setQuantity(cart.getQuantity() + count);
                newCart.setId(cart.getId());
                newCart.setUserId(cart.getUserId());
                newCart.setProductId(cart.getProductId());
                newCart.setSelected(1);
                cartMapper.updateByPrimaryKeySelective(newCart);
            }
        }
        //3.返回购物车列表
        return this.cartList(userId);
    }

    @Override
    public List<CartVO> updateCart(Integer productId, Integer count, Integer userId) throws ImoocMallException {
        //1.检查商品是否在售、是否有库存
        Product product = productMapper.selectByPrimaryKey(productId);
        boolean result = isStockAndStatus(product.getId(), count);
        if (result) {
            //更新商品的数量
            Cart cart = cartMapper.selectByPidAndUid(productId, userId);
            Cart newCart = new Cart();
            //更新商品数量
            newCart.setQuantity(count);
            newCart.setId(cart.getId());
            newCart.setUserId(cart.getUserId());
            newCart.setProductId(cart.getProductId());
            newCart.setSelected(1);
            cartMapper.updateByPrimaryKeySelective(newCart);
        }
        //3.返回购物车列表
        return this.cartList(userId);
    }

    @Override
    public List<CartVO> deleteCart(Integer productId, Integer userId) throws ImoocMallException {
        Cart cart = cartMapper.selectByPidAndUid(productId, userId);
        if (cart == null) {
            throw new ImoocMallException(ImoocMallExceptionEnum.CART_NOT_EXISTED);
        }
        int count = cartMapper.deleteByPrimaryKey(cart.getId());
        if (count == 0) {
            throw new ImoocMallException(ImoocMallExceptionEnum.Cart_DELETED_FAILED);
        }
        //3.返回购物车列表
        return this.cartList(userId);
    }

    @Override
    public List<CartVO> selectCart(Integer productId, Integer selected, Integer userId) throws ImoocMallException {
        Cart cart = cartMapper.selectByPidAndUid(productId, userId);
        if (cart == null) {
            throw new ImoocMallException(ImoocMallExceptionEnum.CART_NOT_EXISTED);
        }else{
            cartMapper.selectOrNot(userId, productId, selected);
        }
        return this.cartList(userId);
    }

    @Override
    public List<CartVO> selectAllCart(Integer selected, Integer userId) throws ImoocMallException {
        List<CartVO> cartVOList = cartMapper.selectList(userId);
        if (cartVOList == null) {
            //购物车中没有商品，空的
            throw new ImoocMallException(ImoocMallExceptionEnum.CART_NOT_EXISTED);
        }else{
            cartMapper.selectOrNot(userId, null, selected);
        }
        return this.cartList(userId);
    }

    /**
     * 检验商品库存和售卖状态
     *
     * @param productId
     * @param count
     * @return
     */
    private boolean isStockAndStatus(Integer productId, Integer count) {
        Product product = productMapper.selectByPrimaryKey(productId);
        Integer stock = product.getStock();
        if (stock < count || product.getStatus() != 1) {
            return false;
        }
        return true;
    }
}
