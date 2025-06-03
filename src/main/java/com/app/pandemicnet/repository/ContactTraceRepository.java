package com.app.pandemicnet.repository;

import com.app.pandemicnet.model.ContactTrace;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContactTraceRepository extends JpaRepository<ContactTrace,Long> {
}
