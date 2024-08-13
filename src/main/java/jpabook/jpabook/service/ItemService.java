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
    public Item updateItem(Long itemId, Book param){
        Item findItem = itemRepository.findOne(itemId);
        findItem.setPrice(param.getPrice());
        findItem.setName(param.getName());
        findItem.setPrice(param.getPrice());
        findItem.setStockQuantity(param.getStockQuantity());

        return findItem;
    }

    public List<Item> findItems(){
        return itemRepository.findAll();
    }

    public Item findOne(Long itemId){
        return itemRepository.findOne(itemId);
    }


}
