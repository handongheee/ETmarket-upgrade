package kr.co.sist.etmarket.dao;

import kr.co.sist.etmarket.entity.ItemTag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemTagDao extends JpaRepository<ItemTag, Long> {
    // ItemId값에 따른 ItemTag 출력
    List<ItemTag> findByItemItemId(Long itemId);

    // ItemId값에 따른 ItemTag 삭제
    void deleteByItemItemId(Long itemId);
}
