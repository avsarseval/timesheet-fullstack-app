package com.aksigorta.timesheet.service.impl;

import com.aksigorta.timesheet.dto.TimesheetCreateRequestDto;
import com.aksigorta.timesheet.dto.TimesheetResponseDto;
import com.aksigorta.timesheet.mapper.TimesheetMapper;
import com.aksigorta.timesheet.model.Timesheet;
import com.aksigorta.timesheet.model.User;
import com.aksigorta.timesheet.repository.TimesheetRepository;
import com.aksigorta.timesheet.service.TimesheetService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class TimesheetServiceImpl implements TimesheetService {

    private final TimesheetRepository timesheetRepository;
    private final TimesheetMapper timesheetMapper;

    public TimesheetServiceImpl(TimesheetRepository timesheetRepository,TimesheetMapper timesheetMapper) {
        this.timesheetRepository = timesheetRepository;
        this.timesheetMapper = timesheetMapper;
    }

    @Override
    public TimesheetResponseDto createTimesheet(TimesheetCreateRequestDto requestDto, User user) {
        // Bitiş saati başlangıçtan önce olamaz.
        if (requestDto.endTime() != null && requestDto.startTime() != null && requestDto.endTime().isBefore(requestDto.startTime())) {
            throw new IllegalArgumentException("End time cannot be before start time.");
        }

        // Bu zaman aralığında başka bir kayıt var mı?
        List<Timesheet> conflictingTimesheets = timesheetRepository
                .findByUserIdAndDateAndStartTimeBeforeAndEndTimeAfter(
                        user.getId(),
                        requestDto.date(),
                        requestDto.endTime(),
                        requestDto.startTime()
                );

        if (!conflictingTimesheets.isEmpty()) {
            throw new IllegalStateException("A timesheet already exists for this time period.");
        }

        Timesheet newTimesheet = timesheetMapper.toTimesheetEntity(requestDto);

        // Bu yeni Entity'nin kime ait olduğunu belirt.
        newTimesheet.setUser(user);

        Timesheet savedTimesheet = timesheetRepository.save(newTimesheet);

        // 6. Kaydedilen Entity'yi, cevaba uygun DTO'ya çevirip döndür.
        return timesheetMapper.toTimesheetResponseDto(savedTimesheet);
    }
    @Override
    public Page<TimesheetResponseDto> getTimesheetsForUser(User user, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("date").descending());
        Page<Timesheet> timesheetPage = timesheetRepository.findAllByUserId(user.getId(), pageable);
        return timesheetPage.map(timesheetMapper::toTimesheetResponseDto);
    }

    @Override
    public TimesheetResponseDto updateTimesheet(Long timesheetId, TimesheetCreateRequestDto requestDto, User user) {
        // Veritabanından güncellenecek olan asıl Entity'yi ID ile bul.
        Timesheet existingTimesheet = timesheetRepository.findById(timesheetId)
                .orElseThrow(() -> new RuntimeException("Timesheet not found with id: " + timesheetId));

        //  Yetki kontrolü: Bu kaydı güncellemeye bu kullanıcının izni var mı?
        if (!existingTimesheet.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("You are not authorized to update this timesheet");
        }

        // Bitiş saati başlangıçtan önce olamaz.
        if (requestDto.endTime() != null && requestDto.startTime() != null && requestDto.endTime().isBefore(requestDto.startTime())) {
            throw new IllegalArgumentException("End time cannot be before start time.");
        }

        // Güncelleme, başka bir kayıtla çakışıyor mu?
        List<Timesheet> conflictingTimesheets = timesheetRepository
                .findByUserIdAndDateAndStartTimeBeforeAndEndTimeAfter(
                        user.getId(),
                        requestDto.date(),
                        requestDto.endTime(),
                        requestDto.startTime()
                );

        // Eğer çakışan bir kayıt bulunduysa VE bu kayıt şu an güncellediğimiz kaydın kendisi DEĞİLSE hata ver.
        if (!conflictingTimesheets.isEmpty() && !conflictingTimesheets.get(0).getId().equals(timesheetId)) {
            throw new IllegalStateException("This update conflicts with an existing timesheet.");
        }

        // Mapper'ı kullanarak DTO'daki bilgileri var olan Entity'nin üzerine yaz.
        timesheetMapper.updateTimesheetFromDto(requestDto, existingTimesheet);

        Timesheet updatedTimesheet = timesheetRepository.save(existingTimesheet);

        // Sonucu Response DTO olarak döndür.
        return timesheetMapper.toTimesheetResponseDto(updatedTimesheet);
    }


    @Override
    public void deleteTimesheet(Long timesheetId, User user) {
        Timesheet existingTimesheet = timesheetRepository.findById(timesheetId)
                .orElseThrow(() -> new RuntimeException("Timesheet not found with id: " + timesheetId));

        // Yetki kontrolü
        if (!existingTimesheet.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("You are not authorized to delete this timesheet");
        }

        timesheetRepository.delete(existingTimesheet);
    }
}
