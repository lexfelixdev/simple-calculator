package nl.lexfelix.simplecalculator.calculationserver.repository;

import nl.lexfelix.simplecalculator.calculationserver.repository.entity.Calculation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CalculationRepository extends JpaRepository<Calculation, Long> {
}
