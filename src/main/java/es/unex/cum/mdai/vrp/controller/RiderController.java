package es.unex.cum.mdai.vrp.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import es.unex.cum.mdai.vrp.domain.FoodDeliveryOrder;
import es.unex.cum.mdai.vrp.domain.OrderStatus;
import es.unex.cum.mdai.vrp.domain.Rider;
import es.unex.cum.mdai.vrp.domain.RiderStatus;
import es.unex.cum.mdai.vrp.services.RiderService;

@Controller
public class RiderController {

	private final RiderService riderService;

	public RiderController(RiderService riderService) {
		this.riderService = riderService;
	}

	@GetMapping("/riders")
	public String showRiders(Model model) {
		List<Rider> riders = riderService.findAll();
		model.addAttribute("riders", riders);
		return "riders";
	}

	@PostMapping("/updateRiderStatus")
	public String updateRiderStatus(@RequestParam Long riderId, Model model, RedirectAttributes redirectAttributes) {
		Rider rider = riderService.findById(riderId);
		List<FoodDeliveryOrder> orders = rider.getCurrentOrders();
		if (rider.getStatus() == RiderStatus.AT_RESTAURANT && orders != null && !orders.isEmpty()) {
			rider.setStatus(RiderStatus.ON_THE_WAY);
			for (FoodDeliveryOrder order : orders) {
				order.setStatus(OrderStatus.ONROAD); // Assign the order to the rider
			}
			rider.startTimeOut(); // Start counting time when the rider goes on the road
		} else if (rider.getStatus() == RiderStatus.ON_THE_WAY) {
			rider.setStatus(RiderStatus.AT_RESTAURANT);
			rider.endTimeOut(); // Stop counting time when the rider returns
			rider.setNumOfRides(rider.getNumOfRides() + 1); // Increment the number of rides
			for (FoodDeliveryOrder order : orders) {
				order.setStatus(OrderStatus.DELIVERED); // Deliver the order
				order.setRider(null); // Reset the rider
			}
			rider.setCurrentOrders(null); // Reset the orders
		} else if (rider.getStatus() == RiderStatus.AT_RESTAURANT && (orders == null || orders.isEmpty())) {
			System.out.println("No orders to deliver");
			redirectAttributes.addFlashAttribute("error", "No orders to deliver");
			return "redirect:/riders";
		}
		riderService.save(rider); // Save the updated rider
		return "redirect:/riders"; // Redirect to the page where the riders are listed
	}

	// New endpoint to fetch the time spent on the road
	@GetMapping("/riderTimeOut/{riderId}")
	@ResponseBody
	public long getRiderTimeOut(@PathVariable Long riderId) {
		Rider rider = riderService.findById(riderId);
		return rider != null ? rider.getTimeOut() : 0;
	}
}
