package com.skillcanvas.loans.audit;

import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import java.util.Optional;


/**
 * This class is used to provide the current auditor of the application
 */
@Component("auditAwareImpl")
public class AuditAwareImpl implements AuditorAware<String> {

        /**
        * This method returns the current auditor of the application
        * @return the current auditor
        */
        @Override
        public Optional<String> getCurrentAuditor() {
            return Optional.of("LOANS_MS");
        }
}
