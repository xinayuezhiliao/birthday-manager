package com.birthdaymanager;

import com.birthdaymanager.model.Birthday;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
public class BirthdayControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    // 测试场景1：创建生日信息 - 成功场景
    @Test
    public void testCreateBirthday_Success() throws Exception {
        Birthday birthday = new Birthday();
        birthday.setName("John Doe");
        birthday.setBirthDate(LocalDate.of(1990, 1, 1));
        birthday.setRelationship("Friend");
        birthday.setNotes("Test notes");

        MvcResult result = mockMvc.perform(post("/api/birthdays")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(birthday)))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        Birthday savedBirthday = objectMapper.readValue(content, Birthday.class);
        assertNotNull(savedBirthday.getId());
        assertEquals("John Doe", savedBirthday.getName());
    }

    // 测试场景2：创建生日信息 - 无效日期
    @Test
    public void testCreateBirthday_InvalidDate() throws Exception {
        Birthday birthday = new Birthday();
        birthday.setName("John Doe");
        birthday.setBirthDate(LocalDate.now().plusDays(1)); // 未来日期
        birthday.setRelationship("Friend");

        mockMvc.perform(post("/api/birthdays")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(birthday)))
                .andExpect(status().isBadRequest());
    }

    // 测试场景3：获取不存在的生日信息
    @Test
    public void testGetBirthday_NotFound() throws Exception {
        mockMvc.perform(get("/api/birthdays/999"))
                .andExpect(status().isNotFound());
    }

    // 测试场景4：更新生日信息 - 并发修改
    @Test
    public void testUpdateBirthday_ConcurrentModification() throws Exception {
        // 首先创建一个生日信息
        Birthday birthday = new Birthday();
        birthday.setName("Jane Doe");
        birthday.setBirthDate(LocalDate.of(1995, 1, 1));
        birthday.setRelationship("Family");

        MvcResult createResult = mockMvc.perform(post("/api/birthdays")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(birthday)))
                .andExpect(status().isOk())
                .andReturn();

        Birthday savedBirthday = objectMapper.readValue(
                createResult.getResponse().getContentAsString(), 
                Birthday.class
        );

        // 模拟并发更新
        Birthday update1 = new Birthday();
        update1.setName("Jane Smith");
        update1.setBirthDate(LocalDate.of(1995, 1, 1));
        update1.setRelationship("Family");

        Birthday update2 = new Birthday();
        update2.setName("Jane Johnson");
        update2.setBirthDate(LocalDate.of(1995, 1, 1));
        update2.setRelationship("Family");

        // 执行并发更新
        mockMvc.perform(put("/api/birthdays/" + savedBirthday.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(update1)))
                .andExpect(status().isOk());

        mockMvc.perform(put("/api/birthdays/" + savedBirthday.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(update2)))
                .andExpect(status().isOk());
    }

    // 测试场景5：搜索生日信息 - 特殊字符
    @Test
    public void testSearchBirthday_SpecialCharacters() throws Exception {
        mockMvc.perform(get("/api/birthdays/search")
                .param("name", "John%Doe"))
                .andExpect(status().isOk());
    }

    // 测试场景6：批量操作 - 获取多个生日
    @Test
    public void testGetMultipleBirthdays() throws Exception {
        // 创建多个生日信息
        for (int i = 0; i < 5; i++) {
            Birthday birthday = new Birthday();
            birthday.setName("Test User " + i);
            birthday.setBirthDate(LocalDate.of(1990, 1, 1).plusYears(i));
            birthday.setRelationship("Friend");

            mockMvc.perform(post("/api/birthdays")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(birthday)))
                    .andExpect(status().isOk());
        }

        // 获取所有生日信息
        mockMvc.perform(get("/api/birthdays"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(5));
    }

    // 测试场景7：获取即将到来的生日
    @Test
    public void testGetUpcomingBirthdays() throws Exception {
        // 创建一些即将到来的生日
        Birthday birthday1 = new Birthday();
        birthday1.setName("Coming Soon 1");
        birthday1.setBirthDate(LocalDate.now().minusYears(30).plusDays(5));
        birthday1.setRelationship("Friend");

        Birthday birthday2 = new Birthday();
        birthday2.setName("Coming Soon 2");
        birthday2.setBirthDate(LocalDate.now().minusYears(25).plusDays(10));
        birthday2.setRelationship("Family");

        mockMvc.perform(post("/api/birthdays")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(birthday1)))
                .andExpect(status().isOk());

        mockMvc.perform(post("/api/birthdays")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(birthday2)))
                .andExpect(status().isOk());

        // 获取未来30天的生日
        mockMvc.perform(get("/api/birthdays/upcoming")
                .param("days", "30"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }
}
