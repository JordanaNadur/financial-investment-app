package com.financial.investment.infrastructure.config;

import com.financial.investment.domain.port.InvestmentRepository;
import com.financial.investment.domain.usecase.CalculateReturnUseCase;
import com.financial.investment.domain.usecase.CreateInvestmentUseCase;
import com.financial.investment.domain.usecase.WithdrawInvestmentUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig {

    @Bean
    public CreateInvestmentUseCase createInvestmentUseCase(InvestmentRepository investmentRepository) {
        return new CreateInvestmentUseCase(investmentRepository);
    }

    @Bean
    public CalculateReturnUseCase calculateReturnUseCase() {
        return new CalculateReturnUseCase();
    }

    @Bean
    public WithdrawInvestmentUseCase withdrawInvestmentUseCase(CalculateReturnUseCase calculateReturnUseCase) {
        return new WithdrawInvestmentUseCase(calculateReturnUseCase);
    }
}
