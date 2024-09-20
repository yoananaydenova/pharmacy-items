package com.naydenova.pharmacy_items.repositories;

import com.naydenova.pharmacy_items.entities.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    Optional<Item> findByItemUrl(String itemUrl);
}
