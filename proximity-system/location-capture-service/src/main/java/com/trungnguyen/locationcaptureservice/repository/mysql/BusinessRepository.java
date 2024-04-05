package com.trungnguyen.locationcaptureservice.repository.mysql;

import com.trungnguyen.locationcaptureservice.model.mysql.Business;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface BusinessRepository extends JpaRepository<Business, String> {
}
