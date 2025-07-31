package es.unex.cum.mdai.vrp.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import es.unex.cum.mdai.vrp.domain.Rider;
import es.unex.cum.mdai.vrp.domain.RiderStatus;

@Repository
public interface RiderRepository extends CrudRepository<Rider, Long>{
	public List<Rider> findByStatus(RiderStatus status);
}
