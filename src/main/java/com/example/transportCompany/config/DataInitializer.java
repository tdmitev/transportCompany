package com.example.transportCompany.config;

import com.example.transportCompany.model.Qualification;
import com.example.transportCompany.model.TransportStatus;
import com.example.transportCompany.model.VehicleType;
import com.example.transportCompany.repository.QualificationRepository;
import com.example.transportCompany.repository.TransportStatusRepository;
import com.example.transportCompany.repository.VehicleTypeRepository;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

    @Bean
    public ApplicationRunner loadInitialData(QualificationRepository qualificationRepository,
                                             VehicleTypeRepository vehicleTypeRepository,
                                             TransportStatusRepository transportStatusRepository) {
        return args -> {

            if (qualificationRepository.count() == 0) {
                Qualification hazmat = new Qualification();
                hazmat.setName("HAZMAT");
                qualificationRepository.save(hazmat);

                Qualification flammable = new Qualification();
                flammable.setName("FLAMMABLE");
                qualificationRepository.save(flammable);

                Qualification passengersOver12 = new Qualification();
                passengersOver12.setName("PASSENGERS_OVER_12");
                qualificationRepository.save(passengersOver12);
            }

            if (vehicleTypeRepository.count() == 0) {
                VehicleType bus = new VehicleType();
                bus.setName("BUS");
                vehicleTypeRepository.save(bus);

                VehicleType truck = new VehicleType();
                truck.setName("TRUCK");
                vehicleTypeRepository.save(truck);

                VehicleType cistern = new VehicleType();
                cistern.setName("CISTERN");
                vehicleTypeRepository.save(cistern);
            }
            
            if (transportStatusRepository.count() == 0) {
                TransportStatus planned = new TransportStatus();
                planned.setName("PLANNED");
                transportStatusRepository.save(planned);

                TransportStatus completed = new TransportStatus();
                completed.setName("COMPLETED");
                transportStatusRepository.save(completed);

                TransportStatus canceled = new TransportStatus();
                canceled.setName("CANCELED");
                transportStatusRepository.save(canceled);
            }
        };
    }
}