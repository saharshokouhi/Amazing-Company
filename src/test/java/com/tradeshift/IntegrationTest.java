package com.tradeshift;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.io.File;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class IntegrationTest {
    @Autowired
    private MockMvc mvc;

    @Test
    public void doScenario_1()
            throws Exception {
        File resource = new ClassPathResource("tree/normal_1.json").getFile();
        String payload = new String(Files.readAllBytes(resource.toPath()));

        mvc.perform(put("/tree")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
                .andExpect(status().is2xxSuccessful());
        String result = mvc.perform(get("/tree/1/children"))
                .andExpect(status().is2xxSuccessful())
                .andReturn()
                .getResponse()
                .getContentAsString();
        ObjectMapper mapper = new ObjectMapper();

        List<Map> children = mapper.readValue(result, List.class);
        System.out.println(children);
        Assert.assertEquals(children.get(0).get("id"), 2);
        Assert.assertEquals(children.get(1).get("id"), 3);
        Assert.assertEquals(children.get(2).get("id"), 4);

        Assert.assertEquals(children.get(0).get("height"), 1);

        mvc.perform(patch("/tree/2/4"))
                .andExpect(status().is2xxSuccessful());

        result = mvc.perform(get("/tree/4/children"))
                .andExpect(status().is2xxSuccessful())
                .andReturn()
                .getResponse()
                .getContentAsString();
        children = mapper.readValue(result, List.class);
        Assert.assertEquals(children.get(0).get("id"), 2);
        Assert.assertEquals(children.get(0).get("height"), 2);
    }
}

