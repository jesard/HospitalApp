package net.thumbtack.school.hospital.Service;

import net.thumbtack.school.hospital.dto.request.regDoctor.RegDocDtoRequestByHands;
import net.thumbtack.school.hospital.dto.request.regDoctor.WeekDaysSchedule;
import net.thumbtack.school.hospital.dto.request.regDoctor.WeekSchedule;
import net.thumbtack.school.hospital.dto.response.regDoctor.RegDoctorDtoResponse;
import net.thumbtack.school.hospital.dto.response.regDoctor.DayScheduleDtoResponse;
import net.thumbtack.school.hospital.dto.response.regDoctor.SlotForDtoResponse;
import net.thumbtack.school.hospital.model.DaySchedule;
import net.thumbtack.school.hospital.model.Doctor;
import net.thumbtack.school.hospital.model.Slot;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public class DoctorService extends UserService {

    private static DayOfWeek getDayOfWeek(String day) {
        switch (day) {
            case "Mon": return DayOfWeek.MONDAY;
            case "Tue": return DayOfWeek.TUESDAY;
            case "Wed": return DayOfWeek.WEDNESDAY;
            case "Thu": return DayOfWeek.THURSDAY;
            case "Fri": return DayOfWeek.FRIDAY;
        }
        return null;
    }

    private static DaySchedule makeDaySchedule(Doctor doctor, LocalDate date, LocalTime timeStart, LocalTime timeEnd, int duration) {
        DaySchedule daySchedule = new DaySchedule();
        daySchedule.setDoctor(doctor);
        daySchedule.setDate(date);
        List<Slot> slots = new ArrayList<>();
        for (LocalTime time = timeStart; time.isBefore(timeEnd); time = time.plusMinutes(duration)) {
            Slot slot = new Slot();
            slot.setTimeStart(time);
            slot.setTimeEnd(time.plusMinutes(duration - 1));
            slots.add(slot);
        }
        daySchedule.setSlotSchedule(slots);
        return daySchedule;
    }

    protected static Doctor makeDoctorFromDtoRequest(RegDocDtoRequestByHands request) {
        Doctor doctor = new Doctor();
        doctor.setFirstName(request.getFirstName());
        doctor.setLastName(request.getLastName());
        doctor.setPatronymic(request.getPatronymic());
        doctor.setLogin(request.getLogin());
        doctor.setPassword(request.getPassword());
        doctor.setSpeciality(request.getSpeciality());
        doctor.setRoom(request.getRoom());
        int duration = request.getDuration();
        DateTimeFormatter formatterDate = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate dateStart = LocalDate.parse(request.getDateStart(), formatterDate);
        LocalDate dateEnd = LocalDate.parse(request.getDateEnd(), formatterDate);
        EnumSet<DayOfWeek> workDays = EnumSet.noneOf(DayOfWeek.class);
        List<DaySchedule> schedule = new ArrayList<>();
        WeekSchedule weekSchedule = request.getWeekSchedule();
        List<WeekDaysSchedule> weekDaysSchedule = request.getWeekDaysSchedules();
        DateTimeFormatter formatterTime = DateTimeFormatter.ofPattern("HH:mm");
        if (request.hasWeekSchedule()) {
            LocalTime timeStart = LocalTime.parse(weekSchedule.getTimeStart(), formatterTime);
            LocalTime timeEnd = LocalTime.parse(weekSchedule.getTimeEnd(), formatterTime);
            if (weekSchedule.getWeekDays().size() != 0) {
                for (String dayOfWeek: weekSchedule.getWeekDays()) {
                    workDays.add(getDayOfWeek(dayOfWeek));
                }
                for (LocalDate date = dateStart; date.isBefore(dateEnd.plusDays(1)); date = date.plusDays(1)) {
                    if (workDays.contains(date.getDayOfWeek())) {
                        schedule.add(makeDaySchedule(doctor, date, timeStart, timeEnd, duration));
                    }
                }
            } else {
                for (LocalDate date = dateStart; date.isBefore(dateEnd.plusDays(1)); date = date.plusDays(1)) {
                    schedule.add(makeDaySchedule(doctor, date, timeStart, timeEnd, duration));
                }
            }
        }

        if (request.hasWeekDaysSchedule()) {
            for (WeekDaysSchedule day: weekDaysSchedule) {
                workDays.add(getDayOfWeek(day.getWeekDay()));
            }
            for (LocalDate date = dateStart; date.isBefore(dateEnd.plusDays(1)); date = date.plusDays(1)) {
                if (workDays.contains(date.getDayOfWeek())) {
                    for (WeekDaysSchedule day: weekDaysSchedule) {
                        if (date.getDayOfWeek() == getDayOfWeek(day.getWeekDay())) {
                            LocalTime timeStart = LocalTime.parse(day.getTimeStart(), formatterTime);
                            LocalTime timeEnd = LocalTime.parse(day.getTimeEnd(), formatterTime);
                            schedule.add(makeDaySchedule(doctor, date, timeStart, timeEnd, duration));
                        }
                    }
                }
            }


        }
        doctor.setSchedule(schedule);
        return doctor;
    }

    protected static RegDoctorDtoResponse makeDtoResponseFromDoctor(Doctor doctor) {
        RegDoctorDtoResponse response = new RegDoctorDtoResponse();
        response.setId(doctor.getId());
        response.setFirstName(doctor.getFirstName());
        response.setLastName(doctor.getLastName());
        response.setPatronymic(doctor.getPatronymic());
        response.setSpeciality(doctor.getSpeciality());
        response.setRoom(doctor.getRoom());
        List<DayScheduleDtoResponse> schedule = new ArrayList<>();
        for (DaySchedule day: doctor.getSchedule()) {
            DayScheduleDtoResponse dayResponse = new DayScheduleDtoResponse();
            dayResponse.setDate(day.getDate().toString());
            List<SlotForDtoResponse> slotsForResponse = new ArrayList<>();
            for (Slot slot: day.getSlotSchedule()) {
                SlotForDtoResponse slotResponse = new SlotForDtoResponse();
                slotResponse.setTime(slot.getTimeStart().toString());
                slotsForResponse.add(slotResponse);
            }
            dayResponse.setSlots(slotsForResponse);
            schedule.add(dayResponse);
        }
        response.setSchedule(schedule);
        return response;
    }

    public String registerDoctor(String regDoctorJson) {
        RegDocDtoRequestByHands regDocDtoRequestByHands = new RegDocDtoRequestByHands();
        regDocDtoRequestByHands.parse(regDoctorJson);
        Doctor doctor = makeDoctorFromDtoRequest(regDocDtoRequestByHands);
        doctorDao.insertDoctorWithSchedule(doctor);
        RegDoctorDtoResponse response = makeDtoResponseFromDoctor(doctor);
        return gson.toJson(response);
    }
}
