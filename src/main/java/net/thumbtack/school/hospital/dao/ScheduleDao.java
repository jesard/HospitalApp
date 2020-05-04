package net.thumbtack.school.hospital.dao;

import net.thumbtack.school.hospital.model.DaySchedule;
import net.thumbtack.school.hospital.model.Doctor;
import net.thumbtack.school.hospital.model.Slot;

public interface ScheduleDao {

    DaySchedule insert(Doctor doctor, DaySchedule daySchedule);

    Slot insert(DaySchedule daySchedule, Slot slot);

}
