package com.sios.ex.springbatch;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SourceItemRepository extends JpaRepository<SourceItemEntity, String> {

}
