package org.fleetflow.souktransportbackend.service;

import org.fleetflow.souktransportbackend.dto.response.DashboardAdminDto;
import org.fleetflow.souktransportbackend.dto.response.DashboardExpediteurDto;

public interface DashboardService {
    DashboardAdminDto getDashboardAdmin();
    DashboardExpediteurDto dashboardExpediteur(Long expediteurId);


}
