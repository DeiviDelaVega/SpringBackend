package com.reservas.polo.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.reservas.polo.model.Inmueble;

@Repository
public interface InmuebleRepository extends JpaRepository<Inmueble, Integer>, JpaSpecificationExecutor<Inmueble>{
	@Query("SELECT i FROM Inmueble i WHERE (i.descripcion LIKE %?1% OR i.serviciosIncluidos LIKE %?1%) AND i.disponibilidad = ?2")
	Page<Inmueble> findByFiltroAndDisponibilidad(String filtro, String disponibilidad, Pageable pageable);

	@Query("SELECT i FROM Inmueble i WHERE i.descripcion LIKE %?1% OR i.serviciosIncluidos LIKE %?1%")
	Page<Inmueble> filtrarPorDescripcionOServicio(String filtro, Pageable pageable);

	@Query("""
		    SELECT i FROM Inmueble i
		    WHERE 
		        (:filtro IS NULL OR i.descripcion LIKE %:filtro% OR i.serviciosIncluidos LIKE %:filtro%)
		        AND (:disponibilidad IS NULL OR i.disponibilidad = :disponibilidad)
		        AND (:adminId IS NULL OR i.administrador.id = :adminId)
		""")
		Page<Inmueble> findByFiltroDisponibilidadYAdministrador(
		    @Param("filtro") String filtro,
		    @Param("disponibilidad") String disponibilidad,
		    @Param("adminId") Integer adminId,
		    Pageable pageable
		);
	
	Page<Inmueble> findByDisponibilidad(String disponibilidad, Pageable pageable);
	
	@Modifying
	@Query("UPDATE Inmueble i SET i.latitud = :lat, i.longitud = :lng WHERE i.id = :id")
	void actualizarUbicacion(@Param("lat") Double lat, @Param("lng") Double lng, @Param("id") Integer id);
}