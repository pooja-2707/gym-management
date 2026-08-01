package com.gym.management.repository;

import com.gym.management.entity.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

    // Find attendance by member
    List<Attendance> findByMemberId(Long memberId);

    // Find attendance by date
    List<Attendance> findByDate(LocalDate date);

    // Find attendance by member and date
    List<Attendance> findByMemberIdAndDate(Long memberId, LocalDate date);
}
