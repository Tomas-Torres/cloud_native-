package lumina.bodega.repository;

import lumina.bodega.entity.AlertaStock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AlertaStockRepository extends JpaRepository<AlertaStock, Long> {
    List<AlertaStock> findByResueltaFalseOrderByFechaCreacionDesc();
}
