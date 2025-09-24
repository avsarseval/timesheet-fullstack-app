package com.aksigorta.timesheet.mapper;

import com.aksigorta.timesheet.dto.TimesheetCreateRequestDto;
import com.aksigorta.timesheet.dto.TimesheetResponseDto;
import com.aksigorta.timesheet.model.Timesheet;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface TimesheetMapper {

    // Timesheet Entity'sini -> TimesheetResponseDto'ya çevirir.
    @Mapping(source = "user.username", target = "username")
    TimesheetResponseDto toTimesheetResponseDto(Timesheet timesheet);

    // TimesheetCreateRequestDto'yu -> Timesheet Entity'sine çevirir.
    Timesheet toTimesheetEntity(TimesheetCreateRequestDto requestDto);
    void updateTimesheetFromDto(TimesheetCreateRequestDto dto, @MappingTarget Timesheet entity);
}