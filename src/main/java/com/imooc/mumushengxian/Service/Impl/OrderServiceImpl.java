package com.imooc.mumushengxian.Service.Impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.imooc.mumushengxian.Service.CartService;
import com.imooc.mumushengxian.Service.OrderService;
import com.imooc.mumushengxian.common.Constant;
import com.imooc.mumushengxian.exception.ImoocMallException;
import com.imooc.mumushengxian.exception.ImoocMallExceptionEnum;
import com.imooc.mumushengxian.model.dao.CartMapper;
import com.imooc.mumushengxian.model.dao.OrderItemMapper;
import com.imooc.mumushengxian.model.dao.OrderMapper;
import com.imooc.mumushengxian.model.dao.ProductMapper;
import com.imooc.mumushengxian.model.pojo.Order;
import com.imooc.mumushengxian.model.pojo.OrderItem;
import com.imooc.mumushengxian.model.pojo.Product;
import com.imooc.mumushengxian.model.request.CreateOrderReq;
import com.imooc.mumushengxian.model.vo.CartVO;
import com.imooc.mumushengxian.model.vo.OrderItemVO;
import com.imooc.mumushengxian.model.vo.OrderVO;
import com.imooc.mumushengxian.util.OrderCodeFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private  CartMapper cartMapper;
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private OrderItemMapper orderItemMapper;
    @Autowired
    private CartService cartService;

    /**
     * 创建订单
     * @param userId
     * @param createOrderReq
     * @return
     * @throws ImoocMallException
     */
    @Override
    public String createOrder(Integer userId, CreateOrderReq createOrderReq) throws ImoocMallException {
        //Step1.校验购物车商品：首先拉取用户购物车列表；其次筛选出已经被选中的购物车商品项；最后若无被选中的购物车，抛出异常；
        List<CartVO> cartVOList = cartService.cartList(userId);
        if (cartVOList==null) {
            throw new ImoocMallException(ImoocMallExceptionEnum.CART_EMPTY);
        }
        List<CartVO> selectedCartVOList = new ArrayList<>();
        for (CartVO cartVO : cartVOList) {
            if (cartVO.getSelected() == 1) {
                selectedCartVOList.add(cartVO);
            }
        }
        if (selectedCartVOList==null) {
            throw new ImoocMallException(ImoocMallExceptionEnum.CART_PRODUCT_EMPTY);
        }
        //Step2.验证选中的购物车列表中每一项商品：是否在售、库存是否充足；
        validvalidSaleStatusAndStock(selectedCartVOList);
        //Step3.库存扣减：遍历选中的购物车列表，对每一项商品进行口村扣减；
        for (CartVO cartVO : selectedCartVOList) {
            Product product = productMapper.selectByPrimaryKey(cartVO.getProductId());
            int newStock = product.getStock() - cartVO.getQuantity();
            if (newStock < 0) {
                throw new ImoocMallException(ImoocMallExceptionEnum.PRODUCT_NOT_ENOUGH);
            }
            product.setStock(newStock);
            productMapper.updateByPrimaryKeySelective(product);
        }
        //Step4.将选中的购物车列表转换为订单列表
         List<OrderItem> orderItemList = selectedCartVOListToOrderItemList(selectedCartVOList);
        //Step5.清理购物车
        cleanCart(selectedCartVOList);
        //Step6:生成订单号
        //通过 OrderCodeFactory.getOrderCode() 生成唯一订单号（结合用户ID）
        String orderNo = OrderCodeFactory.getOrderCode(Long.valueOf(userId));
        //Step7.创建主订单：设置各属性
        Order order = new Order();
        order.setOrderNo(orderNo);
        order.setUserId(userId);
        order.setTotalPrice(totalPrice(orderItemList));
        order.setReceiverName(createOrderReq.getReceiverName());
        order.setReceiverMobile(createOrderReq.getReceiverMobile());
        order.setReceiverAddress(createOrderReq.getReceiverAddress());

        order.setOrderStatus(Constant.OrderStatusEnum.NOT_PAID.getCode());
        order.setPostage(0);
        order.setPaymentType(1);
        orderMapper.insertSelective(order);

        //Step8.保存订单明细：订单明细关联主订单
        for (OrderItem orderItem : orderItemList) {
            orderItem.setOrderNo(orderNo);
            orderItemMapper.insertSelective(orderItem);
        }
        //Step9.返回订单号
        return orderNo;
    }

    /**
     * 订单详情
     * @param userId
     * @param orderNO
     * @return
     */
    @Override
    public OrderVO orderDetail(Integer userId, String orderNO) throws ImoocMallException {
        OrderVO orderVO = new OrderVO();
        Order order = orderMapper.selectByOrderNO(orderNO);
        if (order==null) {
            throw new ImoocMallException(ImoocMallExceptionEnum.NO_ORDER);
        }
        //判断订单所属用户：订单是否是当前用户的订单
        if (!order.getUserId().equals(userId)) {
            throw new ImoocMallException(ImoocMallExceptionEnum.NOT_YOUR_ORDER);
        }
        //将Order转换为OrderVO返回给前端
        return orderToOrderVO(order);
    }

    @Override
    public PageInfo orderlistForUser(Integer userId, Integer pageNum, Integer pageSize) throws ImoocMallException {
        List<Order> orderList = orderMapper.selectByUserId(userId);
        //orderList转为orderVOList
        List<OrderVO> orderVOList = new ArrayList<>();
        for (Order order : orderList) {
            OrderVO orderVO = orderToOrderVO(order);
            orderVOList.add(orderVO);
        }
        //要传给前端OrderVOList
        PageHelper.startPage(pageNum, pageSize);
        //保留完整分页元数据
        PageInfo pageInfo = new PageInfo<>(orderList);
        //orderList是原始DAO层返回的持久化实体（包含数据库原始字段）; orderVOList是转换后的视图对象
        pageInfo.setList(orderVOList);
        return pageInfo;
    }

    @Override
    public void cancelOrder(Integer userId, String orderNO) throws ImoocMallException {
        Order order = orderMapper.selectByOrderNO(orderNO);
        if (order==null) {
            throw new ImoocMallException(ImoocMallExceptionEnum.NO_ORDER);
        }
        if (!order.getUserId().equals(userId)) {
            throw new ImoocMallException(ImoocMallExceptionEnum.NOT_YOUR_ORDER);
        }
        //改变订单状态 并 执行更新操作
        //将 NOT_PAID 改变为 CANCELED
        if(order.getOrderStatus().equals(Constant.OrderStatusEnum.NOT_PAID.getCode())) {
            order.setOrderStatus(Constant.OrderStatusEnum.CANCELED.getCode());
            order.setEndTime(new Date());
            orderMapper.updateByPrimaryKeySelective(order);
        }else{
            throw new ImoocMallException(ImoocMallExceptionEnum.WRONG_ORDER_STATUS);
        }
    }

    @Override
    public PageInfo orderListForAdmin(Integer pageNum, Integer pageSize) throws ImoocMallException {
        //获取数据库中所有的订单
        List<Order> orderList = orderMapper.selectAllOrder();
        //将orderList转化为orderVOList
        List<OrderVO> orderVOList = new ArrayList<>();
        for (Order order : orderList) {
            OrderVO orderVO = orderToOrderVO(order);
            orderVOList.add(orderVO);
        }
        PageHelper.startPage(pageNum, pageSize);
        PageInfo pageInfo = new PageInfo<>(orderList);
        pageInfo.setList(orderVOList);
        return pageInfo;
    }

    @Override
    public void deliver(String orderNo) throws ImoocMallException {
        Order order = orderMapper.selectByOrderNO(orderNo);
        if (order==null) {
            throw new ImoocMallException(ImoocMallExceptionEnum.NO_ORDER);
        }
        //订单状态由PAID转化为DELIVERED
        if(order.getOrderStatus().equals(Constant.OrderStatusEnum.PAID.getCode())) {
            order.setOrderStatus(Constant.OrderStatusEnum.DELIVERED.getCode());
            order.setDeliveryTime(new Date());
            orderMapper.updateByPrimaryKeySelective(order);
        }else{
            //只有在已支付的情况下才能进行delivery，其他情况都是不被允许的状态
            throw new ImoocMallException(ImoocMallExceptionEnum.WRONG_ORDER_STATUS);
        }
    }

    @Override
    public void finished(String orderNo) throws ImoocMallException {
        Order order = orderMapper.selectByOrderNO(orderNo);
        if (order==null) {
            throw new ImoocMallException(ImoocMallExceptionEnum.NO_ORDER);
        }
        //订单状态由PAID转化为DELIVERED
        if(order.getOrderStatus().equals(Constant.OrderStatusEnum.DELIVERED.getCode())) {
            order.setOrderStatus(Constant.OrderStatusEnum.FINISHED.getCode());
            order.setEndTime(new Date());
            orderMapper.updateByPrimaryKeySelective(order);
        }else{
            //只有在DELIVERED的情况下才能进行FINISHED，其他情况都是不被允许的状态
            throw new ImoocMallException(ImoocMallExceptionEnum.WRONG_ORDER_STATUS);
        }
    }

    /**
     *将order对象转化为orderVO对象并返回
     */
    private OrderVO orderToOrderVO(Order order) throws ImoocMallException {
        //OrderVO里面多了orderItemVOList 和 orderStatusName
        OrderVO orderVO = new OrderVO();
        BeanUtils.copyProperties(order, orderVO);
        //接下来设置OrderVO里面的orderItemVOList 和 orderStatusName
        List<OrderItem> orderItemList = orderItemMapper.selectByOrderNo(order.getOrderNo());
        List<OrderItemVO> orderItemVOList = new ArrayList<>();
        //将orderItemList 转化为 orderItemVOList
        for (OrderItem orderItem : orderItemList) {
            OrderItemVO orderItemVO = new OrderItemVO();
            BeanUtils.copyProperties(orderItem, orderItemVO);
            orderItemVOList.add(orderItemVO);
        }
        orderVO.setOrderStatusName(Constant.OrderStatusEnum.codeOf(orderVO.getOrderStatus()).getValue());
        orderVO.setOrderItemVOList(orderItemVOList);
        return orderVO;
    }

    private Integer totalPrice(List<OrderItem> orderItemList) {
        int totalPrice = 0;
        for (OrderItem orderItem : orderItemList) {
            totalPrice += orderItem.getTotalPrice();
        }
        return totalPrice;
    }

    private void cleanCart(List<CartVO> selectedCartVOList) {
        for (CartVO cartVO : selectedCartVOList) {
            cartMapper.deleteByPrimaryKey(cartVO.getId());
        }
    }

    private List<OrderItem> selectedCartVOListToOrderItemList(List<CartVO> selectedCartVOList) {
        List<OrderItem> orderItemList = new ArrayList<>();
        for (CartVO cartVO : selectedCartVOList) {
            OrderItem orderItem = new OrderItem();
            orderItem.setProductId(cartVO.getProductId());
            //记录商品快照信息
            orderItem.setProductName(cartVO.getProductName());
            orderItem.setProductImg(cartVO.getProductImage());
            orderItem.setUnitPrice(cartVO.getPrice());
            orderItem.setQuantity(cartVO.getQuantity());
            orderItem.setTotalPrice(cartVO.getTotalPrice());
            orderItemList.add(orderItem);
        }
        return orderItemList;
    }

    private void validvalidSaleStatusAndStock(List<CartVO> selectedCartVOList) throws ImoocMallException {
        for (CartVO cartVO : selectedCartVOList) {
            Product product = productMapper.selectByPrimaryKey(cartVO.getProductId());
            if(product.getStatus() != 1){
                throw new ImoocMallException(ImoocMallExceptionEnum.PRODUCT_NOT_SAIL);
            }
            if (product.getStock() < cartVO.getQuantity()) {
                throw new ImoocMallException(ImoocMallExceptionEnum.PRODUCT_NOT_ENOUGH);
            }
        }
    }
}
