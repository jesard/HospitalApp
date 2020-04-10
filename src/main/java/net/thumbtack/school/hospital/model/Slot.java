package net.thumbtack.school.hospital.model;

import java.time.LocalTime;

public class Slot {

    private LocalTime timeStart;
    private LocalTime timeEnd;
    private boolean free;
    // REVU private Patient patient. Не надо экспонировать реляционую модель в классах Явы
    private int patientId;
}
