package net.thumbtack.school.hospital.dao;

import net.thumbtack.school.hospital.model.Patient;

public interface PatientDao extends UserDao {

    Patient insertPatient(Patient patient);

    Patient getPatientById(int id);
}
