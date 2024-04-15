package com.trungnguyen.locationcaptureservice.intergration.repository;


import com.trungnguyen.locationcaptureservice.constant.MockDataConstant;
import com.trungnguyen.locationcaptureservice.model.mysql.Business;
import com.trungnguyen.locationcaptureservice.repository.mysql.BusinessRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class BusinessRepositoryTest {


    @Autowired
    BusinessRepository businessRepository;

    @Test
    public void given_WhenUserAddLocation() {
        Business business = new Business();
        business.setId(MockDataConstant.ID);
        business.setName(MockDataConstant.NAME);
        business.setDescription(MockDataConstant.DESCRIPTION);
        business.setBusinessType(MockDataConstant.BUSINESS_TYPE.name());

        Business savedBusiness = businessRepository.save(business);

        assertThat(savedBusiness.getId()).isEqualTo(MockDataConstant.ID);
        assertThat(savedBusiness.getName()).isEqualTo(MockDataConstant.NAME);
        assertThat(savedBusiness.getDescription()).isEqualTo(MockDataConstant.DESCRIPTION);
        assertThat(savedBusiness.getBusinessType()).isEqualTo(MockDataConstant.BUSINESS_TYPE.name());
    }
}
