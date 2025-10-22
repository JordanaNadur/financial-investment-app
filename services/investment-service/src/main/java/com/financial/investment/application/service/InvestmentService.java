package com.financial.investment.application.service;

import com.financial.investment.application.dto.InvestmentRequest;
import com.financial.investment.application.dto.InvestmentResponse;
import com.financial.investment.domain.Investment;
import com.financial.investment.domain.usecase.CalculateReturnUseCase;
import com.financial.investment.domain.usecase.CreateInvestmentUseCase;
import com.financial.investment.domain.usecase.WithdrawInvestmentUseCase;
import com.financial.investment.domain.port.InvestmentRepository;
import com.financial.notification.messaging.InvestmentCreatedEvent;
import com.financial.investment.messaging.InvestmentEventProducer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class InvestmentService {

    private final InvestmentRepository investmentRepository;
    private final CreateInvestmentUseCase createInvestmentUseCase;
    private final CalculateReturnUseCase calculateReturnUseCase;
    private final WithdrawInvestmentUseCase withdrawInvestmentUseCase;
    private final InvestmentEventProducer investmentEventProducer;

    public InvestmentService(InvestmentRepository investmentRepository,
                             CreateInvestmentUseCase createInvestmentUseCase,
                             CalculateReturnUseCase calculateReturnUseCase,
                             WithdrawInvestmentUseCase withdrawInvestmentUseCase,
                             InvestmentEventProducer investmentEventProducer) {
        this.investmentRepository = investmentRepository;
        this.createInvestmentUseCase = createInvestmentUseCase;
        this.calculateReturnUseCase = calculateReturnUseCase;
        this.withdrawInvestmentUseCase = withdrawInvestmentUseCase;
        this.investmentEventProducer = investmentEventProducer;
    }

    public InvestmentResponse createInvestment(Long userId, InvestmentRequest request) {
        Investment investment = new Investment(userId, request.getValor(), request.getPrazoMeses(),
                request.getRentabilidadeMensal(), request.getModalidade());
        Investment saved = createInvestmentUseCase.execute(userId, investment);
        BigDecimal returns = calculateReturnUseCase.execute(saved);
        InvestmentCreatedEvent event = new InvestmentCreatedEvent(userId, saved.getId(), "Investimento criado com sucesso!");
        investmentEventProducer.sendInvestmentCreatedEvent(event);
        return new InvestmentResponse(saved, returns);
    }

    public Page<InvestmentResponse> getInvestments(Long userId, Pageable pageable) {
        Page<Investment> investments = investmentRepository.findByUserId(userId, pageable);
        return investments.map(inv -> new InvestmentResponse(inv, calculateReturnUseCase.execute(inv)));
    }

    public Optional<InvestmentResponse> getInvestment(Long userId, Long id) {
        Optional<Investment> opt = investmentRepository.findByIdAndUserId(id, userId);
        return opt.map(inv -> new InvestmentResponse(inv, calculateReturnUseCase.execute(inv)));
    }

    public BigDecimal withdrawInvestment(Long userId, Long id) {
        Optional<Investment> opt = investmentRepository.findByIdAndUserId(id, userId);
        if (opt.isPresent()) {
            return withdrawInvestmentUseCase.execute(opt.get());
        }
        throw new RuntimeException("Investment not found");
    }
}
