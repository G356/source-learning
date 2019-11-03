package com.example.demo.web.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author : zhuqiang
 * @version : V1.0
 * @date : 2018/8/1 22:48
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserControllerTest {
    @Autowired
    private WebApplicationContext wac;

    // 伪造的mvc不会真正去启动项目?
    // 相对来说会比直接启用项目要快
    private MockMvc mockMvc;

    @Before
    public void setup() {
        // befor 注解，每个测试用例执行前都会执行
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    // 查询成功的测试用例
    @Test
    public void whenQuerySuccess() throws Exception {
        String contentAsString = mockMvc
                // 发起请求
                .perform(get("/user")
                                 .param("username", "mrcode")
                                 .param("age", "1")
                                 .param("ageTo", "3")
                                 .param("xxx", "test")
                                 // 添加请求头为json
                                 .contentType(APPLICATION_JSON_UTF8)
                )
                // 期望的结果
                // 这里期望返回的http状态码为200
                .andExpect(status().isOk())
                // 从返回的结果中（json）获取长度，期望长度为3
                .andExpect(jsonPath("$.length()").value(3))
                .andReturn().getResponse().getContentAsString();
        System.out.println(contentAsString);
    }

    // 获取用户详情成功测试用例
    @Test
    public void whenGetInfoSuccess() throws Exception {
        String contentAsString = mockMvc.perform(get("/user/1")
                                                         .contentType(APPLICATION_JSON_UTF8)
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("mrcode"))
                .andReturn().getResponse().getContentAsString();
        System.out.println(contentAsString);
    }

    // 获取用户详情，传递id不为数字，失败测试
    @Test
    public void whenGetInfoFail() throws Exception {
        mockMvc.perform(get("/user/a")
                                .contentType(APPLICATION_JSON_UTF8)
        )
                // 打印请求响应详细日志，可以在控制台看到详细的日志信息
                .andDo(MockMvcResultHandlers.print())
                // 返回结果为 404
                .andExpect(status().is4xxClientError())
        ;
    }

    @Test
    public void whenCreateSuccess() throws Exception {
//        long time = new Date().getTime(); 等价于下面的
        long birthday = Instant.now().toEpochMilli();
        String content = "{\"username\":\"mrcode\",\"password\":null,\"birthday\":" + birthday + "}";
        String contentAsString = mockMvc.perform(post("/user")
                                                         .contentType(APPLICATION_JSON_UTF8)
                                                         .content(content)
        )
                .andExpect(status().isOk())
                // 因为是创建，一般创建完成后需要返回创建的id
                // 预期是返回1
                .andExpect(jsonPath("$.id").value("1"))
                .andReturn().getResponse().getContentAsString();
        System.out.println(contentAsString);
    }

    @Test
    public void whenUpdateSuccess() throws Exception {
        // jdk8的时间处理类；使用LocalDateTime必须传递时区
        // 给定一个一年后的时间
//        long birthday = LocalDateTime.now().plusYears(1).atZone(ZoneId.systemDefault()).toEpochSecond();
        // 注意这里的api不要传错了。是毫秒不是秒
        long birthday = LocalDateTime.now().plusYears(1).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        String content = "{\"username\":\"mrcode\",\"password\":null,\"birthday\":" + birthday + "}";
        String contentAsString = mockMvc.perform(put("/user/1")
                                                         .contentType(APPLICATION_JSON_UTF8)
                                                         .content(content)
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andReturn().getResponse().getContentAsString();
        System.out.println(contentAsString);
    }

    @Test
    public void whenDeleteSuccess() throws Exception {
        mockMvc.perform(delete("/user/1")
                                .contentType(APPLICATION_JSON_UTF8)
        )
                .andExpect(status().isOk());
    }

    @Test
    public void whenFileUploadSuccess() throws Exception {
        // v5.0+ fileUpLoad方法已经过时了
        String file = mockMvc.perform(multipart("/file")
                                              .file(new MockMultipartFile("file", "test.txt", "multipart/form-data", "hello upload".getBytes())))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        System.out.println(file);
    }

    @Test
    public void whenFileDownloadSuccess() throws Exception {
        // v5.0+ fileUpLoad方法已经过时了
        String file = mockMvc.perform(get("/file/fileUploadTest.txt"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        System.out.println(file);
    }
}
