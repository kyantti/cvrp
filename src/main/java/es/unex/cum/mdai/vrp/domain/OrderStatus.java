package es.unex.cum.mdai.vrp.domain;

public enum OrderStatus {
	PENDING, // The order has been created but not yet assigned to a route
	READY, // The order has been finished
	ASSIGNED, // The order has been assigned to a route
	ONROAD, // The order is being delivered
	DELIVERED, // The order has been delivered
}
