package com.miracle.miraclemorningback;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.miracle.miraclemorningback.entity.ResultEntity;
import com.miracle.miraclemorningback.repository.ResultRepository;
import com.miracle.miraclemorningback.repository.RoutineRepository;

@Component
public class ScheduledTasks {

    @Autowired
    RoutineRepository routineRepository;

    @Autowired
    ResultRepository resultRepository;

    // 인증 사진 저장 경로
    @Value("${images.path}")
    private String DIR_PATH;

    @Scheduled(cron = "0 0 4 * * *") // 매일 새벽 4시에 작동
    public void LegacyFilesDelete() {

        // 파일이 있는 디렉토리 경로
        Path directoryPath = Paths.get(DIR_PATH);

        // 삭제할 기간 (5일 전 파일 삭제)
        int daysBeforeDeletion = 5;

        // 현재 시각 기준으로 daysBeforeDeletion 이전의 시각 계산
        Instant threshold = Instant.now().minus(daysBeforeDeletion, ChronoUnit.DAYS);

        try {
            try (// 디렉토리 내의 모든 파일에 대한 스트림 얻기
                    DirectoryStream<Path> directoryStream = Files.newDirectoryStream(directoryPath)) {
                // 파일들을 순회하면서 특정 기간 이전 파일 삭제
                for (Path filePath : directoryStream) {
                    // 파일의 생성 시각 가져오기
                    Instant creationTime = Files.readAttributes(filePath, BasicFileAttributes.class)
                            .creationTime().toInstant();

                    // 특정 기간 이전에 생성된 파일인 경우 삭제
                    if (creationTime.isBefore(threshold)) {
                        Files.delete(filePath);
                        System.out.println("파일이 삭제되었습니다: " + filePath);
                    }
                }
            }

            System.out.println("삭제 작업이 완료되었습니다.");
        } catch (NoSuchFileException e) {
            System.err.println("삭제하려는 파일이 없습니다.");
        } catch (IOException e) {
            System.err.println("파일 삭제 중 오류가 발생했습니다: " + e.getMessage());
        }
    }

    @Scheduled(cron = "0 0 0 * * *") // 매일 자정에 작동
    // 인증되지 않은 루틴 기록 생성
    public void generateIncompleteResults() {
        routineRepository.getActivatedRoutines().forEach(routineEntity -> {
            ResultEntity resultEntity = ResultEntity.builder()
                    .memberName(routineEntity.getMemberName())
                    .routineName(routineEntity.getRoutineName())
                    .build();
            resultRepository.save(resultEntity);
        });
    }
}
