package net.thumbtack.school.hospital.model;

import java.time.LocalTime;

public class Slot {

    private LocalTime timeStart;
    private LocalTime timeEnd;
    private boolean free;
    private int patientId;
}
