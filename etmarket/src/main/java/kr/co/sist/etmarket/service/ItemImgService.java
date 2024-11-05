package kr.co.sist.etmarket.service;

import jakarta.transaction.Transactional;
import kr.co.sist.etmarket.dao.ItemImgDao;
import kr.co.sist.etmarket.dto.ItemImgDto;
import kr.co.sist.etmarket.entity.Item;
import kr.co.sist.etmarket.entity.ItemImg;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ItemImgService {
    private final ItemImgDao itemImgDao;
    private  final S3Uploader s3Uploader;

    // ItemImg DB Insert
    public void insertItemImg(ArrayList<MultipartFile> itemImgUpload, Item item) {
        ArrayList<String> itemImgUrls = new ArrayList<>();

        for(MultipartFile  itemImg:itemImgUpload) {
            try {
                itemImgUrls.add(s3Uploader.upload(itemImg, "itemImg"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        for (String itemImgUrl:itemImgUrls) {
            ItemImg itemImg = ItemImg.builder()
                    .item(item)
                    .itemImg(itemImgUrl)
                    .build();

            itemImgDao.save(itemImg);
        }
    }

    // ItemImg DB getData(itemId)
    public List<ItemImgDto> getItemImgDataByItemId(Long itemId) {
        List<ItemImg> itemImgs = itemImgDao.findByItemItemId(itemId);
        List<ItemImgDto> itemImgDtos = new ArrayList<>();

        for (ItemImg itemImg : itemImgs) {
            ItemImgDto dto = new ItemImgDto();
            dto.setItemImgId(itemImg.getItemImgId());
            dto.setItemImg(itemImg.getItemImg());

            itemImgDtos.add(dto);
        }

        return itemImgDtos;
    }

    // ItemImg DB getData(itemImgId)
    public ItemImgDto getItemImgDataByItemImgId(Long itemImgId) {
        ItemImg itemImg = itemImgDao.findByItemImgId(itemImgId);

        ItemImgDto itemImgDto = new ItemImgDto();
        itemImgDto.setItemImgId(itemImg.getItemImgId());
        itemImgDto.setItemImg(itemImg.getItemImg());

        return itemImgDto;
    }

    // ItemImg DB Update
    public void updateItemImg(ArrayList<MultipartFile> itemImgUpload,ItemImgDto itemImgDto , int itemImgCount, Item item) {
        if (itemImgUpload.isEmpty() || itemImgUpload.get(0).getSize() > 0) {
            List<ItemImgDto> itemImgDtos = getItemImgDataByItemId(item.getItemId());

            for (ItemImgDto itemImgDto2 : itemImgDtos) {
                s3Uploader.deleteFile(getPath(itemImgDto2.getItemImg()));
                itemImgDao.deleteByItemImgId(itemImgDto2.getItemImgId());
            }

            insertItemImg(itemImgUpload, item);

        } else if (itemImgDto.getItemImgUploadCount() != itemImgCount) {
            List<String> itemDeleteImgIdList = Arrays.asList(itemImgDto.getItemDeleteImgIds().split("\\s+"));

            for (String itemDeleteImgId : itemDeleteImgIdList) {
                Long imgId = Long.parseLong(itemDeleteImgId);

                ItemImgDto itemImgDto2 = getItemImgDataByItemImgId(imgId);

                s3Uploader.deleteFile(getPath(itemImgDto2.getItemImg()));

                itemImgDao.deleteByItemImgId(imgId);
            }
        }
    }

    // Delete를 위한 이미지명 변환
    public String getPath(String itemImgName) {
        String target = "itemImg/";
        int index = itemImgName.indexOf(target);

        return itemImgName.substring(index);

    }

    // ItemImg S3 Delete(itemId)
    public void deleteItemImgS3(Long itemId){
        List<ItemImgDto> itemImgDtos = getItemImgDataByItemId(itemId);

        for (ItemImgDto itemImgDto : itemImgDtos) {
            s3Uploader.deleteFile(getPath(itemImgDto.getItemImg()));
        }
    }

    public String getFirstItemImgByItemId(Long itemId) {
        return itemImgDao.findFirstItemImgByItemId(itemId);
    }


    /*마이페이지에서 사용*/
   /*@Autowired
    public ItemImgService(ItemImgDao itemImgDao) {
        this.itemImgDao = itemImgDao;
    }
*/
    public ItemImg getFirstImageByItemId(Long itemId) {
        Optional<ItemImg> optionalItemImg = itemImgDao.findFirstByItem_ItemIdOrderByItemImgIdAsc(itemId);
        return optionalItemImg.orElse(null); // orElse를 사용하여 값이 없을 경우 null을 반환하도록 처리
    }
    
    
}
