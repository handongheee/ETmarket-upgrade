package kr.co.sist.etmarket.service;

import jakarta.transaction.Transactional;
import kr.co.sist.etmarket.dao.ItemTagDao;
import kr.co.sist.etmarket.dto.ItemTagDto;
import kr.co.sist.etmarket.entity.Item;
import kr.co.sist.etmarket.entity.ItemTag;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ItemTagService {
    private final ItemTagDao itemTagDao;

    // ItemTag DB insert
    public void insertItemTag(ItemTagDto itemTagDto, Item item) {
        // 태그 분리
        List<String> itemTagList = Arrays.asList(itemTagDto.getItemTagText().split("\\s+"));
        itemTagList.removeIf(String::isEmpty);

        for(String tag:itemTagList) {
            ItemTag itemTag = ItemTag.builder()
                    .item(item)
                    .itemTag(tag)
                    .build();

            itemTagDao.save(itemTag);
        }
    }

    // ItemTag DB getData(itemId)
    public String getItemTagsByItemId(Long itemId) {
        List<ItemTag> itemTagArray = itemTagDao.findByItemItemId(itemId);
        StringBuilder itemTagsBuilder = new StringBuilder();

        for (ItemTag itemTag : itemTagArray) {
            itemTagsBuilder.append(itemTag.getItemTag()).append(" ");
        }

        if (!itemTagsBuilder.isEmpty()) {
            // 마지막 공백 제거
            itemTagsBuilder.setLength(itemTagsBuilder.length() - 1);
        }

        return itemTagsBuilder.toString();
    }

    // ItemTag DB Delete(itemId)
    public void deleteItemTag(Long itemId) {
        itemTagDao.deleteByItemItemId(itemId);
    }

}
