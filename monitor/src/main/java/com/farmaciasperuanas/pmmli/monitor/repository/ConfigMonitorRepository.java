package com.farmaciasperuanas.pmmli.monitor.repository;

import com.farmaciasperuanas.pmmli.monitor.entity.ConfigMonitor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfigMonitorRepository extends JpaRepository<ConfigMonitor, Long>, JpaSpecificationExecutor<ConfigMonitor> {

}
