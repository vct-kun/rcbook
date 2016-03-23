package com.rcbook.repository;

import com.rcbook.domain.Brand;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by vctran on 23/03/16.
 */
public interface BrandRepository extends JpaRepository<Brand, Long> {
}
