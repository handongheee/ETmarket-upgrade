package kr.co.sist.etmarket;

import kr.co.sist.etmarket.etenum.CategoryName;
import kr.co.sist.etmarket.etenum.ItemStatus;

import java.util.HashMap;
import java.util.Map;

public class KeyValueMapper {

    private static final Map<CategoryName, String> CATEGORY_MAP = new HashMap<>();
    private static final Map<ItemStatus, String> ITEM_STATUS_MAP = new HashMap<>();

    static {
        CATEGORY_MAP.put(CategoryName.식품, "식품");
        CATEGORY_MAP.put(CategoryName.패션, "패션");
        CATEGORY_MAP.put(CategoryName.전자기기, "전자기기");
        CATEGORY_MAP.put(CategoryName.생활주방, "생활/주방");
        CATEGORY_MAP.put(CategoryName.뷰티미용, "뷰티/미용");
        CATEGORY_MAP.put(CategoryName.도서, "도서");
        CATEGORY_MAP.put(CategoryName.티켓교환권, "티켓/교환권");
        CATEGORY_MAP.put(CategoryName.기타, "기타");

        ITEM_STATUS_MAP.put(ItemStatus.새상품, "새 상품(미사용)");
        ITEM_STATUS_MAP.put(ItemStatus.사용감없음, "사용감 없음");
        ITEM_STATUS_MAP.put(ItemStatus.사용감적음, "사용감 적음");
        ITEM_STATUS_MAP.put(ItemStatus.사용감많음, "사용감 많음");
        ITEM_STATUS_MAP.put(ItemStatus.고장파손상품, "고장/파손 상품");
    }

    public static String getValue(CategoryName categoryName) {
        return CATEGORY_MAP.get(categoryName);
    }

    public static String getValue(ItemStatus itemStatus) {
        return ITEM_STATUS_MAP.get(itemStatus);
    }
}
