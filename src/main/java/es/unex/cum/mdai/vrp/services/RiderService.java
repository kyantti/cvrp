package es.unex.cum.mdai.vrp.services;

import java.util.List;

import es.unex.cum.mdai.vrp.domain.Rider;
import es.unex.cum.mdai.vrp.domain.RiderStatus;

public interface RiderService {
	public void saveAll(List<Rider> riders);
	public void save(Rider rider);
	public List<Rider> findAll();
	public Rider findById(Long id);
	public List<Rider> findByStatus(RiderStatus status);
	//public int getCurrentMaxCapacity();
}
