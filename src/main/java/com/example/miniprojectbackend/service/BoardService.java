package com.example.miniprojectbackend.service;

import com.example.miniprojectbackend.domain.Board;
import com.example.miniprojectbackend.domain.Member;
import com.example.miniprojectbackend.mapper.BoardMapper;
import com.example.miniprojectbackend.mapper.FileMapper;
import com.example.miniprojectbackend.mapper.LikeMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardMapper mapper;
    private final MemberService memberService;
    private final LikeMapper likeMapper;
    private final FileMapper fileMapper;

    // 게시글 등록 로직
    public boolean save(Board board, MultipartFile[] files, Member login) {
        // 글 작성 시 작성자를 ID로 선언
        board.setWriter(login.getId());
        int cnt = mapper.insert(board);

        // 파일이 입력된 경우에만 실행
        if(files != null) {
            // 파일 배열을 반복문으로 돌려서 insert
            for(int i=0; i< files.length; i++) {
                // boardFile 테이블에 files 정보 저장
                // boardId, name
                fileMapper.insert(board.getId(), files[i].getOriginalFilename());
                // 파일을 S3 bucket에 upload
                // 일단 local에 저장
                upload(board.getId(), files[i]);
            }
        }
        return cnt == 1;
    }

    // 파일 업로드를 수행하는 메서드
    private void upload(Integer boardId, MultipartFile file) {
        // 파일 저장 경로
        // C:\Temp\prj1\게시물번호\파일명
        try{
            // 파일을 저장할 경로 폴더 지정
            File folder = new File("C:\\temp\\mini-project\\" + boardId);
            // 해당 폴더가 존재하지 않을 경우 폴더 생성
            if (!folder.exists()) {
                folder.mkdirs();
            }
            //  folder.getAbsolutePath()는 folder라는 File 객체의 절대 경로를 문자열로 반환
            // 구분자와 함께 파일명과 함께 저장
            // 즉, "C:\\temp\\mini-project\\게시물번호\\파일명"
            String path = folder.getAbsolutePath() + "\\" + file.getOriginalFilename();
            // 생성된 경로 path를 사용하여 File객체를 생성
            // transferTo 메서드는 MultipartFile 객체에 포함된 데이터를 지정된 경로에 있는 새 파일로 복사
            file.transferTo(new File(path));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    // 게시글 null 값 여부 검증 로직
    public boolean validate(Board board) {
        if (board == null) {
            return false;
        }
        // 본문 검증
        if (board.getContent() == null || board.getContent().isBlank()) {
            return false;
        }

        // 제목 검증
        if (board.getTitle() == null || board.getTitle().isBlank()) {
            return false;

        }

        return true;
    }
    // 게시글 목록 로직
    public Map<String, Object> list(Integer page, String keyword) {
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> pageInfo = new HashMap<>();

        // 페이징 처리
        int countAll = mapper.countAll("%"+keyword+"%");
        // 총 페이지의 마지막 번호
/*        +1이 필요한 이유
          총 게시글이 39개라고 가정
          (39 / 10) = 3, 실제로 4 페이지가 필요하나 3페이지만 나옴
          ((39-1)/10)+1 = 3+1 = 4페이지가 보장
          -1이 필요한 이유
          (40 / 10)+1 = 4+1= 5, 4페이지만 필요하지만 5가 연산됨
          ((40-1)/10)+1 = (39/10)+1 = 3 +1 = 4페이지로 딱 떨어짐
          즉 -1과 +1은 페이지의 수를 보장하기 위함.*/
        int lastPageNumber = (countAll -1) / 10 +1;

        // 페이지 그룹의 첫번째 번호
        /* +1이 필요한 이유 - 25페이지라고 가정
        * (25/10)*10 = 2*10 = 20, 21~30페이지 그룹에 속해있어야 하지만 20으로 떨어짐
        * (25/10)*10+1 = 21페이지 그룹에 속해있게 됨.
        * -1 이 필요한 이유 - 20페이지라고 가정
        * (20/10)*10+1 = 21, 11~20페이지 에 속해 있어야하지만 21이 됨
        * (20-1/10)*10+1 = (19/10)*10+1 = 11페이지 그룹에 속해 있게 됨.
        * */
        int startPageNumber = (page -1)/10 * 10 +1;
        // 해당 페이지 그룹의 끝 번호(예: 11~20 에서 20)
        int endPageNumber = startPageNumber +9;
        /* 가장 끝 페이지 그룹에서 마지막 페이지를 나타내기 위함  */
        /* 예: 45가 마지막 페이지라고 가정했을 때 41~45를 나타내기 위함*/
        endPageNumber = Math.min(endPageNumber, lastPageNumber);

        // 현재 페이지 정보를 나타내기 위함.
        pageInfo.put("currentPageNumber", page);

        // 페이지 그룹 첫 번째와 마지막 번호 할당
        pageInfo.put("startPageNumber", startPageNumber);
        pageInfo.put("endPageNumber", endPageNumber);

        // 페이지 그룹의 이전, 이후 그룹 번호 할당
        /* 시작 번호 -10으로 이전 그룹으로 이동 */
        int prevPageNumber = startPageNumber -10;
        /* 끝 번호 +1로 이후 그룹으로 이동 */
        int nextPageNumber = endPageNumber +1;
        /* 현재 페이지가 1~10인 경우 prevPage는 0이 되므로 오류 방지*/
        if(prevPageNumber > 0){
            pageInfo.put("prevPageNumber", startPageNumber -10);
        }
        /* nextPageNumber가 가장 마지막 페이지 보다 작거나 클 때만 할당 */
        if(nextPageNumber < lastPageNumber){
            pageInfo.put("nextPageNumber", nextPageNumber);
        }

        // 페이징
        /* LIMIT의 첫 번째 항목의 인덱스가 0부터 시작하기 때문에 -1을 해준다.
         LIMIT 첫번째 인수는 데이터의 인덱스를 나타남.
         즉 여기서는 10개씩 페이지에 나누어지므로 n번째 글부터 10개씩 반환
         (1-1)*10 = 0 -> LIMIT 0, 10 - 1페이지 - 1번 글 부터 10개 데이터 반환  1~10
         (2-1)*10 = 10 -> LIMIT 10,10, - 2페이지 - 10번 글 부터 10개씩 반환 10~20
         */
        int from = (page -1) * 10;
        // 게시글을 from번째 부터 10개씩 객체형태로 담긴다.
        map.put("boardList", mapper.loadList(from, "%"+keyword+"%"));
        // 페이지 그룹을 나타내는 startPageNumber와 lastPageNumber가 담긴다.
        map.put("pageInfo", pageInfo);
        return map;
    }

    // 게시글 보기 로직
    public Board get(Integer id) {  // view
        return mapper.selectById(id);
    }

    // 게시글 삭제 로직
    public boolean remove(Integer id) {
        likeMapper.deleteByBoardId(id);
        return mapper.deleteById(id);

    }

    // 게시글 수정 로직
    public boolean update(Board board) {
        return mapper.update(board);
    }

    // 접근 권한 가능 여부
    public boolean hasAccess(Integer id, Member login) {
        if (login == null) {
            return false;
        }

        if (login.isAdmin()) {
            return true;
        }
        if( memberService.isAdmin(login)){
            return true;
        }
        // 해당 글의 작성자 id를 가져옴
        Board board = mapper.selectById(id);
        // 작성자와 로그인 아이디가 동일한지 검증
        return board.getWriter().equals(login.getId());
    }

}
