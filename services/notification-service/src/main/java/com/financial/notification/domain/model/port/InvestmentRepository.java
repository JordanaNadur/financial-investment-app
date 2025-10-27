package com.financial.investment.domain.port;

import com.financial.investment.domain.Investment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface InvestmentRepository extends JpaRepository<Investment, Long> {

    // Buscar investimentos por usuário
    List<Investment> findByUserId(Long userId);

    // Buscar investimentos por usuário com paginação
    Page<Investment> findByUserId(Long userId, Pageable pageable);

    // Buscar investimento específico por ID e usuário
    Optional<Investment> findByIdAndUserId(Long id, Long userId);

    // Buscar investimentos por modalidade
    List<Investment> findByModalidade(Investment.TipoModalidade modalidade);

    // Buscar investimentos por usuário e modalidade
    List<Investment> findByUserIdAndModalidade(Long userId, Investment.TipoModalidade modalidade);

    // Buscar investimentos com valor maior ou igual
    List<Investment> findByValorGreaterThanEqual(BigDecimal valor);

    // Buscar investimentos com valor entre
    List<Investment> findByValorBetween(BigDecimal minValor, BigDecimal maxValor);

    // Buscar investimentos por período de data
    List<Investment> findByDataInvestimentoBetween(LocalDate startDate, LocalDate endDate);

    // Buscar investimentos por usuário e período
    List<Investment> findByUserIdAndDataInvestimentoBetween(Long userId, LocalDate startDate, LocalDate endDate);

    // Buscar investimentos ativos (não vencidos)
    @Query("SELECT i FROM Investment i WHERE i.userId = :userId AND i.dataInvestimento + i.prazoMeses MONTH > CURRENT_DATE")
    List<Investment> findActiveInvestmentsByUserId(@Param("userId") Long userId);

    // Buscar investimentos vencidos
    @Query("SELECT i FROM Investment i WHERE i.userId = :userId AND i.dataInvestimento + i.prazoMeses MONTH <= CURRENT_DATE")
    List<Investment> findExpiredInvestmentsByUserId(@Param("userId") Long userId);

    // Buscar investimentos que vencem em um determinado mês/ano
    @Query("SELECT i FROM Investment i WHERE i.userId = :userId AND YEAR(i.dataInvestimento + i.prazoMeses MONTH) = :year AND MONTH(i.dataInvestimento + i.prazoMeses MONTH) = :month")
    List<Investment> findInvestmentsMaturingInMonth(@Param("userId") Long userId,
                                                    @Param("year") int year,
                                                    @Param("month") int month);

    // Calcular valor total investido por usuário
    @Query("SELECT COALESCE(SUM(i.valor), 0) FROM Investment i WHERE i.userId = :userId")
    BigDecimal getTotalInvestedAmountByUserId(@Param("userId") Long userId);

    // Calcular valor total investido por usuário e modalidade
    @Query("SELECT COALESCE(SUM(i.valor), 0) FROM Investment i WHERE i.userId = :userId AND i.modalidade = :modalidade")
    BigDecimal getTotalInvestedAmountByUserIdAndModalidade(@Param("userId") Long userId,
                                                           @Param("modalidade") Investment.TipoModalidade modalidade);

    // Buscar investimentos com rentabilidade acima de um valor
    @Query("SELECT i FROM Investment i WHERE i.rentabilidadeMensal > :minRentabilidade")
    List<Investment> findByRentabilidadeMensalGreaterThan(@Param("minRentabilidade") BigDecimal minRentabilidade);

    // Buscar investimentos por usuário ordenados por data (mais recentes primeiro)
    List<Investment> findByUserIdOrderByDataInvestimentoDesc(Long userId);

    // Buscar investimentos por usuário ordenados por valor (maiores primeiro)
    List<Investment> findByUserIdOrderByValorDesc(Long userId);

    // Buscar investimentos com prazo mínimo
    List<Investment> findByPrazoMesesGreaterThanEqual(Integer minPrazoMeses);

    // Buscar investimentos com prazo máximo
    List<Investment> findByPrazoMesesLessThanEqual(Integer maxPrazoMeses);

    // Buscar investimentos por usuário e faixa de prazo
    List<Investment> findByUserIdAndPrazoMesesBetween(Long userId, Integer minPrazo, Integer maxPrazo);

    // Contar investimentos por usuário
    Long countByUserId(Long userId);

    // Contar investimentos por modalidade
    Long countByModalidade(Investment.TipoModalidade modalidade);

    // Verificar se existe investimento para usuário
    Boolean existsByUserId(Long userId);

    // Buscar investimentos com filtros múltiplos
    @Query("SELECT i FROM Investment i WHERE " +
            "(:userId IS NULL OR i.userId = :userId) AND " +
            "(:modalidade IS NULL OR i.modalidade = :modalidade) AND " +
            "(:minValor IS NULL OR i.valor >= :minValor) AND " +
            "(:maxValor IS NULL OR i.valor <= :maxValor) AND " +
            "(:minPrazo IS NULL OR i.prazoMeses >= :minPrazo) AND " +
            "(:maxPrazo IS NULL OR i.prazoMeses <= :maxPrazo)")
    Page<Investment> findByFilters(@Param("userId") Long userId,
                                   @Param("modalidade") Investment.TipoModalidade modalidade,
                                   @Param("minValor") BigDecimal minValor,
                                   @Param("maxValor") BigDecimal maxValor,
                                   @Param("minPrazo") Integer minPrazo,
                                   @Param("maxPrazo") Integer maxPrazo,
                                   Pageable pageable);

    // Buscar top investimentos por valor
    @Query("SELECT i FROM Investment i WHERE i.userId = :userId ORDER BY i.valor DESC")
    Page<Investment> findTopInvestmentsByUserId(@Param("userId") Long userId, Pageable pageable);

    // Buscar investimentos que vencem nos próximos X dias
    @Query("SELECT i FROM Investment i WHERE i.userId = :userId AND " +
            "i.dataInvestimento + i.prazoMeses MONTH BETWEEN CURRENT_DATE AND CURRENT_DATE + :days DAYS")
    List<Investment> findInvestmentsMaturingInNextDays(@Param("userId") Long userId,
                                                       @Param("days") Integer days);

    // Calcular projeção de retorno total por usuário
    @Query("SELECT i FROM Investment i WHERE i.userId = :userId")
    List<Investment> findAllByUserIdForProjection(@Param("userId") Long userId);

    // Deletar investimentos por usuário
    void deleteByUserId(Long userId);

    // Deletar investimento específico por ID e usuário
    void deleteByIdAndUserId(Long id, Long userId);
}