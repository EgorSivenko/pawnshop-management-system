package ua.nure.cpp.sivenko.practice6.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ua.nure.cpp.sivenko.practice6.model.Item;
import ua.nure.cpp.sivenko.practice6.model.ItemCategory;
import ua.nure.cpp.sivenko.practice6.service.ItemCategoryService;
import ua.nure.cpp.sivenko.practice6.service.ItemService;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Controller
public class ItemController {

    @Autowired
    private ItemService itemService;

    @Autowired
    private ItemCategoryService itemCategoryService;

    @GetMapping("/items")
    public String getItems(@RequestParam(value = "id", required = false) Long itemId, Model model) {
        if (itemId != null) {
            Item item = itemService.getItemById(itemId);
            if (item != null) {
                model.addAttribute("items", Collections.singletonList(item));
            }
        } else {
            List<Item> items = itemService.getAllItems();
            model.addAttribute("items", items);
        }
        List<ItemCategory> itemCategories = itemCategoryService.getAllItemCategories();
        model.addAttribute("itemCategories", itemCategories);
        return "item/items";
    }

    @GetMapping("/items/add")
    public String addItemForm(Model model) {
        Item item = new Item();
        model.addAttribute("item", item);

        List<ItemCategory> itemCategories = itemCategoryService.getAllItemCategories();
        model.addAttribute("itemCategories", itemCategories);
        return "item/add_item";
    }

    @PostMapping("/items")
    public String addItem(@ModelAttribute("item") Item item) {
        itemService.addItem(item);
        return "redirect:/items";
    }

    @GetMapping("/items/edit/{itemId}")
    public String updateItemForm(@PathVariable Long itemId, Model model) {
        Item item = itemService.getItemById(itemId);
        model.addAttribute("item", item);

        List<ItemCategory> itemCategories = itemCategoryService.getAllItemCategories();
        model.addAttribute("itemCategories", itemCategories);
        return "item/update_item";
    }

    @PostMapping("/items/{itemId}")
    public String updateItem(@PathVariable Long itemId, @ModelAttribute("item") Item item, Model model) {
        Item itemById = itemService.getItemById(itemId);
        if (!Objects.equals(itemById, item)) {
            itemService.updateItem(itemId, item);
        }
        return "redirect:/items";
    }

    @GetMapping("/items/{itemId}")
    public String deleteItem(@PathVariable Long itemId, Model model) {
        try {
            itemService.deleteItem(itemId);
        } catch (SQLException e) {
            model.addAttribute("error", e.getMessage());
        }
        return "redirect:/items";
    }
}
