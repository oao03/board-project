package com.study.board.entity;

import com.study.board.dto.MemberDTO;
import jakarta.persistence.*;
        import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String memberEmail;

    @Column
    private String memberPassword;

    @Column
    private String memberName;

    // DTO → Entity 변환용
    public static Member toMember(MemberDTO dto) {
        Member member = new Member();
        member.setMemberEmail(dto.getMemberEmail());
        member.setMemberPassword(dto.getMemberPassword());
        member.setMemberName(dto.getMemberName());
        return member;
    }
}
