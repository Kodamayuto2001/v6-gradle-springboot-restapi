package com.yk.yApp.payroll;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
class OrderModelAssembler implements RepresentationModelAssembler<Order, EntityModel<Order>> {

	@Override
	public EntityModel<Order> toModel(Order order) {

		//	Unconditional links to single-item resource and aggregate root
		EntityModel<Order> orderModel = EntityModel.of(order,
				linkTo(methodOn(OrderController.class).one(order.getId())).withSelfRel(),
				linkTo(methodOn(OrderController.class)).withRel("orders"));

		//	Conditional links based on state of the order
		if (order.getStatus() == Status.IN_PROGRESS) {
			orderModel.add(linkTo(methodOn(OrderController.class).cancel(order.getId())).withRel("cancel"));
			orderModel.add(linkTo(methodOn(OrderController.class).complete(order.getId())).withRel("complete"));
		}

		return orderModel;
	}
}

/*
 * このリソースアセンブラには、単一アイテムリソースへの自己リンクと、集約ルートへのリンクが常に含まれています。
 * ただし、OrderController.cancel(id)およびOrderController.compplete(id)への2つの条件リンクも含まれています。
 * これらのリンクは、オーダーのステータスがStatus.IN_PROGRESSの場合にのみ表示されます。
 * クライアントが単純な古いJSONのデータを単に読み取るのではなく、HALとリンクを読み取る機能を採用できる場合、オーダーシステムに関するドメインの知識の必要性と引き換えにできます。
 * これにより、クライアントとサーバー間の結合が自然に減少します。
 * また、プロセスでクライアントを中断することなく、オーダー処理の流れを調整するための扉を開きます。
 */