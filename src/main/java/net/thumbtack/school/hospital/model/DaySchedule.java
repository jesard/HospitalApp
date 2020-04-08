package net.thumbtack.school.hospital.model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class DaySchedule {

    private LocalDate date;
    private LocalTime timeStart;
    private LocalTime timeEnd;
    private List<Slot> detailedSchedule;

}
