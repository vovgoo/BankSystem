package com.vovgoo.BankSystem.repository.impl;

import com.vovgoo.BankSystem.dto.stats.StatsResponse;
import com.vovgoo.BankSystem.repository.StatsRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Repository
public class StatsRepositoryImpl implements StatsRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public StatsResponse getOverview() {
        Object[] result = (Object[]) em.createQuery("""
            select
                count(c),
                sum(case when size(c.accounts) > 0 then 1 else 0 end),
                sum(case when size(c.accounts) = 0 then 1 else 0 end),
                count(a),
                coalesce(sum(a.balance), 0),
                coalesce(avg(a.balance), 0),
                coalesce(max(a.balance), 0),
                coalesce(min(a.balance), 0)
            from Client c
            left join c.accounts a
        """).getSingleResult();

        return new StatsResponse(
                ((Number) result[0]).longValue(),
                ((Number) result[1]).longValue(),
                ((Number) result[2]).longValue(),
                ((Number) result[3]).longValue(),
                BigDecimal.valueOf(((Number) result[4]).doubleValue()).setScale(2, RoundingMode.HALF_UP),
                BigDecimal.valueOf(((Number) result[5]).doubleValue()).setScale(2, RoundingMode.HALF_UP),
                BigDecimal.valueOf(((Number) result[6]).doubleValue()).setScale(2, RoundingMode.HALF_UP),
                BigDecimal.valueOf(((Number) result[7]).doubleValue()).setScale(2, RoundingMode.HALF_UP)
        );
    }
}