package com.victormiranda.mani.core.test.it;

import com.victormiranda.mani.bean.category.Category;
import com.victormiranda.mani.core.service.category.CategoryService;
import com.victormiranda.mani.type.TransactionFlow;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.List;
import java.util.Optional;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
@SpringApplicationConfiguration(classes = ITApp.class)
@WebAppConfiguration
@IntegrationTest("server.port:0")
public class CategoryServiceIT {

    @Autowired
    private CategoryService categoryService;

    @Test
    public void getGroceriesCategory() {
        List<Category> groceries = categoryService.findCategories("Groceries", Optional.empty());

        Assert.assertEquals(groceries.size(), 1);
    }

    @Test
    public void getGroceriesCategoryWithOutFlow() {
        List<Category> groceries = categoryService.findCategories("Groceries", Optional.of(TransactionFlow.OUT));

        Assert.assertEquals(groceries.size(), 1);
    }

    @Test
    public void getGroceriesCategoryWithINFlow() {
        List<Category> groceries = categoryService.findCategories("Groceries", Optional.of(TransactionFlow.IN));

        Assert.assertEquals(groceries.size(), 0);
    }
}
