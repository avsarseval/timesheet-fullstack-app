package com.aksigorta.timesheet.service;

import com.aksigorta.timesheet.model.Timesheet;
import com.aksigorta.timesheet.model.User;
import com.aksigorta.timesheet.repository.TimesheetRepository;
import com.aksigorta.timesheet.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TimesheetService {

    private final TimesheetRepository timesheetRepository;
    private final UserRepository userRepository;

    @Autowired
    public TimesheetService(TimesheetRepository timesheetRepository, UserRepository userRepository) {
        this.timesheetRepository = timesheetRepository;
        this.userRepository = userRepository;
    }

    // YENİ BİR ZAMAN ÇİZELGESİ OLUŞTURMA TARİFİ
    public Timesheet createTimesheet(Timesheet timesheet, String username) {
        // "Bilekliği" okuyarak gelen username ile veritabanından tam User nesnesini bul.
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        // Gelen zaman çizelgesinin kime ait olduğunu ayarla.
        timesheet.setUser(user);

        // Depoya kaydetmesi için gönder.
        return timesheetRepository.save(timesheet);
    }

    // BELİRLİ BİR KULLANICININ TÜM ZAMAN ÇİZELGELERİNİ GETİRME TARİFİ
    public List<Timesheet> getTimesheetsByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        // Depodan sadece bu kullanıcının ID'sine sahip olanları iste.
        return timesheetRepository.findAllByUserId(user.getId());
    }

    // TODO: Güncelleme ve Silme metotları daha sonra buraya eklenecek.
}