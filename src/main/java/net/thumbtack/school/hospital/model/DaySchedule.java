package net.thumbtack.school.hospital.model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class DaySchedule {

    private LocalDate date;
    // REVU а нужны ли timeStart и timeEnd ? 
    // timeStart - это время из  detailedSchedule.get(0)
    // timeEnd - это время из  detailedSchedule.get(last)
    private LocalTime timeStart;
    private LocalTime timeEnd;
    private List<Slot> detailedSchedule;

}
