package com.yk.yApp.payroll;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.mediatype.problem.Problem;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderController {

	private final OrderRepository orderRepository;
	private final OrderModelAssembler assembler;

	OrderController(OrderRepository orderRepository, OrderModelAssembler assembler) {
		this.orderRepository = orderRepository;
		this.assembler = assembler;
	}

	@GetMapping("/orders")
	CollectionModel<EntityModel<Order>> all() {

		List<EntityModel<Order>> orders = orderRepository.findAll().stream()
				.map(assembler::toModel)
				.collect(Collectors.toList());

		return CollectionModel.of(orders, linkTo(methodOn(OrderController.class).all()).withSelfRel());
	}

	@GetMapping("/orders/{id}")
	EntityModel<Order> one (@PathVariable Long id) {

		Order order = orderRepository.findById(id)
				.orElseThrow(() -> new OrderNotFoundException(id));

		return assembler.toModel(order);
	}

	@PostMapping("/orders")
	ResponseEntity<EntityModel<Order>> newOrder(@RequestBody Order order) {

		order.setStatus(Status.IN_PROGRESS);
		Order newOrder = orderRepository.save(order);

		return ResponseEntity
				.created(linkTo(methodOn(OrderController.class).one(newOrder.getId())).toUri())
				.body(assembler.toModel(newOrder));
	}

	@PutMapping("/orders/{id}/complete")
	ResponseEntity<?> complete(@PathVariable Long id) {

		Order order = orderRepository.findById(id)
				.orElseThrow(() -> new OrderNotFoundException(id));

		if (order.getStatus() == Status.IN_PROGRESS) {
			order.setStatus(Status.COMPLETED);
			return ResponseEntity.ok(assembler.toModel(orderRepository.save(order)));
		}

		return ResponseEntity
				.status(HttpStatus.METHOD_NOT_ALLOWED)
				.header(HttpHeaders.CONTENT_TYPE, MediaTypes.HTTP_PROBLEM_DETAILS_JSON_VALUE)
				.body(Problem.create()
						.withTitle("Method not allowed")
						.withDetail("You can't complete an order that is in the " + order.getStatus() + " status"));
	}

	@DeleteMapping("/orders/{id}/cancel")
	ResponseEntity<?> cancel(@PathVariable Long id) {
		Order order = orderRepository.findById(id)
				.orElseThrow(() -> new OrderNotFoundException(id));

		if (order.getStatus() == Status.IN_PROGRESS) {
			order.setStatus(Status.CANCELLED);
			return ResponseEntity.ok(assembler.toModel(orderRepository.save(order)));
		}

		return ResponseEntity
				.status(HttpStatus.METHOD_NOT_ALLOWED)
				.header(HttpHeaders.CONTENT_TYPE, MediaTypes.HTTP_PROBLEM_DETAILS_JSON_VALUE)
				.body(Problem.create()
						.withTitle("Method not allowed")
						.withDetail("You can't cancel an order that is in the " + order.getStatus() + " status"));
	}
}

/*
 * OrderModelAssemblerを構築する前に、何が必要かについて話し合いましょう。
 * Status.IN_PROGRESS, Status.COMPLETED, Status.CANCELLED間の状態の流れをモデル化しています。
 * このようなデータをクライアントに提供するときの自然なことは、このペイロードに基づいてクライアントに何ができるかを決定させることです。
 * しかし、間違っているでしょう。
 * このフローで新しい状態を導入するとどうなるでしょうか？UI上の様々なボタンの配置は、おそらく誤っているでしょう。
 * おそらく国際的なサポートをコーディングし、各状態のロケール固有のテキストを表示している間に、各状態の名前を変更した場合はどうなるでしょうか？
 * それはほとんどすべてのクライアントを壊すでしょう。
 * アプリケーション状態のエンジンとしてHATEOASまたはハイパーメディアを入力します。
 * クライアントがペイロードを解析する代わりに、有効なアクションを通知するためのリンクをクライアントに提供します。
 * 状態ベースのアクションをデータのペイロードから切り離します。
 * つまり、CANCELとCOMPLETEが有効なアクションである場合、リンクのリストに動的に追加します。
 * リンクが存在する場合、クライアントは対応するボタンをユーザーに表示するだけで済みます。
 * これにより、クライアントはそのようなアクションがいつ有効であるかを知る必要がなくなり、状態遷移のロジックでサーバーとそのクライアントが同期しなくなるリスクを減らします。
 * Spring HATEOS RepresentationModelAssemblerコンポーネントの概念をすでに採用しているため、そのようなロジックをOrderModelAssemblerに配置することは、このビジネスルールをキャプチャーするのに最適な場所です。
 */

