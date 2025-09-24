package com.aksigorta.timesheet.service.impl;

import com.aksigorta.timesheet.dto.TimesheetCreateRequestDto;
import com.aksigorta.timesheet.dto.TimesheetResponseDto;
import com.aksigorta.timesheet.mapper.TimesheetMapper;
import com.aksigorta.timesheet.model.Timesheet;
import com.aksigorta.timesheet.repository.TimesheetRepository;
import com.aksigorta.timesheet.service.AdminService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.opencsv.CSVWriter;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {

    private final TimesheetRepository timesheetRepository;
    private final TimesheetMapper timesheetMapper;


    public AdminServiceImpl(TimesheetRepository timesheetRepository, TimesheetMapper timesheetMapper) {
        this.timesheetRepository = timesheetRepository;
        this.timesheetMapper = timesheetMapper;
    }

    @Override
    public Page<TimesheetResponseDto> getAllTimesheets(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("date").descending());
        Page<Timesheet> timesheetPage = timesheetRepository.findAll(pageable);
        return timesheetPage.map(timesheetMapper::toTimesheetResponseDto);
    }

    @Override
    public TimesheetResponseDto adminUpdateTimesheet(Long timesheetId, TimesheetCreateRequestDto requestDto) {

        // Veritabanından güncellenecek olan asıl Entity'yi bul.
        Timesheet existingTimesheet = timesheetRepository.findById(timesheetId)
                .orElseThrow(() -> new RuntimeException("Timesheet not found with id: " + timesheetId));

        if (requestDto.endTime() != null && requestDto.startTime() != null && requestDto.endTime().isBefore(requestDto.startTime())) {
            throw new IllegalArgumentException("End time cannot be before start time.");
        }

        timesheetMapper.updateTimesheetFromDto(requestDto, existingTimesheet);

        Timesheet savedTimesheet = timesheetRepository.save(existingTimesheet);

        return timesheetMapper.toTimesheetResponseDto(savedTimesheet);
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
    public void exportAllTimesheetsToCsv(HttpServletResponse response) throws IOException {
        response.setContentType("text/csv; charset= UTF-8");
        response.setHeader("Content-Disposition", "attachment; filename=all_timesheets.csv");

        List<Timesheet> allTimesheets = timesheetRepository.findAll(Sort.by("date").descending());
        response.getOutputStream().write(0xEF);
        response.getOutputStream().write(0xBB);
        response.getOutputStream().write(0xBF);
        //  CSVWriter'ı oluştur.
        try (CSVWriter writer = new CSVWriter(new OutputStreamWriter(response.getOutputStream()))) {
            String[] header = { "ID", "User", "Date", "Start Time", "End Time", "Description" };
            writer.writeNext(header);

            // Her bir zaman çizelgesini bir satır olarak yaz.
            for (Timesheet timesheet : allTimesheets) {
                String[] data = {
                        String.valueOf(timesheet.getId()),
                        timesheet.getUser().getUsername(),
                        timesheet.getDate().toString(),
                        timesheet.getStartTime().toString(),
                        timesheet.getEndTime().toString(),
                        timesheet.getDescription()
                };
                writer.writeNext(data);
            }
        }
    }
}