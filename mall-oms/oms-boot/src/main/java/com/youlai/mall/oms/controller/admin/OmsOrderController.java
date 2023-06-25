package com.youlai.mall.oms.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.youlai.common.result.PageResult;
import com.youlai.common.result.Result;
import com.youlai.mall.oms.model.dto.OrderDTO;
import com.youlai.mall.oms.model.entity.OmsOrder;
import com.youlai.mall.oms.model.entity.OmsOrderItem;
import com.youlai.mall.oms.model.query.OrderPageQuery;
import com.youlai.mall.oms.service.OrderItemService;
import com.youlai.mall.oms.service.admin.OmsOrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * 「管理端」订单控制层
 *
 * @author huawei
 * @since 2020/12/30
 */
@Tag(name = "「管理端」订单管理")
@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OmsOrderController {

    private final OmsOrderService orderService;

    private final OrderItemService orderItemService;

    @Operation(summary ="订单分页列表")
    @GetMapping
    public PageResult listOrderPages(OrderPageQuery queryParams) {
        IPage<OmsOrder> result = orderService.listOrderPages(queryParams);
        return PageResult.success(result);
    }

    @Operation(summary= "订单详情")
    @GetMapping("/{orderId}")
    public Result getOrderDetail(
            @Parameter(name = "订单ID") @PathVariable Long orderId
    ) {
        OrderDTO orderDTO = new OrderDTO();
        // 订单
        OmsOrder order = orderService.getById(orderId);

        // 订单明细
        List<OmsOrderItem> orderItems = orderItemService.list(new LambdaQueryWrapper<OmsOrderItem>()
                .eq(OmsOrderItem::getOrderId, orderId)
        );
        orderItems = Optional.ofNullable(orderItems).orElse(Collections.EMPTY_LIST);

        orderDTO.setOrder(order).setOrderItems(orderItems);
        return Result.success(orderDTO);
    }

}
