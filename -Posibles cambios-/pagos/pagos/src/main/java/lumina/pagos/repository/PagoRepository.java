package lumina.pagos.repository;

import lumina.pagos.entity.Pago;
import lumina.pagos.entity.EstadoPago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface PagoRepository extends JpaRepository<Pago, Long> {

    List<Pago> findByOrdenId(String ordenId);

    List<Pago> findByUsuarioId(Long usuarioId);

    Optional<Pago> findByMercadopagoPreferenceId(String preferenceId);

    Optional<Pago> findByMercadopagoPaymentId(String paymentId);

    List<Pago> findByEstado(EstadoPago estado);
}
