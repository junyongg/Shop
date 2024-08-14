package jpabook.jpabook.service;

import jpabook.jpabook.domain.item.Book;
import jpabook.jpabook.domain.item.Item;
import jpabook.jpabook.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    @Transactional
    public void saveItem(Item item) {
        itemRepository.save(item);
    }

    @Transactional //TX가 있으니까 set된 데이터들을 JPA가 자동으로 커밋 & 플러쉬해서 업데이트 시켜줌
    public void updateItem(Long itemId, String name, int price, int stockQuantity ){
        Item findItem = itemRepository.findOne(itemId);
        //findItem.change(name, price, stockQuantity);
        //set을 남발하면 데이터 변경 부분을 추적하기가 어렵다. 그니까 무조건 그 데이터 변경에 맞는 메소드를 만드는게 낫다
        //어디에? 엔티티쪽에 ㅋㅋ
        findItem.setName(name);
        findItem.setPrice(price);
        findItem.setStockQuantity(stockQuantity);
    }

    public List<Item> findItems(){
        return itemRepository.findAll();
    }

    public Item findOne(Long itemId){
        return itemRepository.findOne(itemId);
    }


}
