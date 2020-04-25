package net.thumbtack.school.hospital.model;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class DaySchedule {

    private LocalDate date;
    // REVU идея не очень хорошая. Получается, что в этом классе одно поле для доктора, а другое для пациента
    // так делать не надо
    // варианты правильного решения
    // 1. Просто свой класс для доктора и свой для пациента
    // 2. abstract class AbstractDaySchedule и от него DoctorDaySchedule и PatientDaySchedule
    // 3. Нужен ли для пациента Map - вопрос спорный. Из идейных соображений тут, конечно Map
    // но число элементов в нем настолько мало, что вполне можно и List с полным перебором - это никак медленее не будет
    // да и вообще, подумайте - а нужно ли такое пациенту. Это же DaySchedule. У врача есть дневное расписание, а пациенту оно зачем ?
    private List<Slot> slotSchedule; //for Doctor
    private Map<Doctor, Slot> ticketSchedule; //for Patient

    public DaySchedule(LocalDate date, List<Slot> detailedSchedule) {
        this.date = date;
        this.slotSchedule = detailedSchedule;
    }

    public DaySchedule(LocalDate date, Map<Doctor, Slot> ticketSchedule) {
        this.date = date;
        this.ticketSchedule = ticketSchedule;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public List<Slot> getSlotSchedule() {
        return slotSchedule;
    }

    public void setSlotSchedule(List<Slot> slotSchedule) {
        this.slotSchedule = slotSchedule;
    }

}
