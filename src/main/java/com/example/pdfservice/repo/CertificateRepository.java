package com.example.pdfservice.repo;


import com.example.pdfservice.entity.Certificate;
import com.example.pdfservice.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CertificateRepository extends JpaRepository<Certificate, Long> {
    List <Certificate> findAllByEvent(Event event);
}
