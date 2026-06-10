package kr.adapterz.springboot.report;

import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class ReportRepository {
    private final Map<Long, Report> store = new HashMap<>();
    private Long sequence = 1L;

    public Report save(Report report) {
        report.setId(sequence);
        store.put(sequence, report);
        sequence++;

        return report;
    }

    public Optional<Report> findByPostIdAndUserId(Long postId, Long userId) {
        return store.values().stream()
                .filter(report -> report.getPostId().equals(postId))
                .filter(report -> report.getUserId().equals(userId))
                .findFirst();
    }

    public boolean existsByPostIdAndUserId(Long postId, Long userId) {
        return findByPostIdAndUserId(postId, userId).isPresent();
    }

    public Long countByPostId(Long postId) {
        return store.values().stream()
                .filter(report -> report.getPostId().equals(postId))
                .count();
    }
}
