package datajpa.study.repository;

import datajpa.study.entity.Item;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class ItemRepositoryTest {

    @Autowired ItemRepository itemRepository;

    @Test
    public void save() throws Exception{
        //given
        Item item=new Item("A");
        itemRepository.save(item);
        //when

        //then

    }
}