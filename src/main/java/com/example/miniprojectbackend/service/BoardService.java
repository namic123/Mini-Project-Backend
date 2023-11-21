package com.example.miniprojectbackend.service;

import com.example.miniprojectbackend.domain.Board;
import com.example.miniprojectbackend.domain.BoardFile;
import com.example.miniprojectbackend.domain.Member;
import com.example.miniprojectbackend.mapper.BoardMapper;
import com.example.miniprojectbackend.mapper.FileMapper;
import com.example.miniprojectbackend.mapper.LikeMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.ObjectCannedACL;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class) // checked 포함 문제 발생 시 rollback
public class BoardService {
    private final BoardMapper mapper;
    private final MemberService memberService;
    private final LikeMapper likeMapper;
    private final FileMapper fileMapper;

    // AWS
    @Value("${image.file.prefix}")
    private String urlPrefix;

    @Value("${aws.s3.bucket.name}")
    private String bucket;

    private final S3Client s3;

    // 게시글 등록 로직
    public boolean save(Board board, MultipartFile[] files, Member login) throws IOException {
        // 글 작성 시 작성자를 ID로 선언
        board.setWriter(login.getId());
        int cnt = mapper.insert(board);

        // 파일이 입력된 경우에만 실행
        if (files != null) {
            // 파일 배열을 반복문으로 돌려서 insert
            for (int i = 0; i < files.length; i++) {
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

    // AWS에 파일 업로드를 수행하는 메서드
    private void upload(Integer boardId, MultipartFile file) throws IOException {
        String key = "mini-project/" + boardId + "/" + file.getOriginalFilename();
        PutObjectRequest objectRequest = PutObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .acl(ObjectCannedACL.PUBLIC_READ)
                .build();

        s3.putObject(objectRequest, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));

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
        int countAll = mapper.countAll("%" + keyword + "%");
        // 총 페이지의 마지막 번호
/*        +1이 필요한 이유
          총 게시글이 39개라고 가정
          (39 / 10) = 3, 실제로 4 페이지가 필요하나 3페이지만 나옴
          ((39-1)/10)+1 = 3+1 = 4페이지가 보장
          -1이 필요한 이유
          (40 / 10)+1 = 4+1= 5, 4페이지만 필요하지만 5가 연산됨
          ((40-1)/10)+1 = (39/10)+1 = 3 +1 = 4페이지로 딱 떨어짐
          즉 -1과 +1은 페이지의 수를 보장하기 위함.*/
        int lastPageNumber = (countAll - 1) / 10 + 1;

        // 페이지 그룹의 첫번째 번호
        /* +1이 필요한 이유 - 25페이지라고 가정
         * (25/10)*10 = 2*10 = 20, 21~30페이지 그룹에 속해있어야 하지만 20으로 떨어짐
         * (25/10)*10+1 = 21페이지 그룹에 속해있게 됨.
         * -1 이 필요한 이유 - 20페이지라고 가정
         * (20/10)*10+1 = 21, 11~20페이지 에 속해 있어야하지만 21이 됨
         * (20-1/10)*10+1 = (19/10)*10+1 = 11페이지 그룹에 속해 있게 됨.
         * */
        int startPageNumber = (page - 1) / 10 * 10 + 1;
        // 해당 페이지 그룹의 끝 번호(예: 11~20 에서 20)
        int endPageNumber = startPageNumber + 9;
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
        int prevPageNumber = startPageNumber - 10;
        /* 끝 번호 +1로 이후 그룹으로 이동 */
        int nextPageNumber = endPageNumber + 1;
        /* 현재 페이지가 1~10인 경우 prevPage는 0이 되므로 오류 방지*/
        if (prevPageNumber > 0) {
            pageInfo.put("prevPageNumber", startPageNumber - 10);
        }
        /* nextPageNumber가 가장 마지막 페이지 보다 작거나 클 때만 할당 */
        if (nextPageNumber < lastPageNumber) {
            pageInfo.put("nextPageNumber", nextPageNumber);
        }

        // 페이징
        /* LIMIT의 첫 번째 항목의 인덱스가 0부터 시작하기 때문에 -1을 해준다.
         LIMIT 첫번째 인수는 데이터의 인덱스를 나타남.
         즉 여기서는 10개씩 페이지에 나누어지므로 n번째 글부터 10개씩 반환
         (1-1)*10 = 0 -> LIMIT 0, 10 - 1페이지 - 1번 글 부터 10개 데이터 반환  1~10
         (2-1)*10 = 10 -> LIMIT 10,10, - 2페이지 - 10번 글 부터 10개씩 반환 10~20
         */
        int from = (page - 1) * 10;
        // 게시글을 from번째 부터 10개씩 객체형태로 담긴다.
        map.put("boardList", mapper.loadList(from, "%" + keyword + "%"));
        // 페이지 그룹을 나타내는 startPageNumber와 lastPageNumber가 담긴다.
        map.put("pageInfo", pageInfo);
        return map;
    }

    // 게시글 보기 로직
    public Board get(Integer id) {  // view
        Board board = mapper.selectById(id);

        List<BoardFile> boardFiles = fileMapper.selectNamesByBoardId(id);
        for(BoardFile boardFile : boardFiles){
            String url = urlPrefix + "mini-project/" + id+ "/" + boardFile.getName();
            boardFile.setUrl(url);
        }
        board.setFiles(boardFiles);
        return board;
    }

    // DB의 파일 레코드와 AWS에 저장된 파일 삭제
    private void deleteFile(Integer id){
        // 파일명 조회
        List<BoardFile> boardFiles = fileMapper.selectNamesByBoardId(id);

        // s3 bucket objects 지우기
        for(BoardFile file : boardFiles){
            String key = "mini-project/" +id+"/"+file.getName();

            DeleteObjectRequest objectRequest = DeleteObjectRequest.builder()
                    .bucket(bucket)
                    .key(key)
                    .build();

            s3.deleteObject(objectRequest);
        }

        fileMapper.deleteByBoardId(id);
    }
    // 게시글 삭제 로직
    public boolean remove(Integer id) {

        // 좋아요 레코드 삭제
        likeMapper.deleteByBoardId(id);

        deleteFile(id);

        return mapper.deleteById(id);

    }

    // 게시글 수정 로직
    public boolean update(Board board, List<Integer> removeFileIds, MultipartFile[] uploadFiles) throws IOException {
        // 파일 지우기
        if(removeFileIds != null) {
            for(Integer id: removeFileIds){
                // s3에서 지우기
                BoardFile file = fileMapper.selectById(id);
                String key = "mini-project/" + board.getId() + "/" + file.getName();
                DeleteObjectRequest objectRequest = DeleteObjectRequest.builder()
                        .bucket(bucket)
                        .key(key)
                        .build();
                s3.deleteObject(objectRequest);
                // db에서 지우기
                fileMapper.deleteById(id);
            }
        }

        // 파일 추가하기
        if(uploadFiles != null){
            // s3에 올리기
            for(MultipartFile file: uploadFiles) {
                upload(board.getId(), file);
                // db에 추가하기
                fileMapper.insert(board.getId(), file.getOriginalFilename());
            }
        }

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
        if (memberService.isAdmin(login)) {
            return true;
        }
        // 해당 글의 작성자 id를 가져옴
        Board board = mapper.selectById(id);
        // 작성자와 로그인 아이디가 동일한지 검증
        return board.getWriter().equals(login.getId());
    }

}
