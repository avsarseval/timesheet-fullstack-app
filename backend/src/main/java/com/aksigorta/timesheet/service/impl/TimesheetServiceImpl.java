package com.aksigorta.timesheet.service.impl;

import com.aksigorta.timesheet.dto.TimesheetDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import com.aksigorta.timesheet.model.Timesheet;
import com.aksigorta.timesheet.model.User;
import com.aksigorta.timesheet.repository.TimesheetRepository;
import com.aksigorta.timesheet.service.TimesheetService;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class TimesheetServiceImpl implements TimesheetService {

    private final TimesheetRepository timesheetRepository;

    public TimesheetServiceImpl(TimesheetRepository timesheetRepository) {
        this.timesheetRepository = timesheetRepository;
    }

    @Override
    public Timesheet createTimesheet(Timesheet timesheet, User user) {
        if (timesheet.getEndTime() != null && timesheet.getStartTime() != null && timesheet.getEndTime().isBefore(timesheet.getStartTime())) {
            throw new IllegalArgumentException("End time cannot be before start time.");
        }
        // --- YİNELENEN KAYIT KONTROLÜ ---
        List<Timesheet> existingTimesheets = timesheetRepository
                .findByUserIdAndDateAndStartTimeBeforeAndEndTimeAfter(
                        user.getId(),
                        timesheet.getDate(),
                        timesheet.getEndTime(),
                        timesheet.getStartTime()
                );

        if (!existingTimesheets.isEmpty()) {
            throw new IllegalStateException("A timesheet already exists for this time period.");
        }
        timesheet.setUser(user);
        return timesheetRepository.save(timesheet);
    }
    @Override
    public Page<TimesheetDto> getTimesheetsForUser(User user, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("date").descending());
        Page<Timesheet> timesheetPage = timesheetRepository.findAllByUserId(user.getId(), pageable);
        return timesheetPage.map(this::convertToDto);
    }
    @Override
    public Page<TimesheetDto> getAllTimesheets(int page, int size) {
        // 1. Sayfa isteğini oluştur.
        Pageable pageable = PageRequest.of(page, size, Sort.by("date").descending());

        // 2. Depodan tüm kayıtları sayfalayarak çek.
        Page<Timesheet> timesheetPage = timesheetRepository.findAll(pageable);

        // 3. Çekilen sayfayı DTO sayfasına dönüştür.
        return timesheetPage.map(this::convertToDto);
    }

    @Override
    public Timesheet updateTimesheet(Long timesheetId, Timesheet updatedTimesheet, User user) {
        Timesheet existingTimesheet = timesheetRepository.findById(timesheetId)
                .orElseThrow(() -> new RuntimeException("Timesheet not found with id: " + timesheetId));

        if (!existingTimesheet.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("You are not authorized to update this timesheet");
        }

        if (updatedTimesheet.getEndTime() != null && updatedTimesheet.getStartTime() != null && updatedTimesheet.getEndTime().isBefore(updatedTimesheet.getStartTime())) {
            throw new IllegalArgumentException("End time cannot be before start time.");
        }
        List<Timesheet> existingTimesheets = timesheetRepository
                .findByUserIdAndDateAndStartTimeBeforeAndEndTimeAfter(
                        user.getId(),
                        updatedTimesheet.getDate(),
                        updatedTimesheet.getEndTime(),
                        updatedTimesheet.getStartTime()
                );

        // Eğer çakışan bir kayıt bulunduysa VE bu kayıt şu an güncellediğimiz kaydın kendisi DEĞİLSE hata ver.
        if (!existingTimesheets.isEmpty() && !existingTimesheets.get(0).getId().equals(timesheetId)) {
            throw new IllegalStateException("This update conflicts with an existing timesheet.");
        }
        existingTimesheet.setDate(updatedTimesheet.getDate());
        existingTimesheet.setStartTime(updatedTimesheet.getStartTime());
        existingTimesheet.setEndTime(updatedTimesheet.getEndTime());
        existingTimesheet.setDescription(updatedTimesheet.getDescription());

        return timesheetRepository.save(existingTimesheet);
    }

    @Override
    public void deleteTimesheet(Long timesheetId, User user) {
        Timesheet existingTimesheet = timesheetRepository.findById(timesheetId)
                .orElseThrow(() -> new RuntimeException("Timesheet not found with id: " + timesheetId));

        if (!existingTimesheet.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("You are not authorized to delete this timesheet");
        }

        timesheetRepository.delete(existingTimesheet);
    }

    @Override
    public Timesheet adminUpdateTimesheet(Long timesheetId, Timesheet updatedTimesheet) {
        Timesheet existingTimesheet = timesheetRepository.findById(timesheetId)
                .orElseThrow(() -> new RuntimeException("Timesheet not found with id: " + timesheetId));

        // Admin olduğu için yetki kontrolü yapmıyoruz.

        // Validasyonları ekleyebiliriz.
        if (updatedTimesheet.getEndTime() != null && updatedTimesheet.getStartTime() != null && updatedTimesheet.getEndTime().isBefore(updatedTimesheet.getStartTime())) {
            throw new IllegalArgumentException("End time cannot be before start time.");
        }

        existingTimesheet.setDate(updatedTimesheet.getDate());
        existingTimesheet.setStartTime(updatedTimesheet.getStartTime());
        existingTimesheet.setEndTime(updatedTimesheet.getEndTime());
        existingTimesheet.setDescription(updatedTimesheet.getDescription());

        return timesheetRepository.save(existingTimesheet);
    }

    @Override
    public void adminDeleteTimesheet(Long timesheetId) {
        // Kaydın var olup olmadığını kontrol etmek, silmeden önce iyi bir pratiktir.
        if (!timesheetRepository.existsById(timesheetId)) {
            throw new RuntimeException("Timesheet not found with id: " + timesheetId);
        }
        // Admin olduğu için yetki kontrolü yapmıyoruz.
        timesheetRepository.deleteById(timesheetId);
    }

    @Override
    public TimesheetDto convertToDto(Timesheet timesheet) {
        return new TimesheetDto(
                timesheet.getId(),
                timesheet.getDate(),
                timesheet.getStartTime(),
                timesheet.getEndTime(),
                timesheet.getDescription(),
                timesheet.getUser().getUsername() // User nesnesinden sadece username'i alıyoruz
        );
    }
}