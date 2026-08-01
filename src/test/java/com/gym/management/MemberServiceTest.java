package com.gym.management;

/*
 * =============================================
 * WHY THIS TEST CLASS EXISTS (MemberServiceTest)
 * =============================================
 * 
 * Unit testing tests a single class in ISOLATION (without connecting to MySQL).
 * 
 * WHY USE MOCKITO?
 * - We don't want tests to fail if MySQL is down.
 * - We mock the MemberRepository so it returns fake data.
 * - Tests run fast (<1 second)!
 * 
 * KEY ANNOTATIONS:
 * - @ExtendWith(MockitoExtension.class) → Enables Mockito in JUnit 5
 * - @Mock → Creates a fake instance of MemberRepository
 * - @InjectMocks → Creates MemberService and injects fake repository into it
 * - @Test → Marks a method as a test case
 */

import com.gym.management.entity.Member;
import com.gym.management.repository.MemberRepository;
import com.gym.management.service.MemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MemberServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private MemberService memberService;

    private Member sampleMember;

    @BeforeEach
    void setUp() {
        sampleMember = new Member();
        sampleMember.setId(1L);
        sampleMember.setName("Rahul Sharma");
        sampleMember.setPhone("9876543210");
        sampleMember.setEmail("rahul@example.com");
        sampleMember.setAge(25);
        sampleMember.setGender("Male");
        sampleMember.setAddress("Delhi, India");
        sampleMember.setJoinDate(LocalDate.of(2026, 1, 15));
    }

    @Test
    @DisplayName("Should save member successfully")
    void testSaveMember() {
        when(memberRepository.save(any(Member.class))).thenReturn(sampleMember);

        Member savedMember = memberService.saveMember(sampleMember);

        assertThat(savedMember).isNotNull();
        assertThat(savedMember.getName()).isEqualTo("Rahul Sharma");
        verify(memberRepository, times(1)).save(sampleMember);
    }

    @Test
    @DisplayName("Should return all members")
    void testGetAllMembers() {
        Member member2 = new Member();
        member2.setId(2L);
        member2.setName("Priya Patel");

        when(memberRepository.findAll()).thenReturn(Arrays.asList(sampleMember, member2));

        List<Member> members = memberService.getAllMembers();

        assertThat(members).hasSize(2);
        assertThat(members.get(0).getName()).isEqualTo("Rahul Sharma");
        assertThat(members.get(1).getName()).isEqualTo("Priya Patel");
    }

    @Test
    @DisplayName("Should find member by ID when member exists")
    void testGetMemberByIdFound() {
        when(memberRepository.findById(1L)).thenReturn(Optional.of(sampleMember));

        Optional<Member> result = memberService.getMemberById(1L);

        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("Rahul Sharma");
    }

    @Test
    @DisplayName("Should return empty optional when member ID does not exist")
    void testGetMemberByIdNotFound() {
        when(memberRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<Member> result = memberService.getMemberById(99L);

        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Should delete member by ID")
    void testDeleteMember() {
        doNothing().when(memberRepository).deleteById(1L);

        memberService.deleteMember(1L);

        verify(memberRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Should search members by keyword")
    void testSearchMembers() {
        when(memberRepository.findByNameContainingIgnoreCaseOrPhoneContaining("Rahul", "Rahul"))
                .thenReturn(List.of(sampleMember));

        List<Member> results = memberService.searchMembers("Rahul");

        assertThat(results).hasSize(1);
        assertThat(results.get(0).getName()).isEqualTo("Rahul Sharma");
    }
}
