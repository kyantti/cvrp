package es.unex.cum.mdai.vrp.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import es.unex.cum.mdai.vrp.domain.FoodDeliveryOrder;
import es.unex.cum.mdai.vrp.domain.OrderStatus;
import es.unex.cum.mdai.vrp.services.FoodDeliveryOrderService;
import es.unex.cum.mdai.vrp.services.RiderService;
import es.unex.cum.mdai.vrp.utils.OrderAssigner;

@Controller
public class FoodDeliveryOrderController {

	private final FoodDeliveryOrderService orderService;
	private final RiderService riderService;

	public FoodDeliveryOrderController(FoodDeliveryOrderService orderService, RiderService riderService) {
		this.orderService = orderService;
		this.riderService = riderService;
	}

	@GetMapping("/orders")
	public String showOrders(Model model) {
		List<FoodDeliveryOrder> orders = orderService.findAll();

		List<FoodDeliveryOrder> pendingOrders = new ArrayList<>();
		List<FoodDeliveryOrder> readyOrders = new ArrayList<>();
		List<FoodDeliveryOrder> assignedOrders = new ArrayList<>();
		List<FoodDeliveryOrder> onRoadOrders = new ArrayList<>();

		for (FoodDeliveryOrder order : orders) {
			if (order.getStatus() == OrderStatus.PENDING) {
				pendingOrders.add(order);
			} else if (order.getStatus() == OrderStatus.READY) {
				readyOrders.add(order);
			} else if (order.getStatus() == OrderStatus.ASSIGNED) {
				assignedOrders.add(order);
			} else if (order.getStatus() == OrderStatus.ONROAD) {
				onRoadOrders.add(order);
			}
		}

		model.addAttribute("pendingOrders", pendingOrders);
		model.addAttribute("readyOrders", readyOrders);
		model.addAttribute("assignedOrders", assignedOrders);
		model.addAttribute("onRoadOrders", onRoadOrders);

		return "/orders";
	}
	
	@PostMapping("/orders/updateStatus")
    public String updateOrderStatus(@RequestParam(required = false) List<Long> ids, @RequestParam OrderStatus status,
            Model model, RedirectAttributes redirectAttributes) {

        if (ids != null && !ids.isEmpty()) {
            List<FoodDeliveryOrder> orders = fetchOrdersByIds(ids);

            if (status == OrderStatus.ASSIGNED) {
                try {
                    assignOrders(orders, status);
                } catch (Exception e) {
                    e.printStackTrace();
                    redirectAttributes.addFlashAttribute("error", e.getMessage());
                    return "redirect:/orders"; // Redirigir con los mensajes de error
                }
            } else {
                updateOrdersStatus(orders, status);
            }
        } else {
            redirectAttributes.addFlashAttribute("error", "No orders selected");
            return "redirect:/orders"; // Redirigir con el mensaje de error
        }

        return "redirect:/orders"; // Redirigir a la página de órdenes
    }

	private List<FoodDeliveryOrder> fetchOrdersByIds(List<Long> ids) {
		List<FoodDeliveryOrder> orders = new ArrayList<>();
		for (Long id : ids) {
			FoodDeliveryOrder order = orderService.findById(id);
			if (order != null) {
				orders.add(order);
			}
		}
		return orders;
	}

	private void assignOrders(List<FoodDeliveryOrder> orders, OrderStatus status) throws Exception {
		OrderAssigner orderAssigner = new OrderAssigner(riderService);
		System.out.println(orders);
		orderAssigner.assignOrders(orders);
		updateOrdersStatus(orders, status);

	}

	private void updateOrdersStatus(List<FoodDeliveryOrder> orders, OrderStatus status) {
		for (FoodDeliveryOrder order : orders) {
			order.setStatus(status);
			orderService.save(order);
		}
	}

}
