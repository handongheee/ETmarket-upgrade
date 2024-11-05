package kr.co.sist.etmarket.dao;

import kr.co.sist.etmarket.entity.Rating;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Rating, Long> {

    int countByTarget_UserId(Long userId);

    List<Rating> findByTarget_UserIdOrderByRatingDateDesc(Long userId, Pageable pageable);

    List<Rating> findByTarget_UserIdOrderByRatingDateDesc(Long userId);

    @Query("SELECT AVG(r.rating) FROM Rating r WHERE r.target.userId = :userId")
    Optional<Double> findByAverageReviewScoreByUserId(@Param("userId") Long userId);
}

