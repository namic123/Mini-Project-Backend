package com.example.miniprojectbackend.service;

import com.example.miniprojectbackend.domain.Auth;
import com.example.miniprojectbackend.domain.Member;
import com.example.miniprojectbackend.mapper.BoardMapper;
import com.example.miniprojectbackend.mapper.MemberMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.WebRequest;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberMapper mapper;
    private final BoardMapper boardMapper;

    // 회원 등록 로직
    // 성공 여부를 반환
    public boolean add(Member member) {
        return mapper.insert(member) == 1;

    }

    // ID 중복 체크 로직
    public String getId(String id) {
        return mapper.selectId(id);
    }

    // Email 중복 체크 로직
    public String getEmail(String email) {
        return mapper.selectEmail(email);
    }

    // 닉네임 중복 체크 로직
    public String getNickName(String nickName) {
        return mapper.selectNickName(nickName);
    }

    // 회원 등록 요청 시 null 값 검증 로직
    public boolean validate(Member member) {
        if (member == null) {
            return false;
        }
        if (member.getPassword().isBlank()) {
            return false;
        }
        if (member.getEmail().isBlank()) {
            return false;
        }
        if (member.getId().isBlank()) {
            return false;
        }
        return true;
    }

    // 회원 목록 로직
    public List<Member> list() {
        return mapper.selectAll();
    }

    // 회원 보기 로직
    public Member getMember(String id) {
        return mapper.getMemberById(id);
    }

    // 회원 탈퇴 로직
    public boolean deleteMember(String id) {
        boardMapper.deleteByWriter(id);

        return mapper.deleteById(id) == 1;
    }

    // 회원 수정 로직
    public boolean update(Member member) {
        return mapper.updateMember(member) == 1;
    }

    // 로그인 로직 구현
    public boolean login(Member member, WebRequest request) {
        // DB에서 사용자 ID에 해당하는 Member객체 조회
        Member dbMember = mapper.getMemberById(member.getId());



        // DB에서 사용자를 찾았는지 확인
        if (dbMember != null) {
            // 사용자가 입력한 비밀번호와 DB에 저장된 비밀번호가 같은지 검증
            if (dbMember.getPassword().equals(member.getPassword())) {
                List<Auth> auth = mapper.selectAuthById(member.getId());
                dbMember.setAuth(auth);
                // 보안상, 비밀번호 필드를 비움
                dbMember.setPassword("");
/*               해당 member의 정보를 담는 객체를 웹 서버의 세션 영역에 'login'이라는 이름의 속성에 저장,
                 웹 서버는 사용자의 브라우저에 세션 ID를 쿠키 형태로 전송한다.
                 사용자 브라우저에는 SessinID로 저장되어 있으므로, 사용자 요청 시 Session ID와 함께 요청이 되고
                 서버는 이 세션 ID로 세션 정보 접근 가능
                 여기서는 사용자가 로그인 상태를 유지하고 있는지 추적하는 역할을 함.
                 login 속성은 세션 영역에 저장되어 있으므로, @SessionAttribute를 통해 객체를 가져올 수 있음*/
                request.setAttribute("login", dbMember, RequestAttributes.SCOPE_SESSION);
                // 로그인 성공
                return true;
            }
        }
        // 로그인 실패
        return false;
    }

    // 접근 권한 여부 로직
    public boolean hasAccess(String id, Member login) {
        if(isAdmin(login)){
            return true;
        }

        return login.getId().equals(id);
    }

    // 관리자 여부 검증 로직
    public boolean isAdmin(Member login){
        if(login.getAuth() != null){
            // 로그인의 auth 리스트의 요소 중 admin이 있는지 확인
            return login.getAuth()
                    .stream()
                    .map((e)-> e.getName())
                    .anyMatch(n->n.equals("admin"));
        }
        return false;
    }
}
