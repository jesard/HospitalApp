package net.thumbtack.school.hospital.service;

import net.thumbtack.school.hospital.dto.request.DeleteDoctorDtoRequest;
import net.thumbtack.school.hospital.dto.request.RegCommissionDtoRequest;
import net.thumbtack.school.hospital.dto.request.regdoctor.RegDocDtoRequest;
import net.thumbtack.school.hospital.dto.request.regdoctor.WeekDaysSchedule;
import net.thumbtack.school.hospital.dto.request.regdoctor.WeekSchedule;
import net.thumbtack.school.hospital.dto.response.EmptyJsonResponse;
import net.thumbtack.school.hospital.error.Field;
import net.thumbtack.school.hospital.error.MyError;
import net.thumbtack.school.hospital.dto.response.RegCommissionDtoResponse;
import net.thumbtack.school.hospital.dto.response.RegPatientDtoResponse;
import net.thumbtack.school.hospital.dto.response.regdoctor.DayScheduleDtoResponse;
import net.thumbtack.school.hospital.dto.response.regdoctor.RegDoctorDtoResponse;
import net.thumbtack.school.hospital.dto.response.regdoctor.SlotDtoResponse;
import net.thumbtack.school.hospital.error.ServerErrorCode;
import net.thumbtack.school.hospital.error.ServerException;
import net.thumbtack.school.hospital.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public class DoctorService extends UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DoctorService.class);

    private DayOfWeek getDayOfWeek(String day) {
        switch (day) {
            case "Mon": return DayOfWeek.MONDAY;
            case "Tue": return DayOfWeek.TUESDAY;
            case "Wed": return DayOfWeek.WEDNESDAY;
            case "Thu": return DayOfWeek.THURSDAY;
            case "Fri": return DayOfWeek.FRIDAY;
        }

        return null;
    }

    private DaySchedule makeSlotSchedule(Doctor doctor, LocalDate date, LocalTime timeStart, LocalTime timeEnd, int duration) {
        DaySchedule daySchedule = new DaySchedule();
        daySchedule.setDoctor(doctor);
        daySchedule.setDate(date);
        List<Slot> slots = new ArrayList<>();
        for (LocalTime time = timeStart; time.plusMinutes(duration).isBefore(timeEnd.plusMinutes(1)); time = time.plusMinutes(duration)) {
            Slot slot = new Slot(time, time.plusMinutes(duration - 1));
            slot.setDaySchedule(daySchedule);
            slots.add(slot);
        }
        daySchedule.setSlotSchedule(slots);
        return daySchedule;
    }

    private List<DaySchedule> makeScheduleFromDtoRequest(RegDocDtoRequest request, Doctor doctor) {
        int duration = request.getDuration();
        LocalDate dateStart = LocalDate.parse(request.getDateStart(), formatterDate);
        LocalDate dateEnd = LocalDate.parse(request.getDateEnd(), formatterDate);
        EnumSet<DayOfWeek> workDays = EnumSet.noneOf(DayOfWeek.class);
        List<DaySchedule> schedule = new ArrayList<>();
        WeekSchedule weekSchedule = request.getWeekSchedule();
        List<WeekDaysSchedule> weekDaysSchedule = request.getWeekDaysSchedules();

        if (weekSchedule != null
                && weekSchedule.getTimeStart() != null
                && weekSchedule.getTimeEnd() != null) {
            LocalTime timeStart = LocalTime.parse(weekSchedule.getTimeStart(), formatterTime);
            LocalTime timeEnd = LocalTime.parse(weekSchedule.getTimeEnd(), formatterTime);
            if (weekSchedule.getWeekDays().size() != 0) {
                for (String dayOfWeek: weekSchedule.getWeekDays()) {
                    workDays.add(getDayOfWeek(dayOfWeek));
                }
            } else {
                for (int i = 1; i < 6; i++) {
                    workDays.add(DayOfWeek.of(i));
                }
            }
            for (LocalDate date = dateStart; date.isBefore(dateEnd.plusDays(1)); date = date.plusDays(1)) {
                if (workDays.contains(date.getDayOfWeek())) {
                    schedule.add(makeSlotSchedule(doctor, date, timeStart, timeEnd, duration));
                }
            }
        }


        if (weekDaysSchedule != null
                && weekDaysSchedule.size() != 0) {
            for (WeekDaysSchedule day: weekDaysSchedule) {
                workDays.add(getDayOfWeek(day.getWeekDay()));
            }
            for (LocalDate date = dateStart; date.isBefore(dateEnd.plusDays(1)); date = date.plusDays(1)) {
                if (workDays.contains(date.getDayOfWeek())) {
                    for (WeekDaysSchedule day: weekDaysSchedule) {
                        if (date.getDayOfWeek() == getDayOfWeek(day.getWeekDay())) {
                            LocalTime timeStart = LocalTime.parse(day.getTimeStart(), formatterTime);
                            LocalTime timeEnd = LocalTime.parse(day.getTimeEnd(), formatterTime);
                            schedule.add(makeSlotSchedule(doctor, date, timeStart, timeEnd, duration));
                        }
                    }
                }
            }
        }
        return schedule;
    }

    private Doctor makeDoctorFromDtoRequest(RegDocDtoRequest request) {
        Doctor doctor = new Doctor(request.getFirstName(), request.getLastName(), request.getPatronymic(), request.getLogin(), request.getPassword(), request.getSpeciality(), request.getRoom());
        List<DaySchedule> schedule = makeScheduleFromDtoRequest(request, doctor);
        doctor.setSchedule(schedule);
        return doctor;
    }

    private boolean ifSchedulesIntersect(List<DaySchedule> oldSchedule, List<DaySchedule> newSchedule) {
        LocalDate oldStartDate = oldSchedule.get(0).getDate();
        LocalDate oldEndDate = oldSchedule.get(oldSchedule.size() - 1).getDate();
        LocalDate newStartDate = newSchedule.get(0).getDate();
        LocalDate newEndDate = newSchedule.get(newSchedule.size() - 1).getDate();
        return !oldStartDate.isAfter(newEndDate) && !oldEndDate.isBefore(newStartDate);

    }

    private List<DayScheduleDtoResponse> makeScheduleResponseFromDoctor(Doctor doctor) {
        List<DayScheduleDtoResponse> schedule = new ArrayList<>();
        for (DaySchedule day: doctor.getSchedule()) {
            DayScheduleDtoResponse dayResponse = new DayScheduleDtoResponse();
            dayResponse.setDate(day.getDate().format(formatterDate));
            List<SlotDtoResponse> slotsForResponse = new ArrayList<>();
            for (Slot slot: day.getSlotSchedule()) {
                SlotDtoResponse slotResponse = new SlotDtoResponse();
                slotResponse.setTime(slot.getTimeStart().format(formatterTime));
                slotsForResponse.add(slotResponse);
            }
            dayResponse.setSlots(slotsForResponse);
            schedule.add(dayResponse);
        }
        return schedule;
    }

    private List<DayScheduleDtoResponse> makeScheduleResponseFromDoctor(Doctor doctor, LocalDate dateStart, LocalDate dateEnd, int patientId) throws ServerException {
        List<DayScheduleDtoResponse> schedule = new ArrayList<>();
        for (DaySchedule day: doctor.getSchedule()) {
            LocalDate date = day.getDate();
            if (date.isAfter(dateStart.minusDays(1)) && date.isBefore(dateEnd.plusDays(1))) {
                DayScheduleDtoResponse dayResponse = new DayScheduleDtoResponse();
                dayResponse.setDate(day.getDate().toString());
                List<SlotDtoResponse> slotsForResponse = makeSlotsForDate(day, patientId);
                dayResponse.setSlots(slotsForResponse);
                schedule.add(dayResponse);
            }
        }
        return schedule;
    }

    private List<SlotDtoResponse> makeSlotsForDate(DaySchedule daySchedule, int patientId) throws ServerException {
        List<SlotDtoResponse> slotsForResponse = new ArrayList<>();
        if (patientId == 0) {
            for (Slot slot : daySchedule.getSlotSchedule()) {
                SlotDtoResponse slotResponse = new SlotDtoResponse();
                slotResponse.setTime(slot.getTimeStart().toString());
                Patient patient = slot.getPatient();
                if (slot.getPatient() != null) {
                    User user = userDao.getUserById(patient.getUserId());
                    RegPatientDtoResponse regPatientDtoResponse = new RegPatientDtoResponse(patientId,
                            user.getFirstName(), user.getLastName(), user.getPatronymic(),
                            patient.getEmail(), patient.getAddress(), patient.getPhone());
                    slotResponse.setPatient(regPatientDtoResponse);
                }
                slotsForResponse.add(slotResponse);
            }
        } else {
            for (Slot slot : daySchedule.getSlotSchedule()) {
                SlotDtoResponse slotResponse = new SlotDtoResponse();
                slotResponse.setTime(slot.getTimeStart().toString());
                Patient patient = slot.getPatient();
                if (patient != null && patient.getId() == patientId) {
                    User user = userDao.getUserById(patient.getUserId());
                    RegPatientDtoResponse regPatientDtoResponse = new RegPatientDtoResponse(patientId,
                            user.getFirstName(), user.getLastName(), user.getPatronymic(),
                            patient.getEmail(), patient.getAddress(), patient.getPhone());
                    slotResponse.setPatient(regPatientDtoResponse);
                }
                slotsForResponse.add(slotResponse);
            }
        }
        return slotsForResponse;
    }

    public RegDoctorDtoResponse registerDoctor(RegDocDtoRequest request, String token) throws ServerException {
        LOGGER.debug("Service insert doctor {}", request.getLastName());
        if (!getUserDecriptorByToken(token).equals(ADMIN)) {
            LOGGER.info("Can't insert doctor {} - {}", request.getLastName(), ServerErrorCode.WRONG_USER.getErrorString());
            throw new ServerException(new MyError(ServerErrorCode.WRONG_USER, Field.COOKIE));
        }
        Doctor doctor = makeDoctorFromDtoRequest(request);
        doctorDao.insertDoctor(doctor);
        RegDoctorDtoResponse response = new RegDoctorDtoResponse(doctor.getId(), doctor.getFirstName(), doctor.getLastName(), doctor.getPatronymic(), doctor.getSpeciality(), doctor.getRoom());;
        response.setSchedule(makeScheduleResponseFromDoctor(doctor));
        return response;
    }

    public RegDoctorDtoResponse getDoctor(int doctorId, String showSchedule, String startDate, String endDate, String token) throws ServerException {
        LOGGER.debug("Service get doctor with id {}, showSchedule = {}, dates {} {}", doctorId, showSchedule, startDate, endDate);
        int userId = doctorDao.getUserIdByDoctorId(doctorId);
        Doctor doctor;
        RegDoctorDtoResponse response = new RegDoctorDtoResponse();
        if (showSchedule.equals("no")) {
            doctor = doctorDao.getDoctorWithoutScheduleByDoctorId(doctorId);
            response = new RegDoctorDtoResponse(doctor.getId(), doctor.getFirstName(), doctor.getLastName(), doctor.getPatronymic(), doctor.getSpeciality(), doctor.getRoom());
        }
        if (showSchedule.equals("yes")) {
            doctor = doctorDao.getDoctorByUserId(userId);
            LocalDate dateStart;
            LocalDate dateEnd;
            if (startDate == null) {
                dateStart = LocalDate.now();
            } else {
                dateStart = LocalDate.parse(startDate, formatterDate);
            }
            if (endDate == null) {
                dateEnd = LocalDate.now().plusMonths(2);
            } else {
                dateEnd = LocalDate.parse(endDate, formatterDate);
            }
            int patientId = 0;
            if (getUserDecriptorByToken(token).equals(PATIENT)) {
                patientId = ((RegPatientDtoResponse) getUserDtoResponseByToken(token)).getId();
            }
            response = new RegDoctorDtoResponse(doctor.getId(), doctor.getFirstName(), doctor.getLastName(), doctor.getPatronymic(), doctor.getSpeciality(), doctor.getRoom());
            response.setSchedule(makeScheduleResponseFromDoctor(doctor, dateStart, dateEnd, patientId));
        }
        return response;

    }

    public List<RegDoctorDtoResponse> getDoctors(String showSchedule, String speciality, String startDate, String endDate, String token) throws ServerException {
        LOGGER.debug("Service get doctors, speciality = {}, showSchedule = {}, dates {} {}", speciality, showSchedule, startDate, endDate);
        List<RegDoctorDtoResponse> responseList = new ArrayList<>();
        List<Integer> doctorIdsList;
        if (speciality == null) {
            doctorIdsList = doctorDao.getAllDoctorIds();
        } else {
            doctorIdsList = doctorDao.getAllDoctorIdsBySpeciality(speciality);
        }
        for (int doctorId: doctorIdsList) {
            RegDoctorDtoResponse regDoctorDtoResponse = getDoctor(doctorId, showSchedule, startDate, endDate, token);
            responseList.add(regDoctorDtoResponse);
        }
        return responseList;
    }

    public RegDoctorDtoResponse updateDoctorSchedule(RegDocDtoRequest request, int doctorId, String token) throws ServerException {
        LOGGER.debug("Service update schedule of doctor with id {}", doctorId);
        if (!getUserDecriptorByToken(token).equals(ADMIN)) {
            throw new ServerException(new MyError(ServerErrorCode.WRONG_USER, Field.COOKIE));
        }
        int userId = doctorDao.getUserIdByDoctorId(doctorId);
        Doctor doctorWithOldSchedule = doctorDao.getDoctorByUserId(userId);
        List<DaySchedule> oldSchedule = doctorWithOldSchedule.getSchedule();
        List<DaySchedule> newSchedule = makeScheduleFromDtoRequest(request, doctorWithOldSchedule);
        String room = doctorWithOldSchedule.getRoom();
        if (!ifSchedulesIntersect(oldSchedule, newSchedule)) {
            doctorDao.insertSchedule(doctorId, room, newSchedule);
        } else {
            doctorDao.updateSchedule(doctorId, room, oldSchedule, newSchedule);
        }
        Doctor doctorWithNewSchedule = doctorDao.getDoctorByUserId(userId);
        RegDoctorDtoResponse response = new RegDoctorDtoResponse(
                doctorWithNewSchedule.getId(),
                doctorWithNewSchedule.getFirstName(),
                doctorWithNewSchedule.getLastName(),
                doctorWithNewSchedule.getPatronymic(),
                doctorWithNewSchedule.getSpeciality(),
                doctorWithNewSchedule.getRoom());
        response.setSchedule(makeScheduleResponseFromDoctor(doctorWithNewSchedule));
        return response;
    }

    public EmptyJsonResponse deleteDoctor(DeleteDoctorDtoRequest request, int doctorId, String token) throws ServerException {
        LOGGER.debug("Service set firing date = {} for doctor with id {} ", request.getDate(), doctorId);
        if (!getUserDecriptorByToken(token).equals(ADMIN)) {
            LOGGER.info("Can't set firing date = {} for doctor with id {} - {}", request.getDate(), doctorId, ServerErrorCode.WRONG_USER.getErrorString());
            throw new ServerException(new MyError(ServerErrorCode.WRONG_USER, Field.COOKIE));
        }
        LocalDate date = LocalDate.parse(request.getDate(), formatterDate);

        doctorDao.setTerminationDate(doctorId, date);
        List<Slot> deletedTickets = doctorDao.deleteScheduleFromDate(doctorId, date);
        for (Slot slot: deletedTickets) {
            String subject = "Ticket " + slot.getTicketNumber() + "at " + slot.getDaySchedule().getDate() + " " + slot.getTimeStart() + " was canceled";
            sendEmail(slot.getPatient().getAddress(), subject, subject);
            sendSMS(slot.getPatient().getPhone(), subject, subject);
        }
        return new EmptyJsonResponse();
    }

    public RegCommissionDtoResponse registerCommission(RegCommissionDtoRequest request, String token) throws ServerException {
        LOGGER.debug("Service register commission for patient with id {} ", request.getPatientId());
        DayOfWeek dayOfWeek = LocalDate.parse(request.getDate(), formatterDate).getDayOfWeek();
        if (dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY) {
            LOGGER.info("Can't register commission for patient with id {} - {}", request.getPatientId(), ServerErrorCode.WRONG_DATE.getErrorString());
            throw new ServerException(new MyError(ServerErrorCode.WRONG_DATE, Field.DATE, request.getDate()));
        }
        if (!getUserDecriptorByToken(token).equals(DOCTOR)) {
            LOGGER.info("Can't register commission for patient with id {} - {}", request.getPatientId(), ServerErrorCode.WRONG_USER.getErrorString());
            throw new ServerException(new MyError(ServerErrorCode.WRONG_USER, Field.COOKIE));
        }
        int patientId = request.getPatientId();
        LocalDate date = LocalDate.parse(request.getDate(), formatterDate);
        LocalTime timeStart = LocalTime.parse(request.getTime(), formatterTime);
        LocalTime timeEnd = timeStart.plusMinutes(request.getDuration());

        List<Slot> slots = patientDao.getBusySlotsByPatientIdDate(patientId, date);
        for (Slot slot: slots) {
            if (slot.getTimeStart().isBefore(timeEnd.plusMinutes(1)) && slot.getTimeEnd().isAfter(timeStart.minusMinutes(1))) {
                LOGGER.info("Can't register commission for patient with id {} - {}", patientId, ServerErrorCode.PATIENT_IS_BUSY.getErrorString());
                throw new ServerException(new MyError(ServerErrorCode.PATIENT_IS_BUSY, Field.DATETIME, request.getDate() + " " +  request.getTime()));
            }
        }
        Doctor doctorByToken = (Doctor) getUserByToken(token);
        List<Integer> doctorIds = request.getDoctorIds();
        if (!doctorIds.contains(doctorByToken.getId())) {
            doctorIds.add(doctorByToken.getId());
        }

        boolean roomIsValid = false;
        int roomId = doctorDao.getRoomId(request.getRoom());
        for (int doctorId: doctorIds) {
            if (roomId == doctorDao.getRoomId(doctorId)) {
                roomIsValid = true;
                break;
            }
        }
        if (!roomIsValid) {
            LOGGER.info("Can't register commission for patient with id {} - {}", request.getPatientId(), ServerErrorCode.WRONG_ROOM.getErrorString());
            throw new ServerException(new MyError(ServerErrorCode.WRONG_ROOM, Field.ROOM, request.getRoom()));
        }

        StringBuilder ticketNumber = new StringBuilder("C");
        for (int doctorId: doctorIds) {
            ticketNumber.append("D").append(doctorId);
        }
        String ticketNumber2 = ticketNumber.append(request.getDate().replace("-","")).append(request.getTime().replace(":", "")).toString();

        for (int doctorId: doctorIds) {
            List<Integer> slotIds = doctorDao.getSlotIdsByDateTimeRange(doctorId, date, timeStart, timeEnd);
            patientDao.makeAppointments(request.getPatientId(), slotIds, ticketNumber2);
        }
        doctorDao.insertCommission(timeStart, timeEnd, request.getRoom(), ticketNumber2, request.getPatientId());

        return new RegCommissionDtoResponse(ticketNumber2, request.getPatientId(), doctorIds, request.getRoom(), request.getDate(), request.getTime(), request.getDuration());
    }


}
